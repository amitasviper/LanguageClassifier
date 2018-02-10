package com.appradar.viper.amazingfacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kontext.Kontext;

import org.json.JSONObject;

import utility.TrackerApplication;


public class ShowFactActivity extends Activity {

    DatabaseHelper databaseHelper;

    TextView total_count_tv;
    TextSwitcher fact_body;
    EditText fact_number;
    ImageView share;

    boolean set_text_changed = true;
    boolean notification_flag = true;

    String fact_language;
    Category categoryHolder;

    private float locX1, locX2;
    static final int max_displacement = 120;
    private int factCount = 0;

    public static boolean DEBUG = false;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prepareDatabase();
        interstetialAdSetup();
        bannerAdSetup();
        prepareCategory();

        AppRater.showRateDialogIfNeeded(this);

        total_count_tv = (TextView) findViewById(R.id.total_count);
        fact_body = (TextSwitcher) findViewById(R.id.fact_body);
        fact_number = (EditText) findViewById(R.id.fact_number);

        fact_number.addTextChangedListener(new MyTextWatcher());


        fact_body.clearAnimation();
        fact_body.setInAnimation(this, R.anim.grow_fade_in);

        fact_body.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(ShowFactActivity.this);
                textView.setTextSize(30);
                textView.setTextColor(Color.YELLOW);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setShadowLayer(10, 10, 10, Color.BLACK);
                return textView;
            }
        });

        setTotalCount();
        shareFact();

        startTracker();
    }

    private void startTracker(){
        TrackerApplication trackerApplication = (TrackerApplication) getApplication();
        Tracker myTracker = trackerApplication.getDefaultTracker();

        myTracker.setScreenName(this.getClass().toString());
        myTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onBackPressed() {
        if(notification_flag){
            Intent intent = new Intent(this, Language.class);
            startActivity(intent);
            finish();
        }
        else{
            finish();
            Kontext.sendTag("totalFactRead", String.valueOf(factCount));
        }
    }

    private void shareFact(){
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TrackerApplication trackerApplication = (TrackerApplication) getApplication();
                Tracker myTracker = trackerApplication.getDefaultTracker();
                myTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Show Facts")
                        .setAction("Share")
                        .setLabel("Sharing")
                        .setValue(1)
                        .build());

                try {
                    JSONObject tags = new JSONObject();
                    tags.put("share", "shared");
                    tags.put("factNo", factCount);
                    Kontext.sendTags(tags);

                } catch (Exception e) {

                }

                String downloadText = "Download the app now:";
                String url = "\nhttps://goo.gl/Ez6A8D";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Facts");
                shareIntent.putExtra(Intent.EXTRA_TEXT, categoryHolder.getFactBody() + "\n" + downloadText + url);
                startActivity(Intent.createChooser(shareIntent, "Share Via"));
            }
        });
    }

    private void prepareCategory(){
        Intent intent = getIntent();
        notification_flag = intent.getBooleanExtra("notification_flag", false);
        if(notification_flag){
            fact_language = intent.getStringExtra("language");
            String category_id = intent.getStringExtra("category_id");
            int fact_id = intent.getIntExtra("fact_id", 1);
            categoryHolder = new Category(this, category_id, fact_id);
        }
        else {
            fact_language = intent.getStringExtra("language");
            String category_id = intent.getStringExtra("category_id");
            categoryHolder = new Category(this, category_id);
        }
    }

    void setTotalCount(){
        total_count_tv.setText("/" + categoryHolder.total_facts);
        set_text_changed = false;
        fact_number.setText("1");
        set_text_changed = true;
        displayFact();
    }


    void displayFact(){
        set_text_changed = false;
        fact_number.setText(""+categoryHolder.current);
        set_text_changed = true;
        fact_body.setText(categoryHolder.getFactBody());

        factCount = Integer.parseInt(fact_number.getText().toString());
        if(factCount % 5 == 0){
            if(interstitialAd.isLoaded()){
                interstitialAd.show();
            }
        }

    }

    void displayPrevious(){
        boolean has_room = categoryHolder.moveToPrev();
        if(has_room) {
            displayFact();
        }
        else{
            Toast.makeText(this, "Reached at the beginning", Toast.LENGTH_LONG).show();
        }
    }

    void displayNext(){
        boolean has_room = categoryHolder.moveToNext();

        if(has_room) {
            displayFact();
        }
        else{
            Toast.makeText(this, "Reached at the end", Toast.LENGTH_LONG).show();
        }
    }

    void displayFactAtPosition(){
        String txt = fact_number.getText().toString();
        if(txt.isEmpty())
            txt = "" + categoryHolder.current;
        int current = Integer.parseInt(txt);

        if(current <= categoryHolder.total_facts && current > 0) {
            categoryHolder.setFactPos(current);
            displayFact();

            if(current % 5 == 0){
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
            }
        }
        else{
            Toast.makeText(this, "Reached at the end", Toast.LENGTH_LONG);
            Kontext.sendTag("readAll", "readAll");
        }
    }

/*    void prepareDatabase(){
        databaseHelper = new DatabaseHelper(ShowFactActivity.this);
        databaseHelper.openDatabase();
    }*/


    private void bannerAdSetup(){
        AdView adView = (AdView) findViewById(R.id.displayFactAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("AdLoaded", "Ad Loaded Successfully");
                createSpaceForAd();
            }
        });
    }

    private void interstetialAdSetup(){
        ManageAds.prepareAds(this);
        interstitialAd = ManageAds.getInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                ManageAds.refreshAd();
            }
        });
    }

    private void createSpaceForAd(){
        LinearLayout list_ly = (LinearLayout) findViewById(R.id.fact_area);
        LinearLayout list_ad_ly = (LinearLayout) findViewById(R.id.fact_ad_area);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) list_ly.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) list_ad_ly.getLayoutParams();

        params1.weight = 7.5f;
        params2.weight = 1.5f;

        list_ly.setLayoutParams(params1);
        list_ad_ly.setLayoutParams(params2);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                locX1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                locX2 = event.getX();
                float deltaX = locX1 - locX2;
                if(Math.abs(deltaX) > max_displacement && (locX2 < locX1)){
                    displayNext();

                }
                else if(Math.abs(deltaX) > max_displacement && (locX2 > locX1)){
                    displayPrevious();
                }
                else{

                }
        }
        return super.dispatchTouchEvent(event);
    }

    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            int pos = fact_number.getText().length();
            fact_number.setSelection(pos);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int pos = fact_number.getText().length();
            fact_number.setSelection(pos);

            if(set_text_changed)
                displayFactAtPosition();

        }
    }
}

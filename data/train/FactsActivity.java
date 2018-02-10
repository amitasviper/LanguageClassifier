package com.appradar.viper.amazingfacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuild;
import com.google.android.gms.analytics.Tracker;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.kontext.Kontext;

import org.json.JSONObject;

import utility.TrackerApplication;

/**
 * Created by akshaypc on 18/10/17.
 */

public class FactsActivity extends IntroActivity {

    public static final String EXTRA_FULLSCREEN = "com.appradar.viper.amazingfacts.EXTRA_FULLSCREEN";
    public static final String EXTRA_SCROLLABLE = "com.appradar.viper.amazingfacts.EXTRA_SCROLLABLE";
    public static final String EXTRA_CUSTOM_FRAGMENTS = "com.appradar.viper.amazingfacts.EXTRA_CUSTOM_FRAGMENTS";
    public static final String EXTRA_SHOW_BACK = "com.appradar.viper.amazingfacts.EXTRA_SHOW_BACK";
    public static final String EXTRA_SHOW_NEXT = "com.appradar.viper.amazingfacts.EXTRA_SHOW_NEXT";
    public static final String EXTRA_SKIP_ENABLED = "com.appradar.viper.amazingfacts.EXTRA_SKIP_ENABLED";

    boolean notification_flag = true;
    String fact_language;
    Category categoryHolder;
    boolean fullscreen, scrollable, showBack, showNext, skipEnabled, customFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prepareCategory();
        setFullscreen(fullscreen);

        super.onCreate(savedInstanceState);

        setButtonBackFunction(skipEnabled ? BUTTON_BACK_FUNCTION_SKIP : BUTTON_BACK_FUNCTION_BACK);
        setButtonBackVisible(showBack);
        setButtonNextVisible(showNext);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        addSlide(new SimpleSlide.Builder()
                .title("BUCKLE UP!")
                .description("Daily Dose of Amazing Facts coming next. :)")
                .background(R.color.color_material_bold)
                .backgroundDark(R.color.color_dark_material_bold)
                .scrollable(scrollable)
                .buttonCtaLabel("Start")
                .buttonCtaClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast
                                .makeText(FactsActivity.this, "Launching", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                        nextSlide();
                    }
                })
                .build());

        for(int i = 0; i < 25; i++) {
            addSlide(new SimpleSlide.Builder()
                    .title("Fact Title")
                    .description("Fact goes here")
                    .background(R.color.color_material_bold)
                    .backgroundDark(R.color.color_dark_material_bold)
                    .scrollable(scrollable)
                    .buttonCtaLabel("Share")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast = Toast
                                    .makeText(FactsActivity.this, "Sharing", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.show();
                            shareFact();
                        }
                    })
                    .build());
        }
    }

    private void shareFact(){

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
                    tags.put("factNo", "FactCount Goes Here");
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
        fullscreen = intent.getBooleanExtra(EXTRA_FULLSCREEN, false);
        scrollable = intent.getBooleanExtra(EXTRA_SCROLLABLE, false);
        showBack = intent.getBooleanExtra(EXTRA_SHOW_BACK, true);
        showNext = intent.getBooleanExtra(EXTRA_SHOW_NEXT, true);
        skipEnabled = intent.getBooleanExtra(EXTRA_SKIP_ENABLED, true);
        customFragments = intent.getBooleanExtra(EXTRA_CUSTOM_FRAGMENTS, true);

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
//            Kontext.sendTag("totalFactRead", String.valueOf(factCount));
        }
    }
}

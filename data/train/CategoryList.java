package com.appradar.viper.amazingfacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appradar.viper.amazingfacts.databinding.ActivitySplashScreenBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kontext.Kontext;

import utility.TrackerApplication;


public class CategoryList extends Activity {

    ListView list;
    String language;

    ProgressDialog dialog;
    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        list = (ListView) findViewById(R.id.category_list);
        prepareList();
        bannerAdSetup();
        startTracker();
    }

    private void startTracker(){
        TrackerApplication trackerApplication = (TrackerApplication) getApplication();
        Tracker myTracker = trackerApplication.getDefaultTracker();

        myTracker.setScreenName(this.getClass().toString());
        myTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setActivityTitle(String lan){
        if(lan.equalsIgnoreCase("1")){
            setTitle("English");
        }
        else if(lan.equalsIgnoreCase("2")){
            setTitle("Hindi");
        }
        else if(lan.equalsIgnoreCase("3")){
            setTitle("Russian");
        }
    }

    private void prepareList(){

        Intent intent = getIntent();
        language = intent.getStringExtra("language");

        setActivityTitle(language);

        Cursor cursor;
        DatabaseHelper jDatabase = new DatabaseHelper(this);
        jDatabase.openDatabase();
        cursor = jDatabase.fetchAllSubCategories(language);

        String[] columns = {jDatabase.CATEGORY_ID, jDatabase.CATEGORY_NAME};
        int[] to = new int[]{R.id.list_category_image, R.id.list_category_text, R.id.list_category_id};

        CustomList customCursorListAdapter = new CustomList(this,R.layout.list_item,cursor,columns,to,0);

        list.setAdapter(customCursorListAdapter);
        list.setOnItemClickListener(new ListViewOnItemClickListener());
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void bannerAdSetup(){
        AdView adView = (AdView) findViewById(R.id.listAd);
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

    private void createSpaceForAd(){
        LinearLayout list_ly = (LinearLayout) findViewById(R.id.list_area);
        LinearLayout list_ad_ly = (LinearLayout) findViewById(R.id.list_ad_area);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) list_ly.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) list_ad_ly.getLayoutParams();

        params1.weight = 8.5f;
        params2.weight = 1.5f;

        list_ly.setLayoutParams(params1);
        list_ad_ly.setLayoutParams(params2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dialog != null){
            dialog.dismiss();
        }
    }

    private void displayProgressBar(){
        dialog = new ProgressDialog(CategoryList.this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    public class ListViewOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            displayProgressBar();
            LinearLayout lin_lay = (LinearLayout) getViewByPosition(position,list);

            TextView idTextView = (TextView) lin_lay.getChildAt(2);
            String category_id = idTextView.getText().toString();
            Kontext.sendTag("category", category_id);

            Intent intent = new Intent(CategoryList.this, FactsActivity.class);
            intent.putExtra(FactsActivity.EXTRA_FULLSCREEN, true);
            intent.putExtra(FactsActivity.EXTRA_SCROLLABLE, true);
            intent.putExtra(FactsActivity.EXTRA_CUSTOM_FRAGMENTS, true);
            intent.putExtra(FactsActivity.EXTRA_SKIP_ENABLED, false);
            intent.putExtra(FactsActivity.EXTRA_SHOW_BACK, true);
            intent.putExtra(FactsActivity.EXTRA_SHOW_NEXT, true);

            intent.putExtra("category_id", category_id);
            intent.putExtra("language", language);
            intent.putExtra("notification_flag", false);
            startActivity(intent);
        }
    }

}

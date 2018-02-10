package com.appradar.wrestlingquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import utils.AppConstants;
import utils.AppController;
import utils.AppRater;
import utils.Category;

public class MainActivity extends BaseAppActivity
{
    public static Context context;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        ImageView playVideoBtn = (ImageView) findViewById(R.id.playVideo);
        playVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayVideo.class);
                startActivity(intent);
            }
        });

        createScreen();
        ReviewDialog();

        intialiseAdmobFeatures();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void ReviewDialog()
    {
        AppRater.onStart(MainActivity.this);
        AppRater.showRateDialogIfNeeded(MainActivity.this);
    }

    private void createScreen(){
        ArrayList<Category> arrayList = prepareCategories();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) findViewById(R.id.category_option_layout);
        //Log.e("MainActivity", "createScreen:Function Called at least Size : " + arrayList.size());

        CustomOnClickListener customOnClickListener = new CustomOnClickListener();

        for(int i = 0; i < arrayList.size() ;) {
            View to_add;
            TextView textView1, textView2;
            ImageView imageView1, imageView2;

            to_add = inflater.inflate(R.layout.type_two, parent, false);

            textView1 = (TextView) to_add.findViewById(R.id.category_text_view_one);
            imageView1 = (ImageView) to_add.findViewById(R.id.category_image_view_one);
            textView1.setText(arrayList.get(i).getFriendly_name());
            imageView1.setImageBitmap(AppController.getThumbnail(arrayList.get(i).getImage_resource()));
            imageView1.setContentDescription(arrayList.get(i).getTechnical_name());
            imageView1.setOnClickListener(customOnClickListener);
            i += 1;

            textView2 = (TextView) to_add.findViewById(R.id.category_text_view_two);
            imageView2 = (ImageView) to_add.findViewById(R.id.category_image_view_two);
            textView2.setText(arrayList.get(i).getFriendly_name());
            imageView2.setImageBitmap(AppController.getThumbnail(arrayList.get(i).getImage_resource()));
            imageView2.setContentDescription(arrayList.get(i).getTechnical_name());
            imageView2.setOnClickListener(customOnClickListener);
            i += 1;


            parent.addView(to_add);
            //Log.e("PopulateView", "View Populated with category");
        }
    }

    private ArrayList<Category> prepareCategories(){
        ArrayList<Category> arrayList = new ArrayList<>();
        String[] categoriesName = Category.getTechnical_name_list();
        String[] categoriesFriendlyName = Category.getFriendly_name_list();
        for(int i = 0; i < categoriesName.length; i++ ){
            int imageResourceId = getResources().getIdentifier(categoriesName[i],"drawable", getPackageName());
            Category category = new Category(imageResourceId, categoriesFriendlyName[i], categoriesName[i]);
            //Log.e("CategorySummary", "Category FName : " + categoriesFriendlyName[i] + " CategoryName : " + categoriesName[i] + " CategoryImageId : " + imageResourceId);
            arrayList.add(category);
        }
        return arrayList;
    }

    private void createSpaceForAd(){
        LinearLayout main_screen = (LinearLayout) findViewById(R.id.main_screen_linearlayout);
        LinearLayout add_screen = (LinearLayout) findViewById(R.id.main_screen_ad_layout);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) main_screen.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) add_screen.getLayoutParams();

        params1.weight = 7f;
        params2.weight = 1.5f;

        main_screen.setLayoutParams(params1);
        add_screen.setLayoutParams(params2);
    }

    private void intialiseAdmobFeatures()
    {
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.appId));

        AdView mAdView = (AdView) findViewById(R.id.ad_banner_home);
        AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                createSpaceForAd();
            }
        });
    }

    protected class CustomOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ImageView view = (ImageView) v;
            String category_name = view.getContentDescription().toString();
            Intent intent = new Intent(MainActivity.this, Instructions.class);
            intent.putExtra(AppConstants.FLAG_CATEGORY, category_name);
            startActivity(intent);
        }
    }
}

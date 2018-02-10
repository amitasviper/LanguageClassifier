package com.appradar.viper.amazingfacts;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by viper on 8/5/15.
 */
public class ManageAds {
    private static InterstitialAd interstitialAd;
    private static AdRequest.Builder builder;

    public static void prepareAds(Context context){
        if(interstitialAd == null) {
            interstitialAd = new InterstitialAd(context);
            String id = context.getResources().getString(R.string.interstetialAdId);
            interstitialAd.setAdUnitId(id);
        }
        refreshAd();
    }

    public static void refreshAd(){
        builder = new AdRequest.Builder();
        if(ShowFactActivity.DEBUG)
            builder.addTestDevice("AD618B7A612CDAC611081C8F115FF919");
        interstitialAd.loadAd(builder.build());
    }

    public static InterstitialAd getInterstitialAd(){
        Log.d("ManageAds", "Request for an ad :getInterstitialAd:");
        return interstitialAd;
    }
}

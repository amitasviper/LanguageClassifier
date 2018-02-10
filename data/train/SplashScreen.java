package com.appradar.viper.amazingfacts;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import com.appradar.viper.amazingfacts.databinding.ActivitySplashScreenBinding;;


public class SplashScreen extends Activity {

    private final Handler waitHandler = new Handler();
    private final Runnable waitCallback = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashScreen.this, Language.class);
            startActivity(intent);
            finish();
        }
    };

    private ActivitySplashScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        //Fake wait 2s to simulate some initialization on cold start (never do this in production!)
        waitHandler.postDelayed(waitCallback, 2000);
    }

    @Override
    protected void onDestroy() {
        waitHandler.removeCallbacks(waitCallback);
        super.onDestroy();
    }

//    ImageView logo;
//    AnimationHandler animationHandler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//
//        logo = (ImageView) findViewById(R.id.logo);
//
//        AlarmReceiver.setNotifications(this);
//
//        animationHandler = new AnimationHandler(this);
//        animationHandler.StartAnimation(logo);
//
//        LanguageActivity thread = new LanguageActivity();
//        thread.start();
//    }
//
//    public class AnimationHandler implements Animation.AnimationListener{
//
//        private Context context;
//
//        public AnimationHandler(Context context){
//            this.context = context;
//        }
//
//        public void StartAnimation(View view){
//
//            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
//            Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
//
//            view.clearAnimation();
//
//            view.startAnimation(fadeIn);
//            view.startAnimation(fadeOut);
//
//            fadeOut.setAnimationListener(this);
//        }
//
//        @Override
//        public void onAnimationStart(Animation animation) {
//
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//
//        }
//    }
//
//    public void StartActivity(){
//        Intent intent = new Intent(this, Language.class);
//        startActivity(intent);
//        finish();
//    }
//
//
//    private class LanguageActivity extends Thread{
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(3000);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        logo.clearAnimation();
//                    }
//                });
//                StartActivity();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

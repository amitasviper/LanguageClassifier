package com.appradar.wrestlingquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import su.levenetc.android.textsurface.TextSurface;
import utils.CookieThumperSample;

public class SplashScreen extends BaseAppActivity
{
    private TextSurface textSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textSurface = (TextSurface) findViewById(R.id.text_surface);

        Process_and_start();

        show(new String[]{"Welcome", "App", "Radar","Wrestling", "Quiz"});
    }

    private void LaunchMainActivity()
    {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void Process_and_start()
    {
        if (false)
        {
            //do some processing
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        Thread.sleep(5000);
                        LaunchMainActivity();
                    }
                    catch (Exception e)
                    {
                        Log.e("SplashScreen", "Exception in thread.sleep() "+ e.toString());
                    }
                }
            }).start();
        }
    }

    private void show(String[] array) {
        textSurface.reset();
        CookieThumperSample.play(textSurface, array, getAssets());
    }
}

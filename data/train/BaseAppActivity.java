package com.appradar.wrestlingquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by viper on 03/05/16.
 */
public abstract class BaseAppActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}

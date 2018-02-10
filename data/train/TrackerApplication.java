package utility;

import android.app.Application;

import com.appradar.viper.amazingfacts.R;
import com.google.android.gms.analytics.GoogleAnalytics;

/**
 * Created by viper on 04/09/15.
 */
public class TrackerApplication extends Application {
    private com.google.android.gms.analytics.Tracker myTracker;

    synchronized public com.google.android.gms.analytics.Tracker getDefaultTracker(){

        if(myTracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            myTracker = analytics.newTracker(R.xml.global_tracker);
        }

        return myTracker;

    }
}

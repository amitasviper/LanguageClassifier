package com.appradar.viper.amazingfacts;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by viper on 25/6/15.
 */
public class NotificationID {
    private static AtomicInteger id = new AtomicInteger(0);
    public static int getID(){
        return  id.incrementAndGet();
    }
}

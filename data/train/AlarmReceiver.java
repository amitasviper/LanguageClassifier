package com.appradar.viper.amazingfacts;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by viper on 10/5/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    DatabaseHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", ":onReceive: called");
        helper = new DatabaseHelper(context);
        helper.openDatabase();

        Calendar now = GregorianCalendar.getInstance();
        int dayOfWeek = now.get(Calendar.DATE);

        String saved_language = getNotificationLanguage(context);

        if (saved_language.equalsIgnoreCase("russian")){
            saved_language = "3";
        }
        else if(saved_language.equalsIgnoreCase("hindi")){
            saved_language = "2";
        }
        else{
            saved_language = "1";
        }

        Cursor cursor = helper.getRandomRow(saved_language);

        int _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FACT_ID)));
        String content = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FACT_CONTENT));
        String category_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FACT_CAT_ID));
        String language_id = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FACT_LAN_ID));

        cursor.close();
        helper.close();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(content);


        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        mBuilder.setAutoCancel(true);

        Intent jintent = new Intent(context, ShowFactActivity.class);
        jintent.putExtra("notification_flag", true);
        jintent.putExtra("fact_id", _id);
        jintent.putExtra("category_id", category_id);
        jintent.putExtra("language", language_id);

        PendingIntent pendingIntent =  PendingIntent.getActivity(context, 2, jintent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("notificationId");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
        //mNotificationManager.notify(2, mBuilder.build());

        AlarmReceiver.setNotifications(context);
    }


    private static int getNotificationCount(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.PREF_FILE, Context.MODE_PRIVATE);
        return preferences.getInt(ApplicationConstants.NOTI_COUNT_TAG, 5);
    }

    private static String getNotificationLanguage(Context context){
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.PREF_FILE, Context.MODE_PRIVATE);
        return preferences.getString(ApplicationConstants.NOTI_LAN_TAG, "english");
    }


    public static void setNotifications(Context context) {


        int notification_count  = getNotificationCount(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if(AlarmReceiver.getNotificationCount(context) > notification_count)
            return;

        for(int i = 1 ; i <= notification_count; i++){
            int hour = 5 + i;
            int notification_id = NotificationID.getID();

            Log.e("handleReceiver", ":start: called Notification count :"+ notification_count +" Current i:"+ i );
            Intent alarmIntent1 = new Intent(context, AlarmReceiver.class);
            alarmIntent1.putExtra("notificationId", notification_id);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, notification_id, alarmIntent1, PendingIntent.FLAG_UPDATE_CURRENT);


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);

            long timeToAlarm = calendar.getTimeInMillis();

            if(timeToAlarm <System.currentTimeMillis())
                continue;

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeToAlarm, pendingIntent1);
        }
    }
}
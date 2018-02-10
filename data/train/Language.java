package com.appradar.viper.amazingfacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kontext.Kontext;
import com.kontext.OSNotification;
import com.kontext.OSNotificationOpenResult;

import utility.TrackerApplication;


public class Language extends Activity {

    Button hindi, english, russian, settings;
//    ProgressBar dialog;
    ProgressDialog dialog;
    private static Context context;

    static boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setTitle(R.id.title);

        context = getApplicationContext();

//        Kontext.setLogLevel(Kontext.LOG_LEVEL.VERBOSE, Kontext.LOG_LEVEL.NONE);
        Kontext.startInit(this)
                .autoPromptLocation(true)
                .setNotificationOpenedHandler(new Language.NotificationOpenedHandler())
                .setNotificationReceivedHandler(new Language.NotificationReceivedHandler())
                .init();

        Kontext.setInFocusDisplaying(Kontext.OSInFocusDisplayOption.Notification);

        hindi = (Button) findViewById(R.id.hindi);
        english = (Button) findViewById(R.id.english);
        russian = (Button) findViewById(R.id.russian);
        settings = (Button) findViewById(R.id.my_settings);

        CustomOnClickListener customOnClickListener = new CustomOnClickListener();

        english.setOnClickListener(customOnClickListener);
        hindi.setOnClickListener(customOnClickListener);
        russian.setOnClickListener(customOnClickListener);
        settings.setOnClickListener(customOnClickListener);

        startTracker();

        AppRater.showRateDialogIfNeeded(this);
    }

    private void startTracker(){
        TrackerApplication trackerApplication = (TrackerApplication) getApplication();
        Tracker myTracker = trackerApplication.getDefaultTracker();

        myTracker.setScreenName(this.getClass().toString());
        myTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static Context getContext(){
        return context;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(dialog != null){
            dialog.dismiss();
        }
    }

    private void displayProgressBar(){
        dialog = new ProgressDialog(Language.this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void Exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to exit ?");
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }


    @Override
    public void onBackPressed() {
        Exit();
    }

    private class CustomOnClickListener implements View.OnClickListener{

        private void setNotificationPreferences(String notification_language, int notification_count){
            SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.PREF_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(ApplicationConstants.NOTI_LAN_TAG,notification_language );
            editor.putInt(ApplicationConstants.NOTI_COUNT_TAG, notification_count);

            editor.commit();
        }

        private int getNotificationCount(){
            SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.PREF_FILE, Context.MODE_PRIVATE);
            return preferences.getInt(ApplicationConstants.NOTI_COUNT_TAG, 5);
        }

        private String getNotificationLanguage(){
            SharedPreferences preferences = context.getSharedPreferences(ApplicationConstants.PREF_FILE, Context.MODE_PRIVATE);
            return preferences.getString(ApplicationConstants.NOTI_LAN_TAG, "english");
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.my_settings){
                final Dialog settings_dialog = new Dialog(Language.this);
                settings_dialog.setContentView(R.layout.settings_layout);
                settings_dialog.setTitle("Notification Settings");


                final NumberPicker picker = (NumberPicker) settings_dialog.findViewById(R.id.notification_count);
                picker.setMinValue(1);
                picker.setMaxValue(15);
                picker.setValue(getNotificationCount());

                RadioButton radio_english = (RadioButton) settings_dialog.findViewById(R.id.notification_english);
                RadioButton radio_hindi = (RadioButton) settings_dialog.findViewById(R.id.notification_hindi);
                RadioButton radio_russian = (RadioButton) settings_dialog.findViewById(R.id.notification_russian);

                String saved_language = getNotificationLanguage();
                if (saved_language.equalsIgnoreCase("russian")){
                    radio_russian.setChecked(true);
                }
                else if(saved_language.equalsIgnoreCase("hindi")){
                    radio_hindi.setChecked(true);
                }
                else{
                    radio_english.setChecked(true);
                }

                Button save = (Button) settings_dialog.findViewById(R.id.settings_save);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioGroup radioGroup = (RadioGroup) settings_dialog.findViewById(R.id.notification_language_group);
                        int radio_id = radioGroup.getCheckedRadioButtonId();
                        int notification_count = picker.getValue();

                        String selected_language = "";
                        switch (radio_id) {
                            case R.id.notification_english:
                                selected_language = "english";
                                break;
                            case R.id.notification_hindi:
                                selected_language = "hindi";
                                break;
                            case R.id.notification_russian:
                                selected_language = "russian";
                                break;
                        }
                        setNotificationPreferences(selected_language, notification_count);
                        AlarmReceiver.setNotifications(context);
                        settings_dialog.dismiss();
                        //Toast.makeText(Language.this, "Language : " + selected_language + " Notification Count : " + notification_count,Toast.LENGTH_LONG ).show();
                    }
                });

                Button cancel = (Button) settings_dialog.findViewById(R.id.cancel_settings);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        settings_dialog.dismiss();
                    }
                });

                settings_dialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = settings_dialog.getWindow();
                lp.copyFrom(window.getAttributes());

                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

                return;
            }

            displayProgressBar();

            Intent intent = new Intent(Language.this, CategoryList.class);
            if(v.getId() == R.id.hindi){
                intent.putExtra("language", DatabaseHelper.HINDI);
                Kontext.sendTag("lang", "hi");
            }
            else if(v.getId() == R.id.english){
                intent.putExtra("language", DatabaseHelper.ENGLISH);
                Kontext.sendTag("lang", "en");
            }
            else if(v.getId() == R.id.russian){
                intent.putExtra("language", DatabaseHelper.RUSSIAN);
                Kontext.sendTag("lang", "ru");
            }

            startActivity(intent);
        }
    }

    private class NotificationReceivedHandler implements Kontext.NotificationReceivedHandler {
        /**
         * Callback to implement in your app to handle when a notification is received while your app running
         *  in the foreground or background.
         *
         *  Use a NotificationExtenderService instead to receive an event even when your app is closed (not 'forced stopped')
         *     or to override notification properties.
         *
         * @param notification Contains information about the notification received.
         */
        @Override
        public void notificationReceived(OSNotification notification) {
            Log.w("Amazing Facts", "notificationReceived!!!!!!");
        }
    }

    private class NotificationOpenedHandler implements Kontext.NotificationOpenedHandler {
        /**
         * Callback to implement in your app to handle when a notification is opened from the Android status bar or in app alert
         *
         * @param openedResult Contains information about the notification opened and the action taken on it.
         */
        @Override
        public void notificationOpened(OSNotificationOpenResult openedResult) {
            Log.w("Amazing Facts", "notificationOpened!!!!!!");
        }
    }


}

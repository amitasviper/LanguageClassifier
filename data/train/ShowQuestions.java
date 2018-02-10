package com.appradar.wrestlingquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;
import utils.AppConstants;
import utils.DatabaseHelper;

public class ShowQuestions extends BaseAppActivity {

    private InterstitialAd mInterstitialAd;

    private TextView tv_timer, tv_question;
    private RadioGroup radio_group;
    private RadioButton radio_option_a, radio_option_b, radio_option_c, radio_option_d;
    private Button btn_skip;
    FButton btn_submit;

    private CountDownTimer countDownTimer;
    private Cursor cursor;

    private String CATEGORY, CORRECT_ANSWER;
    private ArrayList<String> questions, answers, user_answers;
    private int MAX_TIME = 30 * 1000;

    private int SCORE = 0;

    boolean INTERUPT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);

        Intent recv_intent = getIntent();
        CATEGORY = recv_intent.getStringExtra(AppConstants.FLAG_CATEGORY);
        Log.e("ShowQuestions", "Category Received is " + CATEGORY);
        LinkViews();

        createInterstitialAd();

    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel the current quiz session")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        INTERUPT = true;
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        else
                        {
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void createInterstitialAd()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstetialAdId));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (INTERUPT)
                {
                    finish();
                }
                else {
                    moveToNextActivity();
                }
            }
        });

        requestNewInterstetialAd();
    }

    private void requestNewInterstetialAd()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void LinkViews()
    {
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_question = (TextView) findViewById(R.id.tv_question);


        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        radio_option_a = (RadioButton) findViewById(R.id.radio_option_a);
        radio_option_b = (RadioButton) findViewById(R.id.radio_option_b);
        radio_option_c = (RadioButton) findViewById(R.id.radio_option_c);
        radio_option_d = (RadioButton) findViewById(R.id.radio_option_d);

        btn_skip = (Button) findViewById(R.id.btn_skip);
        btn_submit = (FButton) findViewById(R.id.btn_submit);

        CustomOnClickListener customOnClickListener = new CustomOnClickListener();

        btn_submit.setOnClickListener(customOnClickListener);
        btn_skip.setOnClickListener(customOnClickListener);

        FetchAndPlaceQuestions();

        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(MAX_TIME, 1000);
        customCountDownTimer.start();

    }

    private void FetchAndPlaceQuestions()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(ShowQuestions.this);
        databaseHelper.openDatabase();
        cursor = databaseHelper.fetchQuestionsInCategory(CATEGORY);
        databaseHelper.close();

        questions = new ArrayList<String>();
        answers = new ArrayList<String>();
        user_answers = new ArrayList<String>();

        setQuizTime();

        PlaceNextQuestion(true);
    }

    private boolean PlaceNextQuestion(boolean updateGui)
    {
        if (cursor.moveToNext() == false)
        {
            return false;
        }

        String question = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_QUESTION));
        String option_a = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_OPTION_A));
        String option_b = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_OPTION_B));
        String option_c = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_OPTION_C));
        String option_d = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_OPTION_D));
        CORRECT_ANSWER = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ANSWER));

        questions.add(question);
        answers.add(CORRECT_ANSWER);

        if (updateGui) {
            radio_group.clearCheck();
            tv_question.setText(question);
            radio_option_a.setText(option_a);
            radio_option_b.setText(option_b);
            radio_option_c.setText(option_c);
            radio_option_d.setText(option_d);
        }
        else {
            user_answers.add("Not Attempted");
        }

        return true;
    }

    private void UpdateUserResponseList(boolean skip, String user_response)
    {
        String response;
        if (skip)
        {
            response = "Not Attempted";
        }
        else
        {
            response = user_response;
        }
        user_answers.add(response);
    }

    public boolean ValidateResponse(String user_answer)
    {
        if (user_answer.equalsIgnoreCase(CORRECT_ANSWER))
            return true;
        return false;
    }

    private String UpdateScore()
    {
        String user_response = "-1";


        RadioButton radioButton = (RadioButton) findViewById(radio_group.getCheckedRadioButtonId());

        if (radioButton != null)
        {
            user_response = radioButton.getText().toString();
            if (ValidateResponse(user_response))
            {
                SCORE += 4;
            }

            else
            {
                SCORE -= 1;
            }
        }
        return user_response;
    }

    private void DisplayScoreCard(boolean timeout)
    {
        user_answers.add("Not Attempted");

        while (PlaceNextQuestion(false))
        {
            Log.d("ShowQuestions", "Adding non attempted questions to the list");
        }

        if (countDownTimer != null && !timeout)
            countDownTimer.cancel();

        if (SCORE > 0)
        {
            int time_left = 0;
            try {
                time_left = Integer.parseInt(tv_timer.getText().toString().replace(" sec", ""));
            }
            catch (NumberFormatException e)
            {
                Log.e("DisplayScoreCard", "Unable to parse time from tv_timer");
            }
            time_left /= 10;
            SCORE += time_left;
        }

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else {
            moveToNextActivity();
        }

    }

    private void moveToNextActivity()
    {
        Intent intent = new Intent(ShowQuestions.this, ScoreCard.class);
        intent.putExtra(AppConstants.FLAG_SCORE, SCORE);
        intent.putExtra(AppConstants.FLAG_CATEGORY, CATEGORY);
        intent.putStringArrayListExtra(AppConstants.FLAG_QUESTIONS, questions);
        intent.putStringArrayListExtra(AppConstants.FLAG_ANSWERS, answers);
        intent.putStringArrayListExtra(AppConstants.FLAG_USER_ANSWERS, user_answers);
        startActivity(intent);
        finish();
    }

    private void setQuizTime()
    {
        int question_count = cursor.getCount();
        MAX_TIME = question_count * 30 * 1000;
    }

    private class CustomCountDownTimer extends CountDownTimer
    {
        public CustomCountDownTimer(int total_time_in_millis, int tick_time_in_millis)
        {
            super(total_time_in_millis, tick_time_in_millis);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            long currentTime = millisUntilFinished / 1000;
            tv_timer.setText(String.valueOf(currentTime) + " " + "sec");
            //Log.e("onTick", "Value : " + String.valueOf(currentTime));
        }

        @Override
        public void onFinish()
        {
            try
            {
                TimeUp();
            }
            catch (Exception e)
            {
                Log.e("ShowQuestions", "onFinish: Exception: " + e.toString());
            }

        }

        // This is called even if the CountdownTimer is explicitely cancelled. Need a FIX
        public void TimeUp()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowQuestions.this);
            builder.setMessage("TIMEOUT!").setCancelable(
                    false).setPositiveButton("Show Score Card",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            {
                                DisplayScoreCard(true);
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private class CustomOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == btn_skip.getId())
            {
                UpdateUserResponseList(true, "");
                if (PlaceNextQuestion(true) == false)
                {
                    DisplayScoreCard(false);
                }

            }
            else if (v.getId() == btn_submit.getId())
            {
                String user_resp = UpdateScore();
                if (user_resp.equalsIgnoreCase("-1"))
                {
                    Toast.makeText(ShowQuestions.this, "Select atleast one option", Toast.LENGTH_SHORT).show();
                    return;
                }
                UpdateUserResponseList(false, user_resp);
                if (PlaceNextQuestion(true) == false)
                {
                    DisplayScoreCard(false);
                }
            }
            else
            {

            }
        }
    }
}

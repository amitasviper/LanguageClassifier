package com.appradar.wrestlingquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import utils.AppConstants;
import utils.AppController;
import utils.DefaultStorage;

public class ScoreCard extends BaseAppActivity {

    InterstitialAd mInterstitialAd;
    public int direction = 0;
    ImageView image_grade;
    TextView tv_grade, tv_score, tv_best_score;
    Button btn_home, btn_summary;

    private ArrayList<String> questions, answers, user_answers;

    String CATEGORY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card);

        LinkViews();
        UpdateScoreCard();

        createInterstitialAd();
    }

    private void createInterstitialAd()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstetialAdId));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (direction == 1)
                {
                    finish();
                }
                else
                {
                    moveToSummary();
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

    private void UpdateScoreCard()
    {
        Intent intent = ScoreCard.this.getIntent();

        int score = intent.getIntExtra(AppConstants.FLAG_SCORE, 0);
        CATEGORY = intent.getStringExtra(AppConstants.FLAG_CATEGORY);
        questions = intent.getStringArrayListExtra(AppConstants.FLAG_QUESTIONS);
        answers = intent.getStringArrayListExtra(AppConstants.FLAG_ANSWERS);
        user_answers = intent.getStringArrayListExtra(AppConstants.FLAG_USER_ANSWERS);

        image_grade.setImageResource(AppController.getImageResourceId(CATEGORY));

        int best_score = DefaultStorage.GetBestScore(CATEGORY);
        if (score > best_score)
        {
            DefaultStorage.SaveBestScore(CATEGORY, score);
            best_score = score;
        }

        String report = "";
        if (score == 60)
        {
            report = "PERFECT";
        }
        else if (score > 50)
        {
            report = "EXCELLENT";
        }
        else if (score > 40)
        {
            report = "GOOD";
        }
        else if (score > 30)
        {
            report = "AVERAGE";
        }
        else
        {
            report = "POOR";
        }
        tv_grade.setText(report);
        tv_score.setText("Your Score : " + score);
        tv_best_score.setText("Best Score : " + best_score);
    }

    private void LinkViews()
    {
        image_grade = (ImageView) findViewById(R.id.image_grade);

        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_best_score = (TextView) findViewById(R.id.tv_best_score);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_summary = (Button) findViewById(R.id.btn_summary);

        CustomOnClickListener customOnClickListener = new CustomOnClickListener();

        btn_home.setOnClickListener(customOnClickListener);
        btn_summary.setOnClickListener(customOnClickListener);
    }

    private void moveToSummary()
    {
        Intent intent = new Intent(ScoreCard.this, Summary.class);
        intent.putExtra(AppConstants.FLAG_CATEGORY, CATEGORY);
        intent.putStringArrayListExtra(AppConstants.FLAG_QUESTIONS, questions);
        intent.putStringArrayListExtra(AppConstants.FLAG_ANSWERS, answers);
        intent.putStringArrayListExtra(AppConstants.FLAG_USER_ANSWERS, user_answers);
        startActivity(intent);
        finish();
    }

    private class CustomOnClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if (v.getId() == btn_home.getId())
            {
                direction = 1;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    finish();
                }
            }
            else if (v.getId() == btn_summary.getId())
            {
                direction = 2;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    moveToSummary();
                }

            }
        }
    }
}

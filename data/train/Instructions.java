package com.appradar.wrestlingquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import utils.AppConstants;

public class Instructions extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        Button btn_start_quiz = (Button) findViewById(R.id.btn_start_quiz);
        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartQuizActivity();
            }
        });
    }

    private void StartQuizActivity()
    {
        Intent recv_intent = Instructions.this.getIntent();
        String category = recv_intent.getStringExtra(AppConstants.FLAG_CATEGORY);

        Log.e("Instructions", "Category received from intents : " + category);

        Intent intent = new Intent(Instructions.this, ShowQuestions.class);
        intent.putExtra(AppConstants.FLAG_CATEGORY, category);
        startActivity(intent);
        finish();
    }
}

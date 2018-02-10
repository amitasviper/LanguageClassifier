package com.appradar.wrestlingquiz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.util.ArrayList;

import utils.AppConstants;
import utils.DatabaseHelper;

public class Summary extends BaseAppActivity {

    TextView description;
    private ArrayList<String> questions, answers, user_answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        description = (TextView) findViewById(R.id.tv_description);

        Intent intent = getIntent();
        String category = intent.getStringExtra(AppConstants.FLAG_CATEGORY);

        questions = intent.getStringArrayListExtra(AppConstants.FLAG_QUESTIONS);
        answers = intent.getStringArrayListExtra(AppConstants.FLAG_ANSWERS);
        user_answers = intent.getStringArrayListExtra(AppConstants.FLAG_USER_ANSWERS);

        DatabaseHelper databaseHelper = new DatabaseHelper(Summary.this);
        databaseHelper.openDatabase();
        Cursor cursor = databaseHelper.fetchDescriptionInCategory(category);
        databaseHelper.close();

        String[] description_txt = new String[questions.size()];
        int i = 0;
        while (cursor.moveToNext())
        {
            description_txt[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESCRIPTION));
            i += 1;
        }

        String formattedText = getFormatedText(questions, answers, user_answers, description_txt);

        description.setText(Html.fromHtml(formattedText));
        description.setMovementMethod(new ScrollingMovementMethod());

    }

    private String getFormatedText(ArrayList<String> questions, ArrayList<String> answers, ArrayList<String> userAnswers, String[] description_txt){
        String text = "";

        for(int i = 0; i < questions.size(); i++){
            text += "<b>"+questions.get(i)+"</b> <br><br>";

            String ans = answers.get(i);

            text += "Correct Answer : "+ ans + "<br>";
            text += "Your Answer : ";

            String uans = userAnswers.get(i);
            String temp = "";
            if(ans.equalsIgnoreCase(uans)){
                temp = "<font color='#006400'>"+uans+ "</font><br><br>";
            }
            else {
                temp = "<font color='#FF0000'>"+uans+ "</font><br><br>";
            }

            text += temp;

            text += description_txt[i] + "<br><br><br>";

            text += "<br><br><br>";
        }

        return text;
    }
}

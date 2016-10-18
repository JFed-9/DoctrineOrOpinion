package com.xlr8.jackson.doctrineoropinion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Vector;

public class ResultsActivity extends AppCompatActivity {

    SharedPreferences prefs;

    TableLayout tableLayout;

    Vector<String> availableQuestions = new Vector<>();
    Vector<String> completedQuestions = new Vector<>();
    Vector<String> scores = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = ResultsActivity.this.getSharedPreferences("QuizData", Context.MODE_PRIVATE);

        tableLayout = (TableLayout) findViewById(R.id.results_table_layout);

        String available,myScores,completed;

        available = prefs.getString("AvailableQuestions", "");
        myScores = prefs.getString("Score", "");
        completed = prefs.getString("CompletedQuestions","");

//        Toast.makeText(ResultsActivity.this, available + "\n" + myScores, Toast.LENGTH_SHORT).show();

        Collections.addAll(availableQuestions,available.split("ß"));
        if (myScores.charAt(0) == 'ß')
            myScores = myScores.substring(1);
        Collections.addAll(scores,myScores.split("ß"));
        Collections.addAll(completedQuestions,completed.split("ß"));

        loadResults();
        prefs.edit().putString("AvailableQuestions","").apply();
        prefs.edit().putString("Score","").apply();
        prefs.edit().putString("CurrentQuestion","").apply();
        prefs.edit().putString("AllQuestions","").apply();
        prefs.edit().putString("CompletedQuestions","").apply();

    }

    private void loadResults() {
        int shortest = completedQuestions.size();
        if (shortest > scores.size())
            shortest = scores.size();
        if (scores.size() != completedQuestions.size())
            Toast.makeText(ResultsActivity.this,"Error in size of arrays\n" + completedQuestions + "\n" + scores,Toast.LENGTH_SHORT).show();
        for (int i = 0; i < shortest; i++)
        {
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView newText = (TextView)getLayoutInflater().inflate(R.layout.resultstitle, null);
            TextViewCompat.setTextAppearance(newText,R.style.ResultsQuestionTitle);
            newText.setText(completedQuestions.elementAt(i));

            TextView newText2 = (TextView)getLayoutInflater().inflate(R.layout.resultsscore, null);
            TextViewCompat.setTextAppearance(newText,R.style.ResultsQuestionScore);
            newText2.setText(scores.elementAt(i));

            newRow.addView(newText);
            newRow.addView(newText2);
            tableLayout.addView(newRow);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        prefs.edit().putString("AvailableQuestions","").apply();
        prefs.edit().putString("Score","").apply();
        prefs.edit().putString("CurrentQuestion","").apply();
        prefs.edit().putString("AllQuestions","").apply();
        prefs.edit().putString("CompletedQuestions","").apply();
        startActivity(new Intent(ResultsActivity.this, StartScreen.class));
    }

}

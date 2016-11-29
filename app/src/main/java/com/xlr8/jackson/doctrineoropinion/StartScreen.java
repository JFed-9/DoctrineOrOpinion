package com.xlr8.jackson.doctrineoropinion;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartScreen extends AppCompatActivity {

    Button addButton;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        prefs = StartScreen.this.getSharedPreferences("QuizData", Context.MODE_PRIVATE);
        Button btn = (Button) findViewById(R.id.start_begin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prefs.getString("CompletedQuestions","").equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartScreen.this);
                    builder.setTitle("Previous Quiz Found");
                    builder.setMessage("We've noticed you have already started a quiz. Would you like to continue or start a new quiz?")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(StartScreen.this, QuestionActivity.class));
                                }
                            })
                            .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    prefs.edit().putString("AvailableQuestions","").apply();
                                    prefs.edit().putString("Score","").apply();
                                    prefs.edit().putString("CurrentQuestion","").apply();
                                    prefs.edit().putString("AllQuestions","").apply();
                                    prefs.edit().putString("CompletedQuestions","").apply();
                                    startActivity(new Intent(StartScreen.this, QuestionActivity.class));
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    startActivity(new Intent(StartScreen.this, QuestionActivity.class));
                }
            }
        });
        addButton = (Button) findViewById(R.id.start_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartScreen.this, AddQuestion.class));
            }
        });
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

    }
}

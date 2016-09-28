package com.xlr8.jackson.doctrineoropinion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO Add a "Resume" option if they didn't finish a quiz (And a "Start new" maybe)  
        Button btn = (Button) findViewById(R.id.start_begin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Reset all questions and save data to User Preferences  
                startActivity(new Intent(StartScreen.this, QuestionActivity.class));
            }
        });
    }
}

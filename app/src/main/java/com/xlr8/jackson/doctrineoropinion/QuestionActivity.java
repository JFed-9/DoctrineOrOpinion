package com.xlr8.jackson.doctrineoropinion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class QuestionActivity extends AppCompatActivity {

    Button doctrine_button, opinion_button;
    TextView quote;

    Map<Integer,Question> allQuestions = new HashMap<>();

    Vector<Integer> availableQuestions = new Vector<>();
    Vector<String> availableQs = new Vector<>();
    Vector<Integer> completedQuestions = new Vector<>();
    Vector<String> completedQs = new Vector<>();
    Vector<String> scores = new Vector<>();

    Question currentQuestion = new Question();

    String available,myScores,currentQ,completed;

    DatabaseReference mDatabase;

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = QuestionActivity.this.getSharedPreferences("QuizData", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("Finished",false).apply();
//        if (prefs.getString("AvailableQuestions","").equals("")) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.addValueEventListener(getQuestions);
//        }

        doctrine_button = (Button)   findViewById(R.id.question_button_doctrine);
        opinion_button  = (Button)   findViewById(R.id.question_button_opinion);
        quote           = (TextView) findViewById(R.id.question_quote);
        doctrine_button.setOnClickListener(doctrine_choose);
        opinion_button.setOnClickListener(opinion_choose);





        available = prefs.getString("AvailableQuestions", "");
        myScores = prefs.getString("Score", "");
        currentQ = prefs.getString("CurrentQuestion","");
        completed = prefs.getString("CompletedQuestions","");



        if (available.contains("ß")) {
            Collections.addAll(availableQs, available.split("ß"));
            for (String s : availableQs) {
                availableQuestions.add(Integer.parseInt(s));
            }
        }
        if (myScores.contains("ß"))
            Collections.addAll(scores,myScores.split("ß"));
        if (completed.contains("ß")) {
            Collections.addAll(completedQs,completed.split("ß"));
            for (String s : completedQs) {
                completedQuestions.add(Integer.parseInt(s));
            }
        }
        currentQuestion = new Question(currentQ);
    }

    ValueEventListener getQuestions = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            Toast.makeText(QuestionActivity.this,"Found Questions!", Toast.LENGTH_SHORT).show();
            allQuestions.clear();
            if (!available.contains("ß"))
                availableQuestions.clear();

            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                for (DataSnapshot QuestionSnap : snap.getChildren()) {
                    Question addingQ = (Question)QuestionSnap.getValue(Question.class);

                    allQuestions.put(addingQ.getId(), addingQ);
                    if (!available.contains("ß") && addingQ.getApproved())
                        availableQuestions.add(addingQ.getId());
                }
            }
            if (!available.contains("ß"))
                Collections.shuffle(availableQuestions);

            if (quote.getText().equals("Loading..."))
            {
                if (availableQuestions.size() > completedQuestions.size())
                {
                    for (Integer i : availableQuestions)
                    {
                        currentQuestion = allQuestions.get(i);
                        if (!completedQuestions.contains(currentQuestion.getId()))
                            break;
                    }
                    quote.setText(currentQuestion.getDetails());
                } else {
                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("ERROR");
        }
    };

    View.OnClickListener doctrine_choose = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (quote.getText().equals("Loading..."))
            {
                return;
            }
            if (quote.getText().equals("No further questions found"))
            {
                startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
            } else {
                if (currentQuestion.isTrue()) {
                    scores.add("Correct");
//                    Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    scores.add("Incorrect");
//                    Toast.makeText(QuestionActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                }
                completedQuestions.add(currentQuestion.getId());

                quote.setText(R.string.Loading);

                Gson gson = new Gson();

                String available = "";
                for (Integer i : availableQuestions)
                {
                    available += i;
                    available += "ß";
                }

                available = available.substring(0,available.length()-1);

                String completed = "";
                for (Integer i : completedQuestions)
                {
                    completed += i;
                    completed += "ß";
                }

                completed = completed.substring(0,completed.length()-1);

                String myScores = "";
                for (String s : scores)
                {
                    myScores += s;
                    myScores += "ß";
                }
                myScores = myScores.substring(0,myScores.length()-1);

//                Toast.makeText(QuestionActivity.this, myScores, Toast.LENGTH_SHORT).show();

                prefs.edit().putString("AvailableQuestions", available).apply();
                prefs.edit().putString("AllQuestions", gson.toJson(allQuestions)).apply();
                prefs.edit().putString("CurrentQuestion", currentQuestion.toString()).apply();
                prefs.edit().putString("CompletedQuestions", completed).apply();
                prefs.edit().putString("Score", myScores).apply();

                if (availableQuestions.size() == completedQuestions.size() || completedQuestions.size() == 10)
                {
                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
                }
                if (availableQuestions.size() != completedQuestions.size() && completedQuestions.size() != 10)
                {
                    for (Integer i : availableQuestions)
                    {
                        currentQuestion = allQuestions.get(i);
                        if (!completedQuestions.contains(currentQuestion.getId()))
                            break;
                    }
                    quote.setText(currentQuestion.getDetails());
                }
            }
        }
    };

    View.OnClickListener opinion_choose = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (quote.getText().equals("Loading..."))
            {
                return;
            }
            if (quote.getText().equals("No further questions found"))
            {
                startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
            } else {
                if (!currentQuestion.isTrue()) {
                    scores.add("Correct");
//                    Toast.makeText(QuestionActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    scores.add("Incorrect");
//                    Toast.makeText(QuestionActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                }
                completedQuestions.add(currentQuestion.getId());

                quote.setText(R.string.Loading);

                Gson gson = new Gson();

                String available = "";
                for (Integer i : availableQuestions)
                {
                    available += i;
                    available += "ß";
                }

                available = available.substring(0,available.length()-1);

                String completed = "";
                for (Integer i : completedQuestions)
                {
                    completed += i;
                    completed += "ß";
                }

                completed = completed.substring(0,completed.length()-1);

                String myScores = "";
                for (String s : scores)
                {
                    myScores += s;
                    myScores += "ß";
                }
                myScores = myScores.substring(0,myScores.length()-1);

//                Toast.makeText(QuestionActivity.this, myScores, Toast.LENGTH_SHORT).show();
                prefs.edit().putString("AvailableQuestions", available).apply();
                prefs.edit().putString("AllQuestions", gson.toJson(allQuestions)).apply();
                prefs.edit().putString("CurrentQuestion", currentQuestion.toString()).apply();
                prefs.edit().putString("CompletedQuestions", completed).apply();
                prefs.edit().putString("Score", myScores).apply();

                if (availableQuestions.size() == completedQuestions.size() || completedQuestions.size() == 10)
                {
                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
                }
                if (availableQuestions.size() != completedQuestions.size() && completedQuestions.size() != 10)
                {
                    for (Integer i : availableQuestions)
                    {
                        currentQuestion = allQuestions.get(i);
                        if (!completedQuestions.contains(currentQuestion.getId()))
                            break;
                    }
                    quote.setText(currentQuestion.getDetails());
                }
            }
        }
    };
}

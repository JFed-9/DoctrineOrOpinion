package com.xlr8.jackson.doctrineoropinion;

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

    Map<String,Question> allQuestions = new HashMap<>();

    Vector<String> availableQuestions = new Vector<>();
    Vector<String> completedQuestions = new Vector<>();
    Vector<String> scores = new Vector<>();

    Question currentQuestion = new Question();

    DatabaseReference mDatabase;

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = QuestionActivity.this.getSharedPreferences("QuizData", Context.MODE_PRIVATE);

//        if (prefs.getString("AvailableQuestions","").equals("")) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.addValueEventListener(retrieveQuotes);
//        }

        doctrine_button = (Button)   findViewById(R.id.question_button_doctrine);
        opinion_button  = (Button)   findViewById(R.id.question_button_opinion);
        quote           = (TextView) findViewById(R.id.question_quote);
        doctrine_button.setOnClickListener(doctrine_choose);
        opinion_button.setOnClickListener(opinion_choose);



        String available,myScores,currentQ,completed;

        available = prefs.getString("AvailableQuestions", "");
        myScores = prefs.getString("Score", "");
        currentQ = prefs.getString("CurrentQuestion","");
        completed = prefs.getString("CompletedQuestions","");



        if (available.contains("ß"))
            Collections.addAll(availableQuestions,available.split("ß"));
        if (myScores.contains("ß"))
            Collections.addAll(scores,myScores.split("ß"));
        if (completed.contains("ß"))
            Collections.addAll(completedQuestions,completed.split("ß"));
        currentQuestion = new Question(currentQ);
    }

    ValueEventListener retrieveQuotes = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            Toast.makeText(QuestionActivity.this,"Found Questions!", Toast.LENGTH_SHORT).show();
            allQuestions.clear();
            availableQuestions.clear();

            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                for (DataSnapshot QuestionSnap : snap.getChildren()) {
                    Question addingQ = (Question)QuestionSnap.getValue(Question.class);

                    allQuestions.put(addingQ.getTitle(), addingQ);
                    availableQuestions.add(addingQ.getTitle());
                }
            }

            Collections.shuffle(availableQuestions);

            if (quote.getText().equals("Loading..."))
            {
                if (availableQuestions.size() > completedQuestions.size())
                {
                    for (String s : availableQuestions)
                    {
                        currentQuestion = allQuestions.get(s);
                        if (!completedQuestions.contains(currentQuestion.getTitle()))
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
                completedQuestions.add(currentQuestion.getTitle());

                quote.setText(R.string.Loading);

                Gson gson = new Gson();

                String available = "";
                for (String s : availableQuestions)
                {
                    available += s;
                    available += "ß";
                }

                available = available.substring(0,available.length()-1);

                String completed = "";
                for (String s : completedQuestions)
                {
                    completed += s;
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

                String allQs = "";
                for (String s : availableQuestions)
                {
                    Question q = allQuestions.get(s);
                    allQs += q.toString();
                    allQs += "ß";
                }
                myScores = myScores.substring(0,myScores.length()-1);

//                Toast.makeText(QuestionActivity.this, myScores, Toast.LENGTH_SHORT).show();

                prefs.edit().putString("AvailableQuestions", available).apply();
                prefs.edit().putString("AllQuestions", gson.toJson(allQuestions)).apply();
                prefs.edit().putString("CurrentQuestion", currentQuestion.toString()).apply();
                prefs.edit().putString("CompletedQuestions", completed).apply();
                prefs.edit().putString("Score", myScores).apply();

                if (availableQuestions.size() != completedQuestions.size())
                {
                    for (String s : availableQuestions)
                    {
                        currentQuestion = allQuestions.get(s);
                        if (!completedQuestions.contains(currentQuestion.getTitle()))
                            break;
                    }
                    quote.setText(currentQuestion.getDetails());
                }
                if (availableQuestions.size() == completedQuestions.size() || completedQuestions.size() == 20)
                {
                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
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
                completedQuestions.add(currentQuestion.getTitle());

                quote.setText(R.string.Loading);

                Gson gson = new Gson();

                String available = "";
                for (String s : availableQuestions)
                {
                    available += s;
                    available += "ß";
                }

                available = available.substring(0,available.length()-1);

                String completed = "";
                for (String s : completedQuestions)
                {
                    completed += s;
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

                if (availableQuestions.size() != completedQuestions.size())
                {
                    for (String s : availableQuestions)
                    {
                        currentQuestion = allQuestions.get(s);
                        if (!completedQuestions.contains(currentQuestion.getTitle()))
                            break;
                    }
                    quote.setText(currentQuestion.getDetails());
                }
                if (availableQuestions.size() == completedQuestions.size() || completedQuestions.size() == 20)
                {
                    startActivity(new Intent(QuestionActivity.this, ResultsActivity.class));
                }

            }
        }
    };
}

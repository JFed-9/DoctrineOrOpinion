package com.xlr8.jackson.doctrineoropinion;

import android.content.Context;
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

import java.util.Collections;
import java.util.HashMap;
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

    SharedPreferences prefs = QuestionActivity.this.getSharedPreferences("QuizData", Context.MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(retrieveQuotes);

        doctrine_button = (Button)   findViewById(R.id.question_button_doctrine);
        opinion_button  = (Button)   findViewById(R.id.question_button_opinion);
        quote           = (TextView) findViewById(R.id.question_quote);
        doctrine_button.setOnClickListener(doctrine_choose);
        opinion_button.setOnClickListener(opinion_choose);
//        for (int i = 0; i < availableQuestions.size(); i++)
//            if (!completedQuestions.contains(availableQuestions.elementAt(i))) {
//                quote.setText(allQuestions.get(availableQuestions.elementAt(i)).getQuote());
//                currentQuestion = allQuestions.get(availableQuestions.elementAt(i));
//                break;
//            }
    }

    ValueEventListener retrieveQuotes = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            Toast.makeText(QuestionActivity.this,"Found Questions!", Toast.LENGTH_SHORT).show();
            allQuestions.clear();
            availableQuestions.clear();

            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                for (DataSnapshot QuestionSnap : snap.getChildren()) {

                    // Get usable info from Data Snapshot
                    String title = (String)QuestionSnap.child("Title").getValue();
                    String quote = (String)QuestionSnap.child("Quote").getValue();
                    String source = (String)QuestionSnap.child("Source").getValue();
                    Boolean doctrine = (Boolean)QuestionSnap.child("Doctrine").getValue();
                    Double reason = (Double)QuestionSnap.child("Reason").getValue();

                    Question question = new Question(title,quote,doctrine,source,reason);

//                    Toast.makeText(QuestionActivity.this, question.getQuote() , Toast.LENGTH_SHORT).show();
                    allQuestions.put(question.getTitle(), question);
                    availableQuestions.add(question.getTitle());
                }
            }

            //Shuffle
            Collections.shuffle(availableQuestions);

            //TODO add functionality to change the "No Questions" error

            if (quote.getText().equals("Loading..."))
            {
                for (int i = 0; i < availableQuestions.size(); i++)
                    if (!completedQuestions.contains(availableQuestions.elementAt(i))) {
                        quote.setText(allQuestions.get(availableQuestions.elementAt(i)).getQuote());
                        currentQuestion = allQuestions.get(availableQuestions.elementAt(i));
                        break;
                    }
                if (quote.getText().equals("Loading..."))
                {
                    quote.setText(R.string.NoQuestionsFound);
                    Toast.makeText(QuestionActivity.this,R.string.NoQuestionsFound, Toast.LENGTH_SHORT).show();
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
            completedQuestions.add(currentQuestion.getTitle());
            quote.setText(R.string.Loading);
            for (int i = 0; i < availableQuestions.size(); i++)
                if (!completedQuestions.contains(availableQuestions.elementAt(i))) {
                    quote.setText(allQuestions.get(availableQuestions.elementAt(i)).getQuote());
                    currentQuestion = allQuestions.get(availableQuestions.elementAt(i));
                    break;
                }
            if (quote.getText().equals("Loading..."))
            {
                quote.setText(R.string.NoQuestionsFound);
                Toast.makeText(QuestionActivity.this,R.string.NoQuestionsFound, Toast.LENGTH_SHORT).show();
            }

            prefs.edit().putStringSet("AvailableQuestions", (Set<String>) availableQuestions).apply();
            prefs.edit().putStringSet("CompletedQuestions", (Set<String>) completedQuestions).apply();
            prefs.edit().putStringSet("AllQuestions",(Set<String>) allQuestions).apply();
            prefs.edit().putString("CurrentQuestion",currentQuestion.toString()).apply();
            prefs.edit().putStringSet("Score", (Set<String>) scores).apply();

        }
    };

    View.OnClickListener opinion_choose = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            completedQuestions.add(currentQuestion.getTitle());
            quote.setText(R.string.Loading);
            for (int i = 0; i < availableQuestions.size(); i++)
                if (!completedQuestions.contains(availableQuestions.elementAt(i))) {
                    quote.setText(allQuestions.get(availableQuestions.elementAt(i)).getQuote());
                    currentQuestion = allQuestions.get(availableQuestions.elementAt(i));
                    break;
                }
            if (quote.getText().equals("Loading..."))
            {
                quote.setText(R.string.NoQuestionsFound);
                Toast.makeText(QuestionActivity.this,R.string.NoQuestionsFound, Toast.LENGTH_SHORT).show();
            }

            prefs.edit().putStringSet("AvailableQuestions", (Set<String>) availableQuestions);
            prefs.edit().putStringSet("CompletedQuestions", (Set<String>) completedQuestions);
            prefs.edit().putStringSet("AllQuestions",(Set<String>) allQuestions);
            prefs.edit().putString("CurrentQuestion",currentQuestion.toString());
            prefs.edit().putStringSet("Score", (Set<String>) scores);

            prefs.edit().apply();
        }
    };
}

package com.xlr8.jackson.doctrineoropinion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AddQuestion extends AppCompatActivity {

    private DatabaseReference mDatabase;

    Map<Integer,Question> allQuestions = new HashMap<>();

    Vector<Integer> availableQuestions = new Vector<>();

    EditText title,details,source;
    CheckBox truth;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        title = (EditText)findViewById(R.id.add_title);
        details = (EditText)findViewById(R.id.add_details);
        source = (EditText)findViewById(R.id.add_source);
        truth = (CheckBox)findViewById(R.id.add_truth);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(getQuestions);

        submit = (Button) findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                for (int i = 0; i < availableQuestions.size(); i++)
                {
                    if (availableQuestions.elementAt(i) > id)
                        id = availableQuestions.elementAt(i);
                }
                id++;

                Question questionToAdd = new Question(id,title.getText().toString(),details.getText().toString(),truth.isChecked() ,source.getText().toString(),false);
//                Toast.makeText(AddQuestion.this,mDatabase.child("Quotes").toString(),Toast.LENGTH_SHORT).show();
                try {
                    mDatabase.child("Quotes").push().setValue(questionToAdd.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestion.this);
                            builder.setTitle("Question Added Successfully!");
                            builder.setMessage("Please wait at least 48 hours for your question to be approved.")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setCancelable(false);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    title.setText("");
                    details.setText("");
                    source.setText("");
                    truth.setChecked(false);
                } catch (RuntimeException e)
                {
                    System.out.println(e.toString());
                }
            }
        });

    }
    ValueEventListener getQuestions = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            Toast.makeText(QuestionActivity.this,"Found Questions!", Toast.LENGTH_SHORT).show();
            allQuestions.clear();
            availableQuestions.clear();

            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                for (DataSnapshot QuestionSnap : snap.getChildren()) {
                    Question addingQ = QuestionSnap.getValue(Question.class);

                    allQuestions.put(addingQ.getId(), addingQ);
                    availableQuestions.add(addingQ.getId());
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("ERROR");
        }
    };

}

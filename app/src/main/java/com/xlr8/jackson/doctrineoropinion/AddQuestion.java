package com.xlr8.jackson.doctrineoropinion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.InvocationTargetException;

public class AddQuestion extends AppCompatActivity {

    private DatabaseReference mDatabase;

    EditText title,details,source,explanation;
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
        explanation = (EditText)findViewById(R.id.add_explanation);
        if (explanation.getText().equals(""))
            explanation.setText("0.0");

        submit = (Button) findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question questionToAdd = new Question(title.getText().toString(),details.getText().toString(),truth.isChecked() ,source.getText().toString(),Double.parseDouble(explanation.getText().toString()));
//                Toast.makeText(AddQuestion.this,mDatabase.child("Quotes").toString(),Toast.LENGTH_SHORT).show();
                try {
                    mDatabase.child("Quotes").push().setValue(questionToAdd.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddQuestion.this,"Success",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (RuntimeException e)
                {
                    System.out.println(e.toString());
                }
            }
        });

    }

}

package com.xlr8.jackson.doctrineoropinion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ResultsActivity extends AppCompatActivity {

    SharedPreferences prefs;

    TableLayout tableLayout;

    Map<String,Question> allQuestions = new HashMap<>();

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

        String available,myScores,completed,all;

        available = prefs.getString("AvailableQuestions", "");
        myScores = prefs.getString("Score", "");
        completed = prefs.getString("CompletedQuestions","");
        all = prefs.getString("AllQuestions","");
//        Toast.makeText(ResultsActivity.this, available + "\n" + myScores, Toast.LENGTH_SHORT).show();

        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<Map<String, Question>>(){}.getType();
        allQuestions = gson.fromJson(all,type);

//        Toast.makeText(ResultsActivity.this,all,Toast.LENGTH_SHORT).show();
//        Toast.makeText(ResultsActivity.this,allQuestions.toString(),Toast.LENGTH_SHORT).show();
        Collections.addAll(availableQuestions,available.split("ß"));
        if (myScores.charAt(0) == 'ß')
            myScores = myScores.substring(1);
        Collections.addAll(scores,myScores.split("ß"));
        Collections.addAll(completedQuestions,completed.split("ß"));

        loadResults();

    }
    @SuppressWarnings("deprecation")
    private void loadResults() {
        int shortest = completedQuestions.size();
        if (shortest > scores.size())
            shortest = scores.size();
        if (scores.size() != completedQuestions.size())
            Toast.makeText(ResultsActivity.this,"Error in size of arrays\n" + completedQuestions + "\n" + scores,Toast.LENGTH_SHORT).show();
        for (int i = 0; i < shortest; i++)
        {
            TableRow newRow = new TableRow(this);
            if (scores.get(i).equals("Correc") || scores.get(i).equals("Incorrec")) {
                scores.set(i,scores.get(i) + "t");  // I have no idea why this happens, but it does, so I have to use this patch.
            }
            if (scores.get(i).equals("Correct"))
                newRow.setBackgroundColor(Color.parseColor("#22b20c"));
            else
                newRow.setBackgroundColor(Color.parseColor("#e52030"));
//            if (i%2==0)
//            {
//                if (scores.get(i).equals("Correct"))
//                    newRow.setBackgroundColor(Color.parseColor("#159102"));
//                else
//                    newRow.setBackgroundColor(Color.parseColor("#ba0716"));
//            }
            newRow.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.FILL_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            final TextView newText = new TextView(ResultsActivity.this);
            newText.setPadding(15, 15, 15, 15);
            newText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            newText.setText(completedQuestions.elementAt(i));

            TextView newText2 = new TextView(ResultsActivity.this);
            newText2.setPadding(15, 15, 15, 15);
            newText2.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            newText2.setText(scores.elementAt(i));

//            TextView newText3 = new TextView(ResultsActivity.this);
//            newText3.setPadding(15, 15, 15, 15);
//            newText3.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
//            newText3.setText("Source");
//            newText3.setTextColor(Color.parseColor("#2211ff"));
//            newText3.setTextIsSelectable(true);

            Button newButton = new Button(ResultsActivity.this);
            newButton.setText("Source");
            newButton.setTextColor(Color.parseColor("#2211ff"));
            newButton.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            newButton.setPadding(15, 15, 15, 15);
            newButton.setBackgroundColor(Color.TRANSPARENT);
            newButton.setPaintFlags(newButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url;
                    url = allQuestions.get((String) newText.getText()).getSource();
//                    Toast.makeText(ResultsActivity.this,url,Toast.LENGTH_SHORT).show();
                    try {
                        URL myurl = new URL(url);
                    } catch (MalformedURLException e) {
                        url = "http://google.com";
                    }
                    Uri myUri = Uri.parse(url);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, myUri);
                    startActivity(browserIntent);
                }
            });

            View v = new View(this);
            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
            v.setBackgroundColor(Color.parseColor("#ffffff"));

            newRow.addView(newText);
            newRow.addView(newText2);
            newRow.addView(newButton);

            tableLayout.addView(newRow);
            if (i != shortest-1)
                tableLayout.addView(v);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
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

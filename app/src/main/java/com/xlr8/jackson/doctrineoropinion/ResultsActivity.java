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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import static android.R.attr.button;

public class ResultsActivity extends AppCompatActivity {

    SharedPreferences prefs;

    TableLayout content;
    TableLayout header;

    Map<Integer,Question> allQuestions = new HashMap<>();

    Vector<Integer> availableQuestions = new Vector<>();
    Vector<Integer> completedQuestions = new Vector<>();
    Vector<String> availableQs = new Vector<>();
    Vector<String> completedQs = new Vector<>();
    Vector<String> scores = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = ResultsActivity.this.getSharedPreferences("QuizData", Context.MODE_PRIVATE);

        content = (TableLayout) findViewById(R.id.results_table_layout);
        header = (TableLayout) findViewById(R.id.results_header);

        String available,myScores,completed,all;

        available = prefs.getString("AvailableQuestions", "");
        myScores = prefs.getString("Score", "");
        completed = prefs.getString("CompletedQuestions","");
        all = prefs.getString("AllQuestions","");
        prefs.edit().putBoolean("Finished",true).apply();
//        Toast.makeText(ResultsActivity.this, available + "\n" + myScores, Toast.LENGTH_SHORT).show();

        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<Map<Integer, Question>>(){}.getType();
        allQuestions = gson.fromJson(all,type);

//        Toast.makeText(ResultsActivity.this,all,Toast.LENGTH_SHORT).show();
//        Toast.makeText(ResultsActivity.this,allQuestions.toString(),Toast.LENGTH_SHORT).show();
        Collections.addAll(availableQs,available.split("ß"));
        for (String s : availableQs) {
            availableQuestions.add(Integer.parseInt(s));
        }
        if (myScores.charAt(0) == 'ß')
            myScores = myScores.substring(1);
        Collections.addAll(scores,myScores.split("ß"));
        Collections.addAll(completedQs,completed.split("ß"));
        for (String s : completedQs) {
            completedQuestions.add(Integer.parseInt(s));
        }
        loadResults();

    }

    @SuppressWarnings("deprecation")
    private void loadResults() {
        int shortest = completedQuestions.size();
        if (shortest > scores.size())
            shortest = scores.size();
        if (scores.size() != completedQuestions.size())
            Toast.makeText(ResultsActivity.this,"Error in size of arrays\n" + completedQuestions + "\n" + scores,Toast.LENGTH_SHORT).show();

        TableRow Header = new TableRow(this);
        Header.setBackgroundColor(Color.parseColor("#aaaaaa"));
        Header.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.FILL_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        Header.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView Header1 = new TextView(ResultsActivity.this);
        Header1.setPadding(15, 10, 15, 10);
        Header1.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Header1.setText("Question");
        Header1.setGravity(Gravity.CENTER);

        TextView Header2 = new TextView(ResultsActivity.this);
        Header2.setPadding(15, 10, 0, 10);
        Header2.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Header2.setText("Result");
        Header2.setGravity(Gravity.RIGHT);

        TextView Header3 = new TextView(ResultsActivity.this);
        Header3.setPadding(0, 10, 15, 10);
        Header3.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        Header3.setText("Source");
        Header3.setGravity(Gravity.RIGHT);

        View Separator = new View(this);
        Separator.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
        Separator.setBackgroundColor(Color.parseColor("#ffffff"));

        Header.addView(Header1);
        Header.addView(Header2);
        Header.addView(Header3);

        header.addView(Header);
        header.addView(Separator);

        for (int i = 0; i < shortest; i++)
        {
            TableRow newRow = new TableRow(this);
//            if (scores.get(i).equals("Correc") || scores.get(i).equals("Incorrec")) {
//                scores.set(i,scores.get(i) + "t");  // I have no idea why this happens, but it does, so I have to use this patch.
//            }
            if (scores.get(i).equals("Correct"))
                newRow.setBackgroundColor(Color.parseColor("#22b20c"));
            else
                newRow.setBackgroundColor(Color.parseColor("#e52030"));
            newRow.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.FILL_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            final Integer currentQ = completedQuestions.elementAt(i);

            final TextView newText = new TextView(ResultsActivity.this);
            newText.setPadding(15,15,15,15);
            newText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            newText.setText(allQuestions.get(currentQ).getTitle());
            newText.setGravity(Gravity.LEFT);

            final TextView newText2 = new TextView(ResultsActivity.this);
            newText2.setPadding(15,15,0,15);
            newText2.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            newText2.setText(scores.elementAt(i));
            newText2.setGravity(Gravity.RIGHT);

            final Button newButton = new Button(ResultsActivity.this);
            newButton.setText("Source");
            newButton.setTextColor(Color.parseColor("#2211ff"));
            newButton.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            newButton.setPadding(0,15,15,15);
            newButton.setBackgroundColor(Color.TRANSPARENT);
            newButton.setPaintFlags(newButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            newButton.setGravity(Gravity.RIGHT);

            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url;

                    url = allQuestions.get(currentQ).getSource();
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

            newRow.addView(newText);
            newRow.addView(newText2);
            newRow.addView(newButton);

            View sep = new View(this);
            sep.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
            sep.setBackgroundColor(Color.parseColor("#ffffff"));

            content.addView(newRow);
            if (i != shortest-1)
                content.addView(sep);
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
        prefs.edit().putBoolean("Finished",false).apply();
        startActivity(new Intent(ResultsActivity.this, StartScreen.class));
    }

}

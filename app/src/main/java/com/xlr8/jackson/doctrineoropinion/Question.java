package com.xlr8.jackson.doctrineoropinion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jackson on 9/27/16.
 */

public class Question {
    String details,title;
    Boolean isTrue;
    String source;
    double explanation;

    Question(String t, String q, Boolean d, String s, double r) {
        details = q;
        title = t;
        isTrue = d;
        source = s;
        explanation = r;
    }
    Question() {
        details = "Blank";
        title = "Blank";
        isTrue = true;
        source = "http://google.com";
        explanation = 0.0;
    }
    Question(String rawData) {
        int count = 0;
        for (int i = 0; i < rawData.length(); i++)
        {
            if (rawData.charAt(i) == 'ß')
            {
                count++;
            }
        }
        if (count != 5)
        {
            details = "Blank";
            title = "Blank";
            isTrue = true;
            source = "http://google.com";
            explanation = 0.0;
        } else {
            String[] values = rawData.split("ß");
            title = values[0];
            details = values[1];
            isTrue = false;
            if (values[2].equals("T"))
                isTrue = true;
            source = values[3];
            explanation = Double.parseDouble(values[4]);
        }
    }

    String getDetails() { return details; }
    String getTitle() { return title; }
    Boolean isTrue() { return isTrue; }
    String getSource() { return source; }
    double getExplanation() { return explanation; }

    public void setDetails(String q) { details = q; }
    public void setTitle(String t) { title = t; }
    public void setTrue(Boolean i) { isTrue = i; }
    public void setSource(String s) { source = s; }
    public void setExplanation(Double r) { explanation = r; }

    public String toString() {
        String s = title + "ß" + details + "ß";
        if (isTrue)
            s += "T";
        else
            s += "F";
        s += "ß";
        s += source + "ß" + Double.toString(explanation);
        return s;
    }

    public HashMap toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("details", details);
        map.put("title", title);
        map.put("isTrue", isTrue);
        map.put("source", source);
        map.put("explanation", explanation);
        return map;
    }
}

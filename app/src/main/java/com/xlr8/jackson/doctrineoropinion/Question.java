package com.xlr8.jackson.doctrineoropinion;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jackson on 9/27/16.
 */

public class Question {
    String quote,title;
    Boolean isDoctrine;
    URL source;
    double reason;

    String defaultSource = "http://google,com";

    Question(String t, String q, Boolean d, String s, double r) {
        quote = q;
        title = t;
        isDoctrine = d;
        try {
            source = new URL(s);
        } catch (MalformedURLException e)
        {
            System.out.println("Failure in converting url: " + s);
            try {
                source = new URL(defaultSource);
            } catch (MalformedURLException f) {
                //Google didn't work either
            }
        }
        reason = r;
    }
    Question() {
        quote = "Blank";
        title = "Blank";
        isDoctrine = true;
        try {
            source = new URL("http://jacksonfeddock.com");
        } catch (MalformedURLException e)
        {
            System.out.println("Failure in converting url: http://jacksonfeddock.com");
            try {
                source = new URL(defaultSource);
            } catch (MalformedURLException f) {
                //Google didn't work either
            }
        }
        reason = 2.4;
    }
    Question(String rawData) {
        String[] values = rawData.split("ß");
        title = values[0];
        quote = values[1];
        isDoctrine = false;
        if (values[2].equals("T"))
            isDoctrine = true;
        try {
            source = new URL(values[3]);
        } catch (MalformedURLException e)
        {
            System.out.println("Failure in converting url: " + values[3]);
            try {
                source = new URL(defaultSource);
            } catch (MalformedURLException f) {
                //Google didn't work either
            }
        }
        reason = Double.parseDouble(values[4]);
    }

    String getQuote() { return quote; }
    String getTitle() { return title; }
    Boolean getIsDoctrine() { return isDoctrine; }
    URL getSource() { return source; }
    double getReason() { return reason; }

    public void setQuote(String q) { quote = q; }
    public void setTitle(String t) { title = t; }
    public void setIsDoctrine(Boolean i) { isDoctrine = i; }
    public void setSource(String s) {
        try {
            source = new URL(s);
        } catch (MalformedURLException e)
        {
            System.out.println("Failure in converting url: " + s);
            try {
                source = new URL("http://google.com");
            } catch (MalformedURLException f) {
                //Google didn't work either
            }
        }
    }
    public void setReason(Double r) { reason = r; }

    public String toString() {
        String s = title + "ß" + quote + "ß";
        if (isDoctrine)
            s += "T";
        else
            s += "F";
        s += "ß";
        s += source.toString() + "ß" + Double.toString(reason);
        return s;
    }
}

package com.xlr8.jackson.doctrineoropinion;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jackson on 9/27/16.
 */

public class Question {
    String details,title;
    Boolean isTrue;
    URL source;
    double explanation;

    private final String defaultSource = "http://google,com";

    Question(String t, String q, Boolean d, String s, double r) {
        details = q;
        title = t;
        isTrue = d;
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
        explanation = r;
    }
    Question() {
        details = "Blank";
        title = "Blank";
        isTrue = true;
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
            explanation = 0.0;
        } else {
            String[] values = rawData.split("ß");
            title = values[0];
            details = values[1];
            isTrue = false;
            if (values[2].equals("T"))
                isTrue = true;
            try {
                source = new URL(values[3]);
            } catch (MalformedURLException e) {
                System.out.println("Failure in converting url: " + values[3]);
                try {
                    source = new URL(defaultSource);
                } catch (MalformedURLException f) {
                    //Google didn't work either
                }
            }
            explanation = Double.parseDouble(values[4]);
        }
    }

    String getDetails() { return details; }
    String getTitle() { return title; }
    Boolean isTrue() { return isTrue; }
    URL getSource() { return source; }
    double getExplanation() { return explanation; }

    public void setDetails(String q) { details = q; }
    public void setTitle(String t) { title = t; }
    public void setTrue(Boolean i) { isTrue = i; }
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
    public void setExplanation(Double r) { explanation = r; }

    public String toString() {
        String s = title + "ß" + details + "ß";
        if (isTrue)
            s += "T";
        else
            s += "F";
        s += "ß";
        s += source.toString() + "ß" + Double.toString(explanation);
        return s;
    }
}

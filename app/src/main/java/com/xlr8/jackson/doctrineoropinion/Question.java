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
    Boolean approved;
    Integer id;

    Question(int i, String t, String q, Boolean d, String s, Boolean a) {
        id = i;
        details = q;
        title = t;
        isTrue = d;
        source = s;
        approved = a;
    }
    Question() {
        id = 0;
        details = "Blank";
        title = "Blank";
        isTrue = true;
        source = "http://google.com";
        approved = false;
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
        if (count != 6)
        {
            id = 0;
            details = "Blank";
            title = "Blank";
            isTrue = true;
            source = "http://google.com";
            approved = false;
        } else {
            String[] values = rawData.split("ß");
            id = Integer.parseInt(values[0]);
            title = values[1];
            details = values[2];
            isTrue = false;
            if (values[3].equals("T"))
                isTrue = true;
            source = values[4];
            approved = false;
            if (values[5].equals("T"))
                approved = true;
        }
    }

    String getDetails() { return details; }
    String getTitle() { return title; }
    Boolean isTrue() { return isTrue; }
    String getSource() { return source; }
    Boolean getApproved() { return approved; }
    int getId() { return id; }

    public void setDetails(String q) { details = q; }
    public void setTitle(String t) { title = t; }
    public void setTrue(Boolean i) { isTrue = i; }
    public void setSource(String s) { source = s; }
    public void setApproved(Boolean a) { approved = a; }
    public void setId(int i) { id = i; }

    public String toString() {
        String s = id + "ß" + title + "ß" + details + "ß";
        if (isTrue)
            s += "T";
        else
            s += "F";
        s += "ß";
        s += source + "ß";
        if (approved)
            s += "T";
        else
            s += "F";
        return s;
    }

    public HashMap toHashMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("details", details);
        map.put("title", title);
        map.put("isTrue", isTrue);
        map.put("source", source);
        map.put("approved", approved);
        return map;
    }
}

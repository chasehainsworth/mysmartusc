package com.example.cs310.mysmartusc;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private List<String> emailAddresses;
    private List<String> bodyKeywords;
    private List<String> subjectKeywords;
    private String mType;
    private DatabaseInterface mDatabaseInterface;

    //What needs to be the match ratio to return true?
    private double keywordMatchFrequency = 0.1;

    public Filter() {}

    public Filter(String type, DatabaseInterface databaseInterface) {
        mType = type;
        mDatabaseInterface = databaseInterface;
    }

    public void refreshKeywords() {
        emailAddresses = loadKeywords("Sender");
        bodyKeywords = loadKeywords("Body");
        subjectKeywords = loadKeywords("Subject");
    }

    public List<String> loadKeywords(String category) {
        List<String> keywords = new ArrayList<>();
        Cursor cursor = mDatabaseInterface.getKeywordsByType(mType, category);

        if (cursor.moveToFirst()) {
            do {
                String currentKeyword = cursor.getString(1);
                Log.e("Adding keyword", currentKeyword);
                keywords.add(currentKeyword);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return keywords;
    }

    public boolean sort(Email email) {
        //Determine what category the email belongs to.

        String currentEmailSubject = email.getSubject();
        String currentEmailSender = email.getSender();
        String currentEmailBody = email.getBody();

        for (String address : emailAddresses) {
            if(currentEmailSender.toLowerCase().contains(address.toLowerCase())) {
                return true;
            }
        }
        for (String subject : subjectKeywords) {
            if(currentEmailSubject.toLowerCase().contains(subject.toLowerCase())) {
                return true;
            }
        }
        for (String body : bodyKeywords) {
            if(currentEmailBody.toLowerCase().contains(body.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void addEmailAddress(String emailAddress) {
        emailAddresses.add(emailAddress);
    }

    public void addBodyKeyword(String keyword) {
        bodyKeywords.add(keyword);
    }

    public void addSubjectKeyword(String keyword) {
        subjectKeywords.add(keyword);
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    public List<String> getBodyKeywords() {
        return bodyKeywords;
    }

    public List<String> getSubjectKeywords() {
        return subjectKeywords;
    }



}

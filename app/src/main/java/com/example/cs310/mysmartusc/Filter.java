package com.example.cs310.mysmartusc;

import android.database.Cursor;

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

    public Filter(String type, DatabaseInterface databaseInterface)
    {
        mType = type;
        mDatabaseInterface = databaseInterface;
        emailAddresses = loadKeywords("address");
        bodyKeywords = loadKeywords("body");
        subjectKeywords = loadKeywords("subject");
    }

    private List<String> loadKeywords(String category)
    {
        List<String> keywords = new ArrayList<>();
        Cursor cursor = mDatabaseInterface.getKeywordsByType(mType, category);
        while(!cursor.isLast())
        {
            keywords.add(cursor.getString(1));
            cursor.moveToNext();
        }
        return keywords;
    }

    public void addEmailAddress(String emailAddress){
        this.emailAddresses.add(emailAddress);
    }

    public void addBodyKeyword(String keyword){
        this.bodyKeywords.add(keyword);
    }

    public void addSubjectKeyword(String keyword){
        this.addSubjectKeyword(keyword);
    }

    public List<String> getEmailAddresses(){
        return this.emailAddresses;
    }

    public List<String> getBodyKeywords(){
        return this.bodyKeywords;
    }

    public List<String> getSubjectKeywords(){
        return this.subjectKeywords;
    }

    public boolean sort(Email email){
        //Determine what category the email belongs to.

        String currentEmailSubject = email.getSubject();
        String currentEmailSender = email.getSender();
        String currentEmailBody = email.getBody();

        if(emailAddresses.contains(currentEmailSender)){
            return true;
        } else if (subjectKeywords.contains(currentEmailSubject)){
            return true;
        } else {
            //Keywords may still be in the body we need to tokenize...
            String words[] = currentEmailBody.split(" ");
            double wordsLength = words.length;

            //Figuring out how many matches there are.
            int matches = 0;
            for (String word : words){
                if (bodyKeywords.contains(word)){
                    matches++;
                }
            }

            //Finding the frequency of the matched keywords to all the
            //words in the email.
            if(wordsLength / ((double) bodyKeywords.size()) >= 0.1){
                return true;
            }
        }

        return false;
    }
}

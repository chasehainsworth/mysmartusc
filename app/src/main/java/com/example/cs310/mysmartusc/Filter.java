package com.example.cs310.mysmartusc;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private List<String> emailAddresses;
    private List<String> bodyKeywords;
    private List<String> subjectKeywords;

    public Filter(){
        this.emailAddresses = new ArrayList<>();
        this.bodyKeywords = new ArrayList<>();
        this.subjectKeywords = new ArrayList<>();
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

    }
}

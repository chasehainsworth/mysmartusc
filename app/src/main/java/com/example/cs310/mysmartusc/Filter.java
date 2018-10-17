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

    }
}

package com.example.cs310.mysmartusc;

import java.math.BigInteger;

public class Email {
    private String subject, body, sender;

    public Email(){
        this.subject = "";
        this.body = "";
        this.sender = "";
    }

    public Email(String subject, String body, String sender){
        this.subject = subject;
        this.sender = sender;
        this.body = body;
    }

    public String getSubject(){
        return this.subject;
    }

    public String getBody(){
        return this.body;
    }

    public String getSender(){
        return this.sender;
    }
}

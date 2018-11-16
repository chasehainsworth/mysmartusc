package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class EmailViewerActivity extends Activity {
    private TextView fromLabel, subjectLabel, body;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_viewer_activity);

        this.fromLabel = (TextView) findViewById(R.id.emailFromLabel);
        this.subjectLabel = (TextView) findViewById(R.id.emailSubjectLabel);
        this.body = (TextView) findViewById(R.id.emailBody);

        //The email body's key should be: emailBody
        String emailBody = getIntent().getStringExtra("body");
        //The email's subject key should be: emailSubject
        String emailSubject = getIntent().getStringExtra("subject");
        //The email's sender key should be: emailSender
        String emailSender = getIntent().getStringExtra("sender");

        this.fromLabel.setText(emailSender);
        this.subjectLabel.setText(emailSubject);
        this.body.setText(Html.fromHtml(getHighlightedBody(emailBody)));

    }

    private String getHighlightedBody(String body){
        String type = getIntent().getStringExtra("type");

        DatabaseInterface db = DatabaseInterface.getInstance(this);

        ArrayList<String> keywords = new ArrayList<>();

        Cursor c = db.getKeywordsByType(type, "Body");
        c.moveToFirst();
        while(!c.isAfterLast()){
            String word = c.getString(c.getColumnIndex("TEXT")).toLowerCase();
            if(word.contains(" ")){
                String[] mWords = word.split(" ");
                for(String s : mWords){
                    keywords.add(s);
                }
            } else {
                keywords.add(word);
            }
            c.moveToNext();
        }


        String[] allBodyWords = body.split(" ");
        for(String w : allBodyWords){
            if(keywords.contains(w.toLowerCase())){
                body = body.replaceAll(w, "<font color = 'red'>" + w + "</font> ");
                //body = body.replaceAll(w, "<span style='background-color: yellow'>" + w + "</span> ");
            }
        }
        return body;
    }
}

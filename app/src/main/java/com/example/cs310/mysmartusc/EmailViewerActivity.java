package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        this.body.setText(emailBody);

    }
}

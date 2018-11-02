package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewKeywordsActivity extends Activity {
    private TextView urgentKeys, savedKeys, spamKeys;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_keywords_activity);

        this.urgentKeys = (TextView) findViewById(R.id.urgentKeywords);
        this.savedKeys = (TextView) findViewById(R.id.savedKeywords);
        this.spamKeys = (TextView) findViewById(R.id.spamKeywords);

//        this.urgentKeys.setText(emailSender);
//        this.savedKeys.setText(emailSubject);
//        this.spamKeys.setText(emailBody);

    }
}

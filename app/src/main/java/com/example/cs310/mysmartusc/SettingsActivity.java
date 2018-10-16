package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        EditText urgentKeywords = (EditText) findViewById(R.id.urgentKeywords);
        EditText savedKeywords = (EditText) findViewById(R.id.savedKeywords);
        EditText spamKeywords = (EditText) findViewById(R.id.spamKeywords);
        Button submit = (Button) findViewById(R.id.submitKeywords);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}


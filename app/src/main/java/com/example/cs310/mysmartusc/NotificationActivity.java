package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        Button saved = (Button) findViewById(R.id.submit);
        Button urgent = (Button) findViewById(R.id.urgent);
        Button spam = (Button) findViewById(R.id.spam);
    }



}

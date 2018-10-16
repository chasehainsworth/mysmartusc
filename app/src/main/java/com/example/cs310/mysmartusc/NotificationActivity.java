package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        Button saved = (Button) findViewById(R.id.submit);
        Button urgent = (Button) findViewById(R.id.urgent);
        Button spam = (Button) findViewById(R.id.spam);
<<<<<<< HEAD
=======

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(NotificationActivity.this, SaveActivity.class);
                startActivity(notificationsIntent);
            }
        });

        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(NotificationActivity.this, UrgentActivity.class);
                startActivity(notificationsIntent);
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(NotificationActivity.this, SpamActivity.class);
                startActivity(notificationsIntent);
            }
        });

    }
>>>>>>> dfb55ddeb58ac99202c08172758e4c6b713074ba

        /*
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(NotificationActivity.this, SavedPageActivity.class);
                startActivity(notificationsIntent);
            }
        });

        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(NotificationActivity.this, UrgentPageActivity.class);
                startActivity(notificationsIntent);
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(NotificationActivity.this, SpamPageActivity.class);
                startActivity(notificationsIntent);
            }
        });
*/
    }

}

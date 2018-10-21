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

        Button saved = (Button) findViewById(R.id.savedButton);
        Button urgent = (Button) findViewById(R.id.urgentButton);
        Button spam = (Button) findViewById(R.id.spamButton);

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveIntent = new Intent(NotificationActivity.this, SaveActivity.class);
                saveIntent.putExtra("accountName", getIntent().getStringExtra("accountName"));
                startActivity(saveIntent);
            }
        });

        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urgentIntent = new Intent(NotificationActivity.this, UrgentActivity.class);
                urgentIntent.putExtra("accountName", getIntent().getStringExtra("accountName"));
                startActivity(urgentIntent);
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent spamIntent = new Intent(NotificationActivity.this, SpamActivity.class);
                spamIntent.putExtra("accountName", getIntent().getStringExtra("accountName"));
                startActivity(spamIntent);
            }
        });

    }
}


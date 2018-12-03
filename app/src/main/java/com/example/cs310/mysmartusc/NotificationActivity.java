package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class NotificationActivity extends Activity {

    Spinner dropdown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        Button saved = (Button) findViewById(R.id.savedButton);
        Button urgent = (Button) findViewById(R.id.urgentButton);
        Button spam = (Button) findViewById(R.id.spamButton);
        Button news = (Button) findViewById(R.id.newsButton);

        dropdown = findViewById(R.id.numberEmails);
        String[] items = new String[]{"Number of Emails to View", "5", "10", "15", "All"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numEmails = dropdown.getSelectedItem().toString();
                Intent saveIntent = new Intent(NotificationActivity.this, SaveActivity.class);
                saveIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                saveIntent.putExtra("numEmails", numEmails);
                startActivity(saveIntent);
            }
        });

        urgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numEmails = dropdown.getSelectedItem().toString();
                Intent urgentIntent = new Intent(NotificationActivity.this, UrgentActivity.class);
                urgentIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                urgentIntent.putExtra("numEmails", numEmails);
                startActivity(urgentIntent);
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numEmails = dropdown.getSelectedItem().toString();
                Intent spamIntent = new Intent(NotificationActivity.this, SpamActivity.class);
                spamIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                spamIntent.putExtra("numEmails", numEmails);
                startActivity(spamIntent);
            }
        });

        news.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String numEmails = dropdown.getSelectedItem().toString();
                Intent newsIntent = new Intent(NotificationActivity.this, NewsletterActivity.class);
                newsIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                newsIntent.putExtra("numEmails", numEmails);
                startActivity(newsIntent);
            }
        });

    }
}


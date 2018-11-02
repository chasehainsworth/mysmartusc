package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePageActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        /**
         * To pass data between activities:
         * intent.putExtra(String key, Object data)
         * intent.getStringExtra(String key)
         */

        TextView tv = (TextView) findViewById(R.id.homeLabel);
        Button notifications = (Button) findViewById(R.id.notificationsButton);
        Button settings = (Button) findViewById(R.id.settingsButton);
        Button viewKeywords = (Button) findViewById(R.id.viewKeywordsButton);
        Button account = (Button) findViewById(R.id.account_button);

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(HomePageActivity.this, NotificationActivity.class);
                notificationsIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                startActivity(notificationsIntent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(HomePageActivity.this, SettingsActivity.class);
                settingsIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                startActivity(settingsIntent);
            }
        });

        viewKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewKeywordsIntent = new Intent(HomePageActivity.this, ViewKeywordsActivity.class);
                viewKeywordsIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                startActivity(viewKeywordsIntent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homepageIntent = new Intent(HomePageActivity.this, LoginActivity.class);
                startActivity(homepageIntent);
            }
        });
    }
}
package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationsIntent = new Intent(HomePageActivity.this, NotificationActivity.class);
                startActivity(notificationsIntent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(HomePageActivity.this, SettingsActivity.class);
                settingsIntent.putExtra("accountName", getIntent().getStringArrayExtra("accountName"));
                startActivity(settingsIntent);
            }
        });
    }
}
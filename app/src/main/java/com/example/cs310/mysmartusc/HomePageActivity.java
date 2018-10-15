package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class HomePageActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        TextView tv = (TextView) findViewById(R.id.homeLabel);
        Button notifications = (Button) findViewById(R.id.notificationsButton);
        Button settings = (Button) findViewById(R.id.settingsButton);

    }
}

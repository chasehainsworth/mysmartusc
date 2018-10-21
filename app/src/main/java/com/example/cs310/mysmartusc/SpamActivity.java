package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;

public class SpamActivity extends Activity {

    private ArrayList<Email> emails;
    DatabaseInterface db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spam_activity);

        emails = new ArrayList<>();
        db = new DatabaseInterface(getApplicationContext());

        String user = getIntent().getStringExtra("accountName");
        String type = "spam";

        String email = user.split("@")[0];
        String domain = user.split("@")[1];

        // TODO: RETREIVE EMAILS FROM DATABASE USING mAccount passed from LoginActivity

        ArrayList<String> emailHeaders = new ArrayList<>();

        for(Email e : emails){
            emailHeaders.add(e.getSubject());
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emailHeaders);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent emailIntent = new Intent(SpamActivity.this, EmailViewerActivity.class);

                emailIntent.putExtra("subject", emails.get(position).getSubject());
                emailIntent.putExtra("body", emails.get(position).getBody());
                emailIntent.putExtra("sender", emails.get(position).getSender());

                startActivity(emailIntent);
            }

        });

    }

}

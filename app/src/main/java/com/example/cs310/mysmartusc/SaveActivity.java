package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveActivity extends Activity {

    private ArrayList<Email> emails;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity);

        emails = new ArrayList<>();

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
                Intent emailIntent = new Intent(SaveActivity.this, EmailViewerActivity.class);

                emailIntent.putExtra("subject", emails.get(position).getSubject());
                emailIntent.putExtra("body", emails.get(position).getBody());
                emailIntent.putExtra("sender", emails.get(position).getSender());

                startActivity(emailIntent);
            }

        });

    }

}

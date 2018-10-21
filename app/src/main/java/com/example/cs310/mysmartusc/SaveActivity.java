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
import android.database.Cursor;

public class SaveActivity extends Activity {

    private ArrayList<Email> emails;
    DatabaseInterface db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity);

        emails = new ArrayList<>();
        db = new DatabaseInterface(getApplicationContext());

        String user = getIntent().getStringExtra("accountName");
        String type = "saved";

        Cursor cursor = db.getEmailByType(user, type);

        if (cursor != null && cursor.getCount() > 0 ) {
            if  (cursor.moveToFirst()) {
                do {
                    String subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                    String body = cursor.getString(cursor.getColumnIndex("BODY"));
                    String sender_user = cursor.getString(cursor.getColumnIndex("SENDER_USER"));
                    String sender_domain = cursor.getString(cursor.getColumnIndex("SENDER_USER"));
                    emails.add(new Email(subject, body, sender_user + "@" + sender_domain));
                }while (cursor.moveToNext());
            }
        }
        cursor.close();

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

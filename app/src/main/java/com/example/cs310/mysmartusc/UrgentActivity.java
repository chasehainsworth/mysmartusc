package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.Cursor;

import java.math.BigInteger;
import java.util.ArrayList;

public class UrgentActivity extends Activity {

    private ArrayList<Email> emails;
    DatabaseInterface db;

    protected void onCreate(Bundle urgentInstanceState) {
        super.onCreate(urgentInstanceState);
        setContentView(R.layout.save_activity);

        emails = new ArrayList<>();
        db = DatabaseInterface.getInstance(this);
        Account account = (Account)getIntent().getParcelableExtra("account");
        String user = account.name;
        String type = "urgent";


        Cursor cursor = db.getEmailByType(user, type);

        if (cursor != null && cursor.getCount() > 0 ) {
            if  (cursor.moveToFirst()) {
                do {
                    String subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                    String body = cursor.getString(cursor.getColumnIndex("BODY"));
                    String sender_user = cursor.getString(cursor.getColumnIndex("SENDER_USER"));
                    String sender_domain = cursor.getString(cursor.getColumnIndex("SENDER_DOMAIN"));


                    emails.add(new Email(subject, body, sender_user + "@" + sender_domain));
                }while (cursor.moveToNext());
            }
        } else {
            if(cursor == null){
                Log.e("UrgentActivity", "The cursor to get the emails is null!");
            } else {
                Log.e("UrgentActivity", "There are no emails here!");
            }
        }
        cursor.close();

        ArrayList<String> emailHeaders = new ArrayList<>();

        for(Email e : emails){
            for(String s : emailHeaders){
                if (!s.equals(e.getSubject())){
                    emailHeaders.add(e.getSubject());
                }
            }
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emailHeaders);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent emailIntent = new Intent(UrgentActivity.this, EmailViewerActivity.class);

                emailIntent.putExtra("subject", emails.get(position).getSubject());
                emailIntent.putExtra("body", emails.get(position).getBody());
                emailIntent.putExtra("sender", emails.get(position).getSender());

                startActivity(emailIntent);
            }

        });

    }

}

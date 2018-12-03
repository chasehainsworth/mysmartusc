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
import java.util.Collections;

public class UrgentActivity extends Activity {

    private ArrayList<Email> emails;
    DatabaseInterface db;
    String mUsername;
    String mType;
    Account mAccount = null;

    @Override
    protected void onStart() {
        super.onStart();
        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        emails.clear();
        Log.e("UrgentActivity", "Getting " + mUsername + ", type: " + mType);
        Cursor cursor;
        String numEmails = getIntent().getStringExtra("numEmails");

        String search = getIntent().getStringExtra("search");
        Log.e("NotifActSearchAfter", "Sent search String " + search);

        if(numEmails.equals("All") || numEmails.equals("Number of Emails to View")){
            cursor = db.getEmailByType(mUsername, mType);
        }else{
            cursor = db.getEmailByType(mUsername, mType, numEmails);
        }
        if(cursor.getCount() > 0){
            Log.e("Urgent", "Count: " + cursor.getCount());
        }else {
            Log.e("Urgent","Zero results!");
        }
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
                    String body = cursor.getString(cursor.getColumnIndex("BODY"));
                    String sender_user = cursor.getString(cursor.getColumnIndex("SENDER_USER"));
                    String sender_domain = cursor.getString(cursor.getColumnIndex("SENDER_DOMAIN"));
                    Long date = cursor.getLong(cursor.getColumnIndex("INTERNAL_DATE"));
                    Boolean read = cursor.getInt(cursor.getColumnIndex("READ")) == 1;

                    Log.e("UrgentActivity", "Creating email with subject: " + subject);
                    Log.e("UrgentActSearch", "Printing search string: " + search);
                    if(search == null)
                    {
                        emails.add(new Email(subject, body, sender_user + "@" + sender_domain, date, read));
                    }
                    else
                    {
                        String checker = subject + " " + body + " " + sender_user + "@" + sender_domain;
                        if(checker.contains(search))
                        {
                            emails.add(new Email(subject, body, sender_user + "@" + sender_domain, date, read));
                        }
                    }


                } while (cursor.moveToNext());
            }
        } else {
            if (cursor == null) {
                Log.e("UrgentActivity", "The cursor to get the emails is null!");
            } else {
                Log.e("UrgentActivity", "There are no emails here!");
            }
        }
        cursor.close();

        ArrayList<String> emailHeaders = new ArrayList<>();
        ArrayList<Email> sortedEmail = new ArrayList<>();

        for (Email e : emails) {
            sortedEmail.add(e);
        }

        Collections.sort(sortedEmail);


        for (Email e : sortedEmail) {
            emailHeaders.add(e.getSubject());
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emailHeaders);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.markEmailAsRead(emails.get(position), mUsername, mType);
                Intent emailIntent = new Intent(UrgentActivity.this, EmailViewerActivity.class);

                emailIntent.putExtra("subject", emails.get(position).getSubject());
                emailIntent.putExtra("body", emails.get(position).getBody());
                emailIntent.putExtra("sender", emails.get(position).getSender());
                emailIntent.putExtra("type", mType);

                startActivity(emailIntent);
            }

        });
    }

    protected void onCreate(Bundle urgentInstanceState) {
        super.onCreate(urgentInstanceState);
        setContentView(R.layout.urgent_activity);

        emails = new ArrayList<>();
        db = DatabaseInterface.getInstance(this);
        mAccount = (Account) getIntent().getParcelableExtra("account");
        mUsername = mAccount.name;
        mType = "urgent";
        refreshView();

    }

}

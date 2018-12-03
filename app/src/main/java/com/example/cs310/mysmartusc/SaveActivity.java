package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.Cursor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class SaveActivity extends Activity {

    private ArrayList<Email> emails;
    DatabaseInterface db;
    String mUsername;
    String mType;

    @Override
    protected void onStart() {
        super.onStart();
        refreshView();
    }

    private void refreshView() {
        emails.clear();
        Log.e("SaveActivity", "Getting " + mUsername + ", type: " + mType);
        Cursor cursor;
        String numEmails = getIntent().getStringExtra("numEmails");

        String search = getIntent().getStringExtra("search");

        if(numEmails.equals("All") || numEmails.equals("Number of Emails to View")){
            cursor = db.getEmailByType(mUsername, mType);
        }else{
            cursor = db.getEmailByType(mUsername, mType, numEmails);
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

                    Log.e("SaveActivity", "Creating email with subject: " + subject);
//                    emails.add(new Email(subject, body, sender_user + "@" + sender_domain, date, read));

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
                Log.e("SaveActivity", "The cursor to get the emails is null!");
            } else {
                Log.e("SaveActivity", "There are no emails here!");
            }
        }
        cursor.close();


        ArrayList<String> emailHeaders = new ArrayList<>();
        final ArrayList<Email> sortedEmail = new ArrayList<>();

        for (Email e : emails) {
            sortedEmail.add(e);
        }

        Collections.sort(sortedEmail);

        for (Email e : sortedEmail) {
            emailHeaders.add(e.getSubject());
        }


        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emailHeaders){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position,convertView,parent);

                if(sortedEmail.get(position).getRead() == false)
                {
                    view.setBackgroundColor(Color.parseColor("#FFFFCC"));
                }else if(sortedEmail.get(position).getRead() == true){
                    view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db.markEmailAsRead(sortedEmail.get(position), mUsername, mType);
                Intent emailIntent = new Intent(SaveActivity.this, EmailViewerActivity.class);

                emailIntent.putExtra("subject", sortedEmail.get(position).getSubject());
                emailIntent.putExtra("body", sortedEmail.get(position).getBody());
                emailIntent.putExtra("sender", sortedEmail.get(position).getSender());
                emailIntent.putExtra("type", mType);

                startActivity(emailIntent);
            }

        });
    }

    protected void onCreate(Bundle urgentInstanceState) {
        super.onCreate(urgentInstanceState);
        setContentView(R.layout.save_activity);

        emails = new ArrayList<>();
        db = DatabaseInterface.getInstance(this);
        Account account = (Account) getIntent().getParcelableExtra("account");
        mUsername = account.name;
        mType = "saved";
        refreshView();


    }

}

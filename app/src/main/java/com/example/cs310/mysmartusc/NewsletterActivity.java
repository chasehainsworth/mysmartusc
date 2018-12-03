package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class NewsletterActivity extends Activity {

    private ArrayList<Email> emails;
    DatabaseInterface db;
    String mUsername;
    String mType;
    Account mAccount = null;

    String search = getIntent().getStringExtra("search");

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

        Log.e("NewsletterActivity", "Getting " + mUsername + ", type: " + mType);
        Cursor cursor;
        String numEmails = getIntent().getStringExtra("numEmails");

        if(numEmails.equals("All") || numEmails.equals("Number of Emails to View")){
            cursor = db.getEmailByType(mUsername, mType);
        }else{
            cursor = db.getEmailByType(mUsername, mType, numEmails);
        }
        if(cursor.getCount() > 0){
            Log.e("News", "Count: " + cursor.getCount());
        }else {
            Log.e("News","Zero results!");
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

                    Log.e("NewsletterActivity", "Creating email with subject: " + subject);
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
                Log.e("NewsletterActivity", "The cursor to get the emails is null!");
            } else {
                Log.e("NewsletterActivity", "There are no emails here!");
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
                Intent emailIntent = new Intent(NewsletterActivity.this, EmailViewerActivity.class);

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
        setContentView(R.layout.newsletter_activity);

        emails = new ArrayList<>();
        db = DatabaseInterface.getInstance(this);
        mAccount = (Account) getIntent().getParcelableExtra("account");
        mUsername = mAccount.name;
        mType = "news";
        refreshView();

    }
}

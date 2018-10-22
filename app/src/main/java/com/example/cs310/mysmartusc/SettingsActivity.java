package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends Activity {
    private EditText urgentKeywords;
    private EditText savedKeywords;
    private EditText spamKeywords;
    private Button saveSubjectButton, saveSenderButton, saveBodyButton;
    private String accountName;
    private Account mAccount;
    //final Toast toast = new Toast(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        mAccount = (Account) getIntent().getParcelableExtra("account");

        accountName = mAccount.name;

        urgentKeywords = (EditText) findViewById(R.id.urgentKeywords);
        savedKeywords = (EditText) findViewById(R.id.savedKeywords);
        spamKeywords = (EditText) findViewById(R.id.spamKeywords);

        saveSubjectButton = (Button) findViewById(R.id.saveSubject);
        saveSenderButton = (Button) findViewById(R.id.saveSender);
        saveBodyButton = (Button) findViewById(R.id.saveBody);


        //Add keywords by list method
        saveSenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton("Sender");
            }
        });

        //Add keywords by list method
        saveSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton("Subject");
            }
        });

        //Add keywords by list method
        saveBodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton("Body");
            }
        });
    }

    //Where type=urgent,saved,spam
    private boolean addKeywordsToDatabase(String type, String[] keywords, String category) {
        DatabaseInterface di = DatabaseInterface.getInstance(this);
        for (String key : keywords) {
            //If the adding of the keyword fails then print error
            //Keyword, type, username
            if (!di.addKeyword(key, type, accountName, category)) {
                System.err.println("Error adding keyword to the database!");
                return false;
            }
        }
        return true;
    }

    private void clickButton(String category) {
        boolean result = true;
        String uKeywords[] = urgentKeywords.getText().toString().split(",");
        String sKeywords[] = spamKeywords.getText().toString().split(",");
        String savKeywords[] = savedKeywords.getText().toString().split(",");

        if (uKeywords[0].length() > 0) {
            //Adding the urgent keywords. If they fail display a message.
            if (!addKeywordsToDatabase("urgent", uKeywords, category)) {
                //toast.makeText(SettingsActivity.this, "Failed adding urgent keywords!", Toast.LENGTH_LONG);
                result = false;
            } else {
                //toast.makeText(SettingsActivity.this, "Added urgent keywords!", Toast.LENGTH_LONG);
                urgentKeywords.setText("");

            }
        }


        if (sKeywords[0].length() > 0) {
            //Adding the spam keywords. IF they fail display a message
            if (!addKeywordsToDatabase("spam", sKeywords, category)) {
                //toast.makeText(SettingsActivity.this, "Failed adding spam keywords!", Toast.LENGTH_LONG);
                result = false;
            } else {
                spamKeywords.setText("");
            }

        }

        if (savKeywords[0].length() > 0) {
            //Adding the save keywords. IF they fail display a message
            if (!addKeywordsToDatabase("saved", savKeywords, category)) {
                //toast.makeText(SettingsActivity.this, "Failed adding saved keywords!", Toast.LENGTH_LONG);
                result = false;
            } else {
                savedKeywords.setText("");
            }
        }


        //All the keywords have been added
        if (result) {
            //toast.makeText(SettingsActivity.this, "Added!", Toast.LENGTH_SHORT);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

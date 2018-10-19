package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends Activity {
    private EditText urgentKeywords;
    private EditText savedKeywords;
    private EditText spamKeywords;
    private Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        urgentKeywords = (EditText) findViewById(R.id.urgentKeywords);
        savedKeywords = (EditText) findViewById(R.id.savedKeywords);
        spamKeywords = (EditText) findViewById(R.id.spamKeywords);

        submit = (Button) findViewById(R.id.submitKeywords);

        final Toast toast = new Toast(this);

        //Add keywords by list method
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = true;
                String uKeywords[] = urgentKeywords.getText().toString().split(",");
                String sKeywords[] = spamKeywords.getText().toString().split(",");
                String savKeywords[] = savedKeywords.getText().toString().split(",");


                //Adding the urgent keywords. If they fail display a message.
                if(!addKeywordsToDatabase("urgent", uKeywords)){
                    toast.makeText(SettingsActivity.this, "Failed adding urgent keywords!", Toast.LENGTH_LONG);
                    result = false;
                }

                //Adding the spam keywords. IF they fail display a message
                if(!addKeywordsToDatabase("spam", sKeywords)){
                    toast.makeText(SettingsActivity.this, "Failed adding spam keywords!", Toast.LENGTH_LONG);
                    result = false;
                }

                //Adding the save keywords. IF they fail display a message
                if(!addKeywordsToDatabase("saved", savKeywords)){
                    toast.makeText(SettingsActivity.this, "Failed adding saved keywords!", Toast.LENGTH_LONG);
                    result = false;
                }

                //All the keywords have been added
                if(result){
                    toast.makeText(SettingsActivity.this, "Added!", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Where type=urgent,saved,spam
    private boolean addKeywordsToDatabase(String type, String[] keywords){
        DatabaseInterface di = new DatabaseInterface(this);
        for(String key : keywords) {
            //If the adding of the keyword fails then print error
            //Keyword, type, username
            if (!di.addKeyword(key, type, null)) {
                System.err.println("Error adding keyword to the database!");
                return false;
            }
        }
        return true;
    }
}


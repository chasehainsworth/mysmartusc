package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import android.database.Cursor;

public class ViewKeywordsActivity extends Activity {
    private TextView urgentKeys, savedKeys, spamKeys;
    ArrayList<String> urgentKeywords, savedKeywords, spamKeywords;
    DatabaseInterface db;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_keywords_activity);

        this.urgentKeys = (TextView) findViewById(R.id.urgentKeywords);
        this.savedKeys = (TextView) findViewById(R.id.savedKeywords);
        this.spamKeys = (TextView) findViewById(R.id.spamKeywords);

        db = DatabaseInterface.getInstance(this);
        Cursor urgC = db.getKeywordsByType("urgent", "");
        Cursor savC = db.getKeywordsByType("saved", "");
        Cursor spmC = db.getKeywordsByType("spam", "");
    }
}

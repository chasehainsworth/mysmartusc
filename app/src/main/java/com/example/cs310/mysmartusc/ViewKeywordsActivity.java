package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import android.database.Cursor;

public class ViewKeywordsActivity extends Activity {
    private TextView urgentKeys, savedKeys, spamKeys;
    ArrayList<String> urgentKeywords, savedKeywords, spamKeywords;
    DatabaseInterface db;
    private ListView urgentView, savedView, spamView;

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

        for(urgC.moveToFirst(); !urgC.isAfterLast(); urgC.moveToNext()) {
            // The Cursor is now set to the right position
            urgentKeywords.add(urgC.getString(1));
        }
        for(savC.moveToFirst(); !savC.isAfterLast(); savC.moveToNext()) {
            savedKeywords.add(savC.getString(1));
        }
        for(spmC.moveToFirst(); !spmC.isAfterLast(); spmC.moveToNext()) {
            spamKeywords.add(spmC.getString(1));
        }

        urgentView = (ListView) findViewById(R.id.urgentView);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, urgentKeywords);
        urgentView.setAdapter(adapter1);
        savedView = (ListView) findViewById(R.id.urgentView);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, urgentKeywords);
        savedView.setAdapter(adapter2);
        spamView = (ListView) findViewById(R.id.urgentView);
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, urgentKeywords);
        spamView.setAdapter(adapter3);

        Button addKeywords = (Button) findViewById(R.id.addKeywords);
        addKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(ViewKeywordsActivity.this, SettingsActivity.class);
                settingsIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                startActivity(settingsIntent);
            }
        });
    }
}

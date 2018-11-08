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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

public class ViewKeywordsActivity extends Activity {
    private TextView urgentKeys, savedKeys, spamKeys;
//    ArrayList<String> urgentKeywords, savedKeywords, spamKeywords;
    ArrayList<String> keywords;
    ArrayList<String> types;
    DatabaseInterface db;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_keywords_activity);

//        this.urgentKeys = (TextView) findViewById(R.id.urgentKeywords);
//        this.savedKeys = (TextView) findViewById(R.id.savedKeywords);
//        this.spamKeys = (TextView) findViewById(R.id.spamKeywords);

        keywords = new ArrayList<String>();
        types = new ArrayList<String>();

        db = DatabaseInterface.getInstance(this);
        Cursor c = db.getAllKeywords();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            keywords.add(c.getString(c.getColumnIndex("TEXT")));
            c.moveToNext();
        }
        c.moveToFirst();
        while(!c.isAfterLast()) {
            types.add(c.getString(c.getColumnIndex("TYPE")));
            c.moveToNext();
        }


        listView = (ListView) findViewById(R.id.listView);
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i= 0; i < keywords.size(); i++) {
            Map<String, String> map = new HashMap<String, String>(2);
            map.put("keyword", keywords.get(i));
            map.put("type", types.get(i));
            listMap.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listMap, android.R.layout.simple_list_item_2,
                new String[] {"keyword", "type"},
                new int[] {android.R.id.text1,
                        android.R.id.text2}); {
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent keywordIntent = new Intent(ViewKeywordsActivity.this, KeywordRemoveActivity.class);

                keywordIntent.putExtra("keyword", keywords.get(position));
                keywordIntent.putExtra("type", types.get(position));
                startActivity(keywordIntent);
            }

        });



//        Cursor urgC = db.getKeywordsByType("urgent", "");
//        Cursor savC = db.getKeywordsByType("saved", "");
//        Cursor spmC = db.getKeywordsByType("spam", "");

//        for(urgC.moveToFirst(); !urgC.isAfterLast(); urgC.moveToNext()) {
//            // The Cursor is now set to the right position
//            urgentKeywords.add(urgC.getString(1));
//        }
//        for(savC.moveToFirst(); !savC.isAfterLast(); savC.moveToNext()) {
//            savedKeywords.add(savC.getString(1));
//        }
//        for(spmC.moveToFirst(); !spmC.isAfterLast(); spmC.moveToNext()) {
//            spamKeywords.add(spmC.getString(1));
//        }
//
//        urgentView = (ListView) findViewById(R.id.urgentView);
//        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, urgentKeywords);
//        urgentView.setAdapter(adapter1);
//        savedView = (ListView) findViewById(R.id.urgentView);
//        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, urgentKeywords);
//        savedView.setAdapter(adapter2);
//        spamView = (ListView) findViewById(R.id.urgentView);
//        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, urgentKeywords);
//        spamView.setAdapter(adapter3);

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

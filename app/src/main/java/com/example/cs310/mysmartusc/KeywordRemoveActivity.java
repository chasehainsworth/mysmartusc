package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class KeywordRemoveActivity extends Activity {
    private TextView keywordLabel, typeLabel;
    private DatabaseInterface mDatabaseInterface;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_keyword_activity);

        this.keywordLabel = (TextView) findViewById(R.id.removeKeywordLabel);
        this.typeLabel = (TextView) findViewById(R.id.removeTypeLabel);

        Button remove = (Button) findViewById(R.id.removeButton);
        Button back = (Button) findViewById(R.id.backButton);

        String keyword = getIntent().getStringExtra("keyword");
        String type = getIntent().getStringExtra("type");

        this.keywordLabel.setText(keyword);
        this.typeLabel.setText(type);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add remove method from database interface
                Intent keywordListIntent = new Intent(KeywordRemoveActivity.this, ViewKeywordsActivity.class);
                startActivity(keywordListIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keywordListIntent = new Intent(KeywordRemoveActivity.this, ViewKeywordsActivity.class);
                startActivity(keywordListIntent);
            }
        });

    }
}

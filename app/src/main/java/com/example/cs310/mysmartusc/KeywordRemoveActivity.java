package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class KeywordRemoveActivity extends Activity {
    private TextView keywordLabel, typeLabel;
    private DatabaseInterface db;
    String mUsername, keyword, type;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_keyword_activity);

        db = DatabaseInterface.getInstance(this);

        Account account = (Account) getIntent().getParcelableExtra("account");
        mUsername = account.name;

        this.keywordLabel = (TextView) findViewById(R.id.removeKeywordLabel);
        this.typeLabel = (TextView) findViewById(R.id.removeTypeLabel);

        Button remove = (Button) findViewById(R.id.removeButton);
        Button back = (Button) findViewById(R.id.backButton);

        keyword = getIntent().getStringExtra("keyword");
        type = getIntent().getStringExtra("type");

        this.keywordLabel.setText(keyword);
        this.typeLabel.setText(type);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeKeyword(mUsername, keyword);
                Intent keywordListIntent = new Intent(KeywordRemoveActivity.this, ViewKeywordsActivity.class);
                keywordListIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                startActivity(keywordListIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keywordListIntent = new Intent(KeywordRemoveActivity.this, ViewKeywordsActivity.class);
                keywordListIntent.putExtra("account", (Parcelable) getIntent().getParcelableExtra("account"));
                startActivity(keywordListIntent);
            }
        });

    }
}

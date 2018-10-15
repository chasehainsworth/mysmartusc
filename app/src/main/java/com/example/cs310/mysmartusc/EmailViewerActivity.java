package com.example.cs310.mysmartusc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmailViewerActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_viewer_activity);

        Button returnButton = (Button) findViewById(R.id.BackButton);
        TextView fromLabel = (TextView) findViewById(R.id.emailFromLabel);
        TextView subjectLabel = (TextView) findViewById(R.id.emailSubjectLabel);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}

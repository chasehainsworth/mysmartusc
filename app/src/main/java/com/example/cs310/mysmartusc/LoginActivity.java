package com.example.cs310.mysmartusc;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;

import java.util.Set;

public class LoginActivity extends Activity {
    Button submit;
    EditText username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        submit = (Button) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, 23);

    }

    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String ud = data.getStringExtra(AccountManager.KEY_USERDATA);
        username.setText(ud);
    }

}
package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.math.BigInteger;

import static android.content.ContentValues.TAG;

public class GmailWrapperService extends IntentService {

    static final String ACCOUNT_PARAM = "account";

    private GmailWrapper mWrapper;

    public GmailWrapperService() {
        super("Started");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mWrapper = new GmailWrapper(
                getApplicationContext(),
                (Account)intent.getParcelableExtra(ACCOUNT_PARAM));
//        mWrapper.fullSync();
        mWrapper.getStartHistoryId();
        try {
            while(true) {
                mWrapper.partialSync();
                Thread.sleep(6000);
            }
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    private BigInteger getHistoryId() {
        return mWrapper.getmHistoryId();
    }
}

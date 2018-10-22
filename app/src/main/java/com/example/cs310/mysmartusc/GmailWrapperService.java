package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.Serializable;
import java.math.BigInteger;

import static android.content.ContentValues.TAG;

public class GmailWrapperService extends IntentService {

    static final String ACCOUNT_PARAM = "account";
    private LocalBinder binder;
    private GmailWrapper mWrapper;

    public GmailWrapperService() {
        super("Started");
    }

    @Override
    public void onCreate() {
        Log.w(TAG, "On create!");
        super.onCreate();
        binder = new LocalBinder();
    }

    @Override
    public void onDestroy() {
//        startService(new Intent(this, GmailWrapperService.class));
        super.onDestroy();
        Log.w(TAG, "Service destroyed!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mWrapper = new GmailWrapper(
                getApplicationContext(),
                (Account)intent.getParcelableExtra(ACCOUNT_PARAM));
//        mWrapper.fullSync();
//        mWrapper.getStartHistoryId();
        try {
            while(true) {
                Log.w(TAG, "Partial sync!");
                mWrapper.partialSync();
                Thread.sleep(60000);
            }
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends android.os.Binder {
        public GmailWrapperService getService() {
            return GmailWrapperService.this;
        }
    }

    public void reloadKeywords() {
        mWrapper.reloadKeywords();
    }
    private BigInteger getHistoryId() {
        return mWrapper.getmHistoryId();
    }
}

package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class GmailWrapperService extends IntentService {

    static final String ACCOUNT_PARAM = "account";
//    private LocalBinder binder;
    private GmailWrapper mWrapper;

    public GmailWrapperService() {
        super("Started");
    }

    @Override
    public void onCreate() {
        Log.w(TAG, "On create!");
        super.onCreate();
//        binder = new LocalBinder();
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
                mWrapper.partialSync();
                if(mWrapper.getIsUrgentNotification()) {
                    launchNotification();
                    mWrapper.setUrgentNotification(false);
                }
                Thread.sleep(30000);
            }
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
    private void launchNotification() {
        Intent intent = new Intent(this, UrgentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "10")
                .setSmallIcon(R.drawable.sc)
                .setContentTitle("Urgent email")
                .setContentText("Check your urgent notifications to see!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(new Random().nextInt(), mBuilder.build());
    }
}

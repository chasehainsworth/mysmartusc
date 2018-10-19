package com.example.cs310.mysmartusc;

import android.app.IntentService;
import android.content.Intent;

public class GmailWrapperService extends IntentService {
    public GmailWrapperService() {
        super("Started");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
}

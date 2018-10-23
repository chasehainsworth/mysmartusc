package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
//import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.History;
import com.google.api.services.gmail.model.HistoryMessageAdded;
import com.google.api.services.gmail.model.ListHistoryResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Profile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

import static android.content.ContentValues.TAG;


public class GmailWrapper {

    // Scope for reading user's contacts
    private static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/gmail.readonly";

    // Global instance of the HTTP transport
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    // Global instance of the JSON factory
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Account mAccount;
    private Context mContext;

    private Filter mUrgentFilter;
    private Filter mSpamFilter;
    private Filter mSavedFilter;

    private DatabaseInterface mDatabaseInterface;
    private BigInteger mHistoryId;
    private boolean mIsUrgentNotification;

    public GmailWrapper(Context context, Account account) {
        mContext = context;
        mAccount = account;

        mDatabaseInterface = DatabaseInterface.getInstance(context);
        // filters need to be populated from database
        mUrgentFilter = new Filter("urgent", mDatabaseInterface);
        mSpamFilter = new Filter("spam", mDatabaseInterface);
        mSavedFilter = new Filter("saved", mDatabaseInterface);
        mIsUrgentNotification = true; // change to false
    }

    public void reloadKeywords() {
        mUrgentFilter = new Filter("urgent", mDatabaseInterface);
        mSpamFilter = new Filter("spam", mDatabaseInterface);
        mSavedFilter = new Filter("saved", mDatabaseInterface);
    }

    // Used when there is no prior HistoryId
    public void fullSync() {
        if (mAccount == null) {
            Log.w(TAG, "fullSync: null account");
            return;
        }
        List<Message> messages = listMessages(Long.valueOf("100000"));
        for (Message m : messages) {
            Message fullMessage = getMessage(m.getId());
            Email email = new Email(
                    getHeader(fullMessage, "Subject"),
                    getBody(fullMessage),
                    getHeader(fullMessage, "From"));
//            sortEmail(email);
            if (m.getHistoryId() == null || m.getHistoryId().compareTo(mHistoryId) > 0) {
                mHistoryId = m.getHistoryId();
                Log.w(TAG, mHistoryId.toString());
            }
        }
    }


    public void sortEmail(Email email) {
        reloadKeywords();
        boolean urgentResult = mUrgentFilter.sort(email);
        boolean spamResult = mSpamFilter.sort(email);
        boolean savedResult = mSavedFilter.sort(email);

        // If no filters are triggered
        if (!urgentResult && !spamResult && !savedResult) {
            Log.e("No filter","NO FILTER!");
            return;
        }
        // If only one filter is triggered
        //else if ( (urgentResult^spamResult^savedResult) && !(urgentResult&&spamResult&&savedResult) ) {
        else {
            if(urgentResult) {
                Log.w(TAG, email.getSubject() + " marked as urgent!");
                mIsUrgentNotification = true;
                mDatabaseInterface.addEmail(email, mAccount.name, "urgent");
            }
            else if(spamResult) {
                Log.w(TAG, email.getSubject() + " marked as spam!");
                mDatabaseInterface.addEmail(email, mAccount.name, "spam");
            }
            else {
                Log.w(TAG, email.getSubject() + " marked as saved!");
                mDatabaseInterface.addEmail(email, mAccount.name, "saved");
            }
        }

//        // If two+ filters triggered
//        else {
//            Log.e(TAG, "Email fit into 2+ categories");
//        }
    }
    public String getHeader(Message message, String name) {
        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            if(header.getName().equals(name)) {
                return header.getValue();
            }
        }
        return null;
    }

    public String getBody(Message message) {
        MessagePart payload = message.getPayload();
        if(payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                if(part.getMimeType().equals("text/plain")) {
                    return new String(Base64.decodeBase64(part.getBody().getData()));
                }
                else if (part.getParts() != null) {
                    for (MessagePart p : part.getParts()) {
                        if (part.getMimeType().equals("text/plain")) {
                            return new String(Base64.decodeBase64(part.getBody().getData()));
                        }
                    }
                }
            }
        }
        return message.getSnippet();

//        StringBuilder sb = new StringBuilder();
//        sb.append(message.getPayload().getParts().get(0).getBody().getData());
//        System.out.println("THINGY: " + new String (Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData().getBytes())));
//        return new String(Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData().getBytes()));
    }


    public Message getMessage(String messageId) {
        Log.w(TAG, "Getting Message ID: " + messageId);
        try {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            return mService
                    .users()
                    .messages()
                    .get(credential.getSelectedAccountName(), messageId)
                    .execute();
        } catch (IOException e) {
            Log.w(TAG, "getMessage:exception", e);
        }
        return null;
    }

    public List<Message> listHistory() {
        try {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            ListHistoryResponse response = mService
                    .users()
                    .history()
                    .list(credential.getSelectedAccountName())
                    .setStartHistoryId(mHistoryId)
                    .execute();
            List<Message> messages = new ArrayList<>();
            if (response.getHistory() != null) {
                for (History history : response.getHistory()) {
                    if (history.getMessagesAdded() != null) {
                        for (HistoryMessageAdded messageAdded : history.getMessagesAdded()) {
                            messages.add(messageAdded.getMessage());
                        }
                    }
                }
            }
            if(messages.size() > 0) mHistoryId = response.getHistoryId();
            return messages;
        } catch (IOException e) {
            Log.w(TAG, "listMessages:exception", e);
        }
        return null;
    }

    public void getStartHistoryId() {
        try {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            Profile profile = mService.users().getProfile(credential.getSelectedAccountName()).execute();
            mHistoryId = profile.getHistoryId();
        } catch (IOException e) {
            Log.w(TAG, "listMessages:exception", e);
        }
    }

    public List<Message> listMessages(Long maxResults) {
        try {
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            ListMessagesResponse response = mService
                    .users()
                    .messages()
                    .list(credential.getSelectedAccountName())
                    .setMaxResults(maxResults)
                    .execute();
            return response.getMessages();
        } catch (IOException e) {
            Log.w(TAG, "listMessages:exception", e);
        }
        return null;
    }

    // Used after a prior HistoryId has been recorded
    public void partialSync() {
        if (mAccount == null) {
            Log.w(TAG, "partialSync: null account");
            return;
        }
//        List<Message> messages = listHistory();
        List<Message> messages = listMessages(Long.valueOf("5"));
        if(messages == null)
        {
            Log.w(TAG, "partialSync: null messages");
            return;
        }
        else
        {
            for (Message m : messages) {
                Message fullMessage = getMessage(m.getId());
                if(fullMessage != null)
                {
                    Email email = new Email(
                            getHeader(fullMessage, "Subject"),
                            getBody(fullMessage),
                            getHeader(fullMessage, "From"));
                    Log.w(TAG, email.getSender());
                    Log.w(TAG, email.getBody());
                    sortEmail(email);
                }

            }
        }


    }

    public BigInteger getmHistoryId() {
        return mHistoryId;
    }

    public boolean getIsUrgentNotification() {
        return mIsUrgentNotification;
    }

    public void setUrgentNotification(boolean isUrgentNotification) {
        mIsUrgentNotification = isUrgentNotification;
    }
}

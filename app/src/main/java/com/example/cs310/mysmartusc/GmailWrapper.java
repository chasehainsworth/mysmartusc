package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.History;
import com.google.api.services.gmail.model.HistoryMessageAdded;
import com.google.api.services.gmail.model.ListHistoryResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Profile;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public GmailWrapper(Context context, Account account) {
        mContext = context;
        mAccount = account;

        mDatabaseInterface = new DatabaseInterface(context);
        // filters need to be populated from database
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
        List<Message> messages = listMessages();
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
        boolean urgentResult = mUrgentFilter.sort(email);
        boolean spamResult = mSpamFilter.sort(email);
        boolean savedResult = mSavedFilter.sort(email);

        // If no filters are triggered
        if (!urgentResult && !spamResult && !savedResult) {
            return;
        }
        // If only one filter is triggered
        else if ( (urgentResult^spamResult^savedResult) && !(urgentResult&&spamResult&&savedResult) ) {
            if(urgentResult) {
                mDatabaseInterface.addEmail(email, mAccount.name, "urgent");
            }
            else if(spamResult) {
                mDatabaseInterface.addEmail(email, mAccount.name, "spam");
            }
            else {
                mDatabaseInterface.addEmail(email, mAccount.name, "saved");
            }
        }

        // If two+ filters triggered
        else {
            Log.e(TAG, "Email fit into 2+ categories");
        }
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
        return message.getPayload().getBody().getData();
    }

    public Message getMessage(String messageId) {
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
            mHistoryId = response.getHistoryId();
            if (response.getHistory() != null) {
                for (History history : response.getHistory()) {
                    if (history != null) {
                        for (HistoryMessageAdded messageAdded : history.getMessagesAdded()) {
                            messages.add(messageAdded.getMessage());
                        }
                    }
                }
            }
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

    public List<Message> listMessages() {
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
        List<Message> messages = listHistory();
        for (Message m : messages) {
            Message fullMessage = getMessage(m.getId());
            Email email = new Email(
                    getHeader(fullMessage, "Subject"),
                    getBody(fullMessage),
                    getHeader(fullMessage, "From"));
            Log.w(TAG, email.getSender());
//            sortEmail(email);
//            System.out.println(m.getHistoryId());
//            if (m.getHistoryId().compareTo(mHistoryId) > 0) {
//                mHistoryId = m.getHistoryId();
//            }
        }
    }

    public BigInteger getmHistoryId() {
        return mHistoryId;
    }
}

package com.example.cs310.mysmartusc;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.ModifyMessageRequest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.ContentValues.TAG;


public class GmailWrapper {

    // Scope for reading user's contacts
//    private static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/gmail.readonly";
    private static final String EMAIL_SCOPE = "https://mail.google.com/";

    // Global instance of the HTTP transport
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    // Global instance of the JSON factory
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private Account mAccount;
    private Context mContext;

    private Filter mUrgentFilter;
    private Filter mSpamFilter;
    private Filter mSavedFilter;

    //For newsletters. Users cannot specify.
    private Filter mNewsFilter;

    private DatabaseInterface mDatabaseInterface;
    private boolean mIsUrgentNotification;

    private ArrayList<Email> allEmails = new ArrayList<>();

    public GmailWrapper() {}

    public Account getmAccount() {
        return mAccount;
    }

    public void setmAccount(Account mAccount) {
        this.mAccount = mAccount;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Filter getmUrgentFilter() {
        return mUrgentFilter;
    }

    public void setmUrgentFilter(Filter mUrgentFilter) {
        this.mUrgentFilter = mUrgentFilter;
    }

    public Filter getmSpamFilter() {
        return mSpamFilter;
    }

    public void setmSpamFilter(Filter mSpamFilter) {
        this.mSpamFilter = mSpamFilter;
    }

    public Filter getmSavedFilter() {
        return mSavedFilter;
    }

    public void setmSavedFilter(Filter mSavedFilter) {
        this.mSavedFilter = mSavedFilter;
    }

    public DatabaseInterface getmDatabaseInterface() {
        return mDatabaseInterface;
    }

    public void setmDatabaseInterface(DatabaseInterface mDatabaseInterface) {
        this.mDatabaseInterface = mDatabaseInterface;
    }

    public GmailWrapper(Context context, Account account) {
        mContext = context;
        mAccount = account;

        mDatabaseInterface = DatabaseInterface.getInstance(context);
        // filters need to be populated from database
        mUrgentFilter = new Filter("urgent", mDatabaseInterface);
        mSpamFilter = new Filter("spam", mDatabaseInterface);
        mSavedFilter = new Filter("saved", mDatabaseInterface);

        //For newsletters
        mNewsFilter = new Filter("news", mDatabaseInterface);
        mIsUrgentNotification = false;
    }

    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public void sendEmail(String subject, String to, String from, String body) {
        Log.w(TAG, "Sending message with subject: " + subject + " To: " + to);
        try {
            MimeMessage mimeMessage = createEmail(to, from, subject, body);
            Message message = createMessageWithEmail(mimeMessage);
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            mService.users().messages().send(mAccount.name, message).execute();
        } catch (IOException e) {
            Log.w(TAG, "sendMessage:exception", e);
        } catch (MessagingException e) {
            Log.w(TAG, "sendMessage:MessagingException", e);
        }
    }

    public void markEmailAsRead(String messageID) {
        try {
            ModifyMessageRequest mods = new ModifyMessageRequest().setRemoveLabelIds(Arrays.asList("UNREAD"));
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            mService.users().messages().modify(mAccount.name, messageID, mods).execute();
        } catch (IOException e) {
            Log.w(TAG, "markEmailAsRead:exception", e);
        }
    }

    public void markEmailAsSpam(String messageID) {
        try {
            ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(Arrays.asList("SPAM"));
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                    mContext,
                    Collections.singleton(EMAIL_SCOPE));
            credential.setSelectedAccount(mAccount);

            Gmail mService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("MySmartUSC")
                    .build();
            mService.users().messages().modify(mAccount.name, messageID, mods).execute();
        } catch (IOException e) {
            Log.w(TAG, "markEmailAsSpam:exception", e);
        }
    }

    public void reloadKeywords() {
        mUrgentFilter.refreshKeywords();
        mSpamFilter.refreshKeywords();
        mSavedFilter.refreshKeywords();
        mNewsFilter.refreshKeywords();
    }

    private boolean containsEmail(Email email){
        for(Email e : allEmails){
            if(e.getSubject().equals(email.getSubject())){
                if(e.getSender().equals(email.getSender())){
                    return true;
                }
            }
        }

        return false;
    }

    public void sortEmail(Email email, String messageID) {
        reloadKeywords();


        boolean urgentResult = mUrgentFilter.sort(email);
        boolean spamResult = mSpamFilter.sort(email);
        boolean savedResult = mSavedFilter.sort(email);
        boolean newsResult = mNewsFilter.sort(email);

        // If no filters are triggered
        if (!urgentResult && !spamResult && !savedResult && !newsResult) {
            Log.e("GMAILWRAPPER", "EMAIL NOT SORTED");
            markEmailAsRead(messageID);
        }
        // If only one filter is triggered
        //else if ( (urgentResult^spamResult^savedResult) && !(urgentResult&&spamResult&&savedResult) ) {
        else {
            if(urgentResult) {
                Log.w(TAG, email.getSubject() + " marked as urgent!");
                mIsUrgentNotification = true;
                mDatabaseInterface.addEmail(email, mAccount.name, "urgent", messageID);
            }
            if(spamResult) {
                Log.w(TAG, email.getSubject() + " marked as spam!");
                mDatabaseInterface.addEmail(email, mAccount.name, "spam", messageID);
                markEmailAsSpam(messageID);
            }
            if(savedResult){
                Log.w(TAG, email.getSubject() + " marked as saved!");
                mDatabaseInterface.addEmail(email, mAccount.name, "saved", messageID);
            }
            if(newsResult){
                Log.w(TAG, email.getSubject() + " marked as a newsletter!");
                mDatabaseInterface.addEmail(email, mAccount.name, "news", messageID);
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
        if (payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                if (part.getMimeType().equals("text/plain")) {
                    return new String(Base64.decodeBase64(part.getBody().getData()));
                } else if (part.getParts() != null) {
                    for (MessagePart p : part.getParts()) {
                        if (part.getMimeType().equals("text/plain")) {
                            return new String(Base64.decodeBase64(part.getBody().getData()));
                        }
                    }
                }
            }
        }
        return message.getSnippet();
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

                // if an Email with that id already exists in the database, do nothing (to avoid duplicates)
                // if no Email in the database exists with that ID, send it to be sorted
                boolean idExists = mDatabaseInterface.checkMessageID(m.getId());
                if (idExists) {
                    continue;
                }

                
                Message fullMessage = getMessage(m.getId());
                
                if(fullMessage != null)
                {
                    Log.e("GmailWrapper", "HistoryID = " + fullMessage.getHistoryId());
                    Email email = new Email(
                            getHeader(fullMessage, "Subject"),
                            getBody(fullMessage),
                            getHeader(fullMessage, "From"),
                            fullMessage.getInternalDate(),
                            false);
                    Log.w(TAG, email.getSender());
                    Log.w(TAG, email.getBody());
                    sortEmail(email, m.getId());
                }

            }
        }


    }

    public boolean getIsUrgentNotification() {
        return mIsUrgentNotification;
    }

    public void setUrgentNotification(boolean isUrgentNotification) {
        mIsUrgentNotification = isUrgentNotification;
    }
}

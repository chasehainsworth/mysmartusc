import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.cs310.mysmartusc.DatabaseInterface;
import com.example.cs310.mysmartusc.Email;
import com.example.cs310.mysmartusc.Filter;
import com.example.cs310.mysmartusc.GmailWrapper;
import com.example.cs310.mysmartusc.R;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static android.util.Base64.encode;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class TestDatabaseInterface {
    private static final String FAKE_FROM_HEADER = "from_value";
    private static final String FAKE_SUBJECT_HEADER = "subject_value";
    private static final String FAKE_BODY = "body_value";
    private static final String FAKE_MESSAGE_ID = "msg_id";

    private GmailWrapper mWrapperSpy;
    private GmailWrapper mWrapper;

    @Mock
    private DatabaseInterface mMockDatabaseInterface;

    @Mock
    private Account mMockAccount;

    @Mock
    private Context mMockContext;

    @Mock
    private Filter mMockUrgentFilter;

    @Mock
    private Filter mMockSavedFilter;

    @Mock
    private Filter mMockSpamFilter;

    @Before
    public void setup() {
        mWrapper = Mockito.spy(new GmailWrapper());
//        mWrapperSpy = spy(mWrapper);
        mWrapper.setmAccount(mMockAccount);
        mWrapper.setmContext(mMockContext);
        mWrapper.setmUrgentFilter(mMockUrgentFilter);
        mWrapper.setmSavedFilter(mMockSavedFilter);
        mWrapper.setmSpamFilter(mMockSpamFilter);
        mWrapper.setmDatabaseInterface(mMockDatabaseInterface);
    }

    @Test
    public void getEmailsByType() {

    }

    @Test
    public void addGetKeyword() {
        mMockDatabaseInterface.addKeyword("free", "urgent", "psyhogeo@usc.edu", "body");
        mMockDatabaseInterface.addKeyword("immediate", "urgent", "daltonb@usc.edu", "subject");
        mMockDatabaseInterface.addKeyword("immediate", "urgent", "daltonb@usc.edu", "subject");
        mMockDatabaseInterface.addKeyword("overseas", "spam", "anaypate@usc.edu", "body");
        mMockDatabaseInterface.addKeyword("abroad", "spam", "hainswor@usc.edu", "subject");
        mMockDatabaseInterface.addKeyword("advisor", "saved", "alexangy@usc.edu", "body");

        Cursor cUrgBod = mMockDatabaseInterface.getKeywordsByType("urgent", "body");    // 1
        Cursor cUrgSub = mMockDatabaseInterface.getKeywordsByType("urgent", "subject"); // 2
        Cursor cUrgSen = mMockDatabaseInterface.getKeywordsByType("urgent", "sender");  // 0
        Cursor cSpamBod = mMockDatabaseInterface.getKeywordsByType("spam", "body");     // 1
        Cursor cSpamSub = mMockDatabaseInterface.getKeywordsByType("spam", "subject");  // 1
        Cursor cSpamSen = mMockDatabaseInterface.getKeywordsByType("spam", "sender");   // 0
        Cursor cSavBod = mMockDatabaseInterface.getKeywordsByType("saved", "body");     // 1
        Cursor cSavSub = mMockDatabaseInterface.getKeywordsByType("saved", "subject");  // 0
        Cursor cSavSen = mMockDatabaseInterface.getKeywordsByType("saved", "sender");   // 0

        assertEquals(cUrgBod.getCount(), 1);
        assertEquals(cUrgSub.getCount(), 2);
        assertEquals(cUrgSen.getCount(), 0);
        assertEquals(cSpamBod.getCount(), 1);
        assertEquals(cSpamSub.getCount(), 1);
        assertEquals(cSpamSen.getCount(), 0);
        assertEquals(cSavBod.getCount(), 1);
        assertEquals(cSavSub.getCount(), 0);
        assertEquals(cSavSen.getCount(), 0);

    }

}

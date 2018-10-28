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
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class TestGmailWrapper {
    private static final String FAKE_FROM_HEADER = "from_value";
    private static final String FAKE_SUBJECT_HEADER = "subject_value";
    private static final String FAKE_BODY = "body_value";
    private static final String FAKE_MESSAGE_ID = "msg_id";

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
        mWrapper = new GmailWrapper();
        mWrapper.setmAccount(mMockAccount);
        mWrapper.setmContext(mMockContext);
        mWrapper.setmUrgentFilter(mMockUrgentFilter);
        mWrapper.setmSavedFilter(mMockSavedFilter);
        mWrapper.setmSpamFilter(mMockSpamFilter);
        mWrapper.setmDatabaseInterface(mMockDatabaseInterface);
    }

    @Test
    public void fromHeader() {
        Message message = new Message();
        MessagePartHeader from = new MessagePartHeader();
        from.setName("From");
        from.setValue(FAKE_FROM_HEADER);
        MessagePart part = new MessagePart();
        MessagePartBody partBody = new MessagePartBody();
        partBody.setData("empty");
        List<MessagePartHeader> headers = Arrays.asList(from);
        part.setBody(partBody);
        part.setHeaders(headers);
        message.setPayload(part);

        String result = mWrapper.getHeader(message, "From");
        assertEquals(result, FAKE_FROM_HEADER);
    }

    @Test
    public void subjectHeader() {
        Message message = new Message();
        MessagePartHeader from = new MessagePartHeader();
        from.setName("Subject");
        from.setValue(FAKE_SUBJECT_HEADER);
        MessagePart part = new MessagePart();
        MessagePartBody partBody = new MessagePartBody();
        partBody.setData("empty");
        List<MessagePartHeader> headers = Arrays.asList(from);
        part.setBody(partBody);
        part.setHeaders(headers);
        message.setPayload(part);

        String result = mWrapper.getHeader(message, "Subject");
        assertEquals(result, FAKE_SUBJECT_HEADER);
    }

    @Test
    public void sortNoMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(false);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(false);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(false);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(0)).addEmail(ArgumentMatchers.any(), ArgumentMatchers.any(), anyString(), ArgumentMatchers.any());
    }

    @Test
    public void sortOneMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(false);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(false);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(1)).addEmail(ArgumentMatchers.any(), ArgumentMatchers.any(), anyString(), ArgumentMatchers.any());
    }

    @Test
    public void sortTwoMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(false);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(2)).addEmail(ArgumentMatchers.any(), ArgumentMatchers.any(), anyString(), ArgumentMatchers.any());
    }

    @Test
    public void sortThreeMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(true);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(3)).addEmail(ArgumentMatchers.any(), ArgumentMatchers.any(), anyString(), ArgumentMatchers.any());
    }

}

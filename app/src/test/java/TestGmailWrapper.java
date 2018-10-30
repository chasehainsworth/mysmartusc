import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.cs310.mysmartusc.DatabaseInterface;
import com.example.cs310.mysmartusc.Email;
import com.example.cs310.mysmartusc.Filter;
import com.example.cs310.mysmartusc.GmailWrapper;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestGmailWrapper {
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
        verify(mMockDatabaseInterface, times(0)).addEmail(any(Email.class), anyString(), anyString(), anyString());
//        verify(mMockDatabaseInterface, times(0)).addEmail(any(), any(), anyString(), any());
    }

    @Test
    public void sortOneMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(false);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(false);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(1)).addEmail(any(Email.class), anyString(), anyString(), anyString());
    }

    @Test
    public void sortTwoMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(false);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(2)).addEmail(any(Email.class), anyString(), anyString(), anyString());
    }

    @Test
    public void sortThreeMatch() {
        Email mockEmail = mock(Email.class);
        when(mMockUrgentFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSavedFilter.sort(mockEmail)).thenReturn(true);
        when(mMockSpamFilter.sort(mockEmail)).thenReturn(true);
        mWrapper.sortEmail(mockEmail, FAKE_MESSAGE_ID);
        verify(mMockDatabaseInterface, times(3)).addEmail(any(Email.class), anyString(), anyString(), anyString());
    }

    @Test
    public void partialSyncDuplicateEmail() {
        Message message = new Message();
        message.setId(FAKE_MESSAGE_ID);
        doReturn(Arrays.asList(message)).when(mWrapper).listMessages(anyLong());
        when(mMockDatabaseInterface.checkMessageID(FAKE_MESSAGE_ID)).thenReturn(true);
        mWrapper.partialSync();
        verify(mWrapper, times(0)).getMessage(FAKE_MESSAGE_ID);
    }

    @Test
    public void partialSyncNewEmail() {
        Message message = new Message();
        message.setId(FAKE_MESSAGE_ID);
        doReturn(Arrays.asList(message)).when(mWrapper).listMessages(anyLong());
        when(mMockDatabaseInterface.checkMessageID(FAKE_MESSAGE_ID)).thenReturn(false);
        doReturn(message).when(mWrapper).getMessage(FAKE_MESSAGE_ID);
        doReturn(FAKE_SUBJECT_HEADER).when(mWrapper).getHeader(any(Message.class), anyString());
        doReturn(FAKE_BODY).when(mWrapper).getBody(any(Message.class));
        mWrapper.partialSync();
        verify(mWrapper, times(1)).sortEmail(any(Email.class), anyString());
    }

    @Test
    public void getBodyTest(){
        String body = "message body";

    }

}

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.cs310.mysmartusc.DatabaseInterface;
import com.example.cs310.mysmartusc.Email;
import com.example.cs310.mysmartusc.Filter;
import com.example.cs310.mysmartusc.GmailWrapper;
import com.example.cs310.mysmartusc.R;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestGmailWrapper {
    private static final String FAKE_FROM_HEADER = "from_value";
    private static final String FAKE_SUBJECT_HEADER = "subject_value";

    private GmailWrapper mWrapper;

    @Mock
    private DatabaseInterface mMockDatabaseInterface;

    @Mock
    private Account mMockAccount;

    @Mock
    private Context mMockContext;

    @Mock
    private Filter mUrgentFilter;

    @Mock
    private Filter mSavedFilter;

    @Mock
    private Filter mSpamFilter;

    @Before
    public void setup() {
        mWrapper = new GmailWrapper();
        mWrapper.setmAccount(mMockAccount);
        mWrapper.setmContext(mMockContext);
        mWrapper.setmUrgentFilter(mUrgentFilter);
        mWrapper.setmSavedFilter(mSavedFilter);
        mWrapper.setmSpamFilter(mSpamFilter);
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
}

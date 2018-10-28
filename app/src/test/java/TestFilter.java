import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.cs310.mysmartusc.DatabaseInterface;
import com.example.cs310.mysmartusc.Email;
import com.example.cs310.mysmartusc.Filter;
import com.example.cs310.mysmartusc.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestFilter {

    private static final String URGENT_TYPE = "urgent";
    private static final String SAVED_TYPE = "saved";
    private static final String SPAM_TYPE = "spam";
    private static final String KEYWORD = "keyword";

    @Test
    public void urgentSubject() {
        Email email = new Email(KEYWORD, "empty", "test@gmail.com");

        // Mock cursor to be returned for Body & Sender
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Subject. Each method call needs to be mocked to avoid exceptions
        Cursor subjectCursor = mock(Cursor.class);
        when(subjectCursor.moveToFirst()).thenReturn(true);
        when(subjectCursor.getString(1)).thenReturn(KEYWORD);
        when(subjectCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Subject")).thenReturn(subjectCursor);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter(URGENT_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }


    @Test
    public void urgentBody() {
        Email email = new Email("empty", KEYWORD, "test@gmail.com");

        // Mock cursor to be returned for Subject & Sender
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Body. Each method call needs to be mocked to avoid exceptions
        Cursor bodyCursor = mock(Cursor.class);
        when(bodyCursor.moveToFirst()).thenReturn(true);
        when(bodyCursor.getString(1)).thenReturn(KEYWORD);
        when(bodyCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Subject")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Body")).thenReturn(bodyCursor);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter(URGENT_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }

    @Test
    public void urgentSender() {
        Email email = new Email("empty", "empty", KEYWORD + "@gmail.com" );

        // Mock cursor to be returned for Body & Subject
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Sender. Each method call needs to be mocked to avoid exceptions
        Cursor senderCursor = mock(Cursor.class);
        when(senderCursor.moveToFirst()).thenReturn(true);
        when(senderCursor.getString(1)).thenReturn(KEYWORD);
        when(senderCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Subject")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(URGENT_TYPE, "Sender")).thenReturn(senderCursor);

        Filter filter = new Filter(URGENT_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }

    @Test
    public void savedSubject() {
        Email email = new Email(KEYWORD, "empty", "test@gmail.com");

        // Mock cursor to be returned for Body & Sender
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Subject. Each method call needs to be mocked to avoid exceptions
        Cursor subjectCursor = mock(Cursor.class);
        when(subjectCursor.moveToFirst()).thenReturn(true);
        when(subjectCursor.getString(1)).thenReturn(KEYWORD);
        when(subjectCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Subject")).thenReturn(subjectCursor);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter(SAVED_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }


    @Test
    public void savedBody() {
        Email email = new Email("empty", KEYWORD, "test@gmail.com");

        // Mock cursor to be returned for Body & Sender
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Body. Each method call needs to be mocked to avoid exceptions
        Cursor bodyCursor = mock(Cursor.class);
        when(bodyCursor.moveToFirst()).thenReturn(true);
        when(bodyCursor.getString(1)).thenReturn(KEYWORD);
        when(bodyCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Subject")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Body")).thenReturn(bodyCursor);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter(SAVED_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }

    @Test
    public void savedSender() {
        Email email = new Email("empty", "empty", KEYWORD + "@gmail.com");

        // Mock cursor to be returned for Body & Subject
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Sender. Each method call needs to be mocked to avoid exceptions
        Cursor senderCursor = mock(Cursor.class);
        when(senderCursor.moveToFirst()).thenReturn(true);
        when(senderCursor.getString(1)).thenReturn(KEYWORD);
        when(senderCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Subject")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SAVED_TYPE, "Sender")).thenReturn(senderCursor);

        Filter filter = new Filter(SAVED_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }

    @Test
    public void spamSubject() {
        Email email = new Email(KEYWORD, "empty", "test@gmail.com");

        // Mock cursor to be returned for Body & Sender
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Subject. Each method call needs to be mocked to avoid exceptions
        Cursor subjectCursor = mock(Cursor.class);
        when(subjectCursor.moveToFirst()).thenReturn(true);
        when(subjectCursor.getString(1)).thenReturn(KEYWORD);
        when(subjectCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Subject")).thenReturn(subjectCursor);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter(SPAM_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }

    @Test
    public void spamBody() {
        Email email = new Email("empty", KEYWORD, "test@gmail.com");

        // Mock cursor to be returned for Body & Sender
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Body. Each method call needs to be mocked to avoid exceptions
        Cursor bodyCursor = mock(Cursor.class);
        when(bodyCursor.moveToFirst()).thenReturn(true);
        when(bodyCursor.getString(1)).thenReturn(KEYWORD);
        when(bodyCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Subject")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Body")).thenReturn(bodyCursor);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter(SPAM_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }

    @Test
    public void spamSender() {
        Email email = new Email("empty", "empty", KEYWORD + "@gmail.com");

        // Mock cursor to be returned for Body & Subject
        Cursor emptyCursor = mock(Cursor.class);
        when(emptyCursor.moveToFirst()).thenReturn(false);

        // Mock cursor for Sender. Each method call needs to be mocked to avoid exceptions
        Cursor senderCursor = mock(Cursor.class);
        when(senderCursor.moveToFirst()).thenReturn(true);
        when(senderCursor.getString(1)).thenReturn(KEYWORD);
        when(senderCursor.moveToNext()).thenReturn(false);

        // Mock DatabaseInterface to return correct cursor for each call to getKeywordsByType
        DatabaseInterface mockDB = mock(DatabaseInterface.class);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Subject")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType(SPAM_TYPE, "Sender")).thenReturn(senderCursor);

        Filter filter = new Filter(SPAM_TYPE, mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }
}

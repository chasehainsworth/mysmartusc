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
        when(mockDB.getKeywordsByType("urgent", "Subject")).thenReturn(subjectCursor);
        when(mockDB.getKeywordsByType("urgent", "Body")).thenReturn(emptyCursor);
        when(mockDB.getKeywordsByType("urgent", "Sender")).thenReturn(emptyCursor);

        Filter filter = new Filter("urgent", mockDB);
        boolean result = filter.sort(email);
        assert(result);
    }
}

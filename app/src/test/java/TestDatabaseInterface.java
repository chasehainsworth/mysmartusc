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
    public void addGetEmail() {
        Email e01 = new Email("free food",
                "come for free food on the viterbi e-quad today at 7pm",
                "csdept@usc.edu");
        Email e02 = new Email("cd phd opportunities at USC",
                "we would really appreciate it if you could encourage exceptionally talented CS majors" +
                        " from your program to apply to our Ph.D. program",
                "csdept@usc.edu");
        Email e03 = new Email("black-box testing",
                "your classmate posted a new question on Piazza",
                "no-reply@piazza.com");
        Email e04 = new Email("SMNT happy hour on wednesday, november 7",
                "Free Event. Cash Bar. Happy Hour Menu till 7:30PM",
                "info@meetup.com");
        Email e05 = new Email("layer up with new men's Nike",
                "new jerseys on sale at the bookstore",
                "customerservice@uscbookstore.com");
        Email e06 = new Email("what's new with Alexa?",
                "I've got some tricks and treats. Just ask",
                "store_news@amazon.com");
        Email e07 = new Email("success networking teams fall 2018 are here",
                "if you signed up for a team, you have been assigned",
                "no-reply@nsls.com");
        Email e08 = new Email("discussion section readings and group presentations",
                "Please go to the Content section on Blackboard for the reading assignment for next" +
                        " week's discussion sections",
                "do-not-reply@blackboard.com");
        Email e09 = new Email("What is the best math trick you have ever learned?",
                "Instead, I reversed the calculation. Because 8 % of 50 is the same as 50 % of 8. Which " +
                        "(for most people) is a much more simple calculation to make in your head.",
                "digest-noreply@quora.com");
        Email e10 = new Email("USC - class registration confirmation",
                "Thank you for using the Web Registration system.",
                "onestop@usc.edu");

        mMockDatabaseInterface.addEmail(e01, "psyhogeo@usc.edu", "urgent", "01");
        mMockDatabaseInterface.addEmail(e02, "psyhogeo@usc.edu", "saved", "02");
        mMockDatabaseInterface.addEmail(e03, "psyhogeo@usc.edu", "spam", "03");
        mMockDatabaseInterface.addEmail(e04, "psyhogeo@usc.edu", "saved", "04");
        mMockDatabaseInterface.addEmail(e05, "psyhogeo@usc.edu", "spam", "05");
        mMockDatabaseInterface.addEmail(e06, "psyhogeo@usc.edu", "spam", "06");
        mMockDatabaseInterface.addEmail(e07, "psyhogeo@usc.edu", "spam", "07");
        mMockDatabaseInterface.addEmail(e08, "psyhogeo@usc.edu", "urgent", "08");
        mMockDatabaseInterface.addEmail(e09, "psyhogeo@usc.edu", "saved", "09");
        mMockDatabaseInterface.addEmail(e10, "psyhogeo@usc.edu", "urgent", "10");

        Cursor cUrg = mMockDatabaseInterface.getEmailByType("psyhogeo@usc.edu", "urgent");
        Cursor cSpam = mMockDatabaseInterface.getEmailByType("psyhogeo@usc.edu", "spam");
        Cursor cSav = mMockDatabaseInterface.getEmailByType("psyhogeo@usc.edu", "saved");

        assertEquals(cUrg.getCount(), 3);
        assertEquals(cUrg.getCount(), 4);
        assertEquals(cUrg.getCount(), 3);

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

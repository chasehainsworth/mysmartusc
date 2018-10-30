import android.provider.ContactsContract;

import com.example.cs310.mysmartusc.DatabaseInterface;
import com.example.cs310.mysmartusc.Email;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DatabaseInterfaceTest {
    @Test
    public void testDatabaseInit(){
        DatabaseInterface db = DatabaseInterface.getInstance(RuntimeEnvironment.application);
        String[] tables = {"Users", "Keywords", "Emails"};

        for(String table : tables){
            assertTrue(db.tableExists(table));
        }

        db.close();
    }

    @Test
    public void databaseSingletonTest(){
        DatabaseInterface db1 = DatabaseInterface.getInstance(RuntimeEnvironment.application);
        DatabaseInterface db2 = DatabaseInterface.getInstance(RuntimeEnvironment.application);

        db1.addUser("brandon@govy.com");
        assertTrue(db2.userExists("brandon", "govy.com"));

        db1.close();
        db2.close();
    }

    @Test
    public void addUserTest(){
        DatabaseInterface db = DatabaseInterface.getInstance(RuntimeEnvironment.application);

        assertTrue(db.addUser("brandon@govy.com"));
        db.close();
    }

    @Test
    public void addEmailTest() {
        DatabaseInterface db = DatabaseInterface.getInstance(RuntimeEnvironment.application);
        db.addUser("brandon@govy.edu");

        Email e01 = new Email("free food",
                "come for free food on the viterbi e-quad today at 7pm",
                "csdept@usc.edu");
        assertTrue(db.addEmail(e01, "brandon@govy.edu", "urgent", "01"));
        db.close();
    }

    @Test
    public void getEmailsByType() {
        DatabaseInterface db = DatabaseInterface.getInstance(RuntimeEnvironment.application);
        db.addUser("brandon@govy.edu");

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

        db.addEmail(e01, "brandon@govy.edu", "urgent", "01");
        db.addEmail(e02, "brandon@govy.edu", "saved", "02");
        db.addEmail(e03, "brandon@govy.edu", "spam", "03");
        db.addEmail(e04, "brandon@govy.edu", "saved", "04");
        db.addEmail(e05, "brandon@govy.edu", "spam", "05");
        db.addEmail(e06, "brandon@govy.edu", "spam", "06");

        assertEquals(db.getEmailByType("brandon@govy.edu", "urgent").getCount(), 1);
        assertEquals(db.getEmailByType("brandon@govy.edu", "spam").getCount(), 3);
        assertEquals(db.getEmailByType("brandon@govy.edu", "saved").getCount(), 2);

        db.close();
    }

    @Test
    public void addKeywordTest() {
        DatabaseInterface db = DatabaseInterface.getInstance(RuntimeEnvironment.application);
        db.addUser("brandon@govy.edu");
        assertTrue(db.addKeyword("free", "urgent", "brandon@govy.edu", "body"));
        db.close();
    }

    @Test
    public void getKeywordTest() {
        DatabaseInterface db = DatabaseInterface.getInstance(RuntimeEnvironment.application);
        db.addUser("brandon@govy.edu");

        db.addKeyword("immediate", "urgent", "brandon@govy.edu", "subject");
        db.addKeyword("immediate", "urgent", "brandon@govy.edu", "subject");
        db.addKeyword("overseas", "spam", "brandon@govy.edu", "body");
        db.addKeyword("abroad", "spam", "brandon@govy.edu", "subject");
        db.addKeyword("advisor", "saved", "brandon@govy.edu", "body");

        assertEquals(db.getKeywordsByType("urgent", "subject").getCount(), 2);
        assertEquals(db.getKeywordsByType("spam", "body").getCount(), 1);
        assertEquals(db.getKeywordsByType("saved", "body").getCount(), 1);

        db.close();
    }
}

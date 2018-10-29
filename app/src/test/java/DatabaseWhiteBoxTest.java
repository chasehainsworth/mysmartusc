import android.provider.ContactsContract;

import com.example.cs310.mysmartusc.DatabaseInterface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.TestCase.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class DatabaseWhiteBoxTest {
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
}

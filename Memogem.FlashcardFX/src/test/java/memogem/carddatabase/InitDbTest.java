package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import memogem.carddatabase.InitDb;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class InitDbTest {
    private String conUnavailable;
    private Statement statement; 
    
    public InitDbTest() {
        statement = null;
        conUnavailable = null;
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        conUnavailable = null;
        InitDb initdb = new InitDb("InitDb.db");
        try {
            Connection connection = DriverManager.getConnection("InitDb.db");
            statement = connection.createStatement();
            
        } catch (SQLException ex) {
            Logger.getLogger(InitDbTest.class.getName()).log(Level.SEVERE, null, ex);
            conUnavailable = ex.getMessage();
        }
    }
    
    @After
    public void tearDown() {
        File f = new File("InitDb.db");
        boolean success = f.delete();
    }
    
    @Test
    public void testingDatabaseCreation() {
        try {
            ResultSet res1 = statement.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='Card';");
            String table = res1.getString(0);
            assertEquals(res1, table);
        } catch (SQLException ex) {
            Logger.getLogger(InitDbTest.class.getName()).log(Level.SEVERE, "statement didn't work properly", ex);
        }
        
    }
    
    @Test
    public void testingConnectionCreation() {
        assertNull("No connection available. Database not found.", conUnavailable);
    }
    
    @Test
    public void testingThatNoCardsExist() {
        try {
            
            ResultSet res1 = statement.executeQuery("SELECT * FROM Card;");
            String table = res1.getString(0);
            assertEquals(table, "");
        } catch (SQLException ex) {
            Logger.getLogger(InitDbTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
            
            
}

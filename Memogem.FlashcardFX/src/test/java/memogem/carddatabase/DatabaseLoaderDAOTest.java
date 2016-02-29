
package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class DatabaseLoaderDAOTest {
    private InitDb initDb;
    private String testDatabase;
    private Connection dbConnection;
    
    public DatabaseLoaderDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testDatabase = "testingDatabase.db";
        initDb = new InitDb(testDatabase);
        try {
            dbConnection = DriverManager.getConnection("jcdb:sqlite:" + testDatabase);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection.");;
        }
        File f = new File(testDatabase);
        boolean success = f.delete();
        
    }
    
    @Test
    public void testTheDatabaseCreationofTableCard() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeQuery("SELECT * FROM Card;");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void testTheDatabaseCreationofTableTag() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeQuery("SELECT * FROM Tag;");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void testTheDatabaseCreationofTableCardTag() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeQuery("SELECT * FROM CardTag;");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void testTheDatabaseCreationofTableSet() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeQuery("SELECT * FROM CardSet;");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void testTheDatabaseCreationofTableStudySpeed() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeQuery("SELECT * FROM StudySpeed;");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
    
    
    
}

package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private InitDb initDb;
    private String testDatabase;
    private Connection dbConnection;
    private DateTimeFormatter formatter;
    
    public InitDbTest() {
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
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + testDatabase);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
        try {
            File f = new File(testDatabase);
            boolean success = f.delete();
        } catch (Exception e) {
            System.out.println("No database of name '" + testDatabase + "' found when trying to delete the testDatabase.");
        }
    }
    
    @Test
    public void testTheDatabaseCreationofTableCard() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeQuery("SELECT * FROM Card;");
            stmt.close();
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
            stmt.close();
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
            stmt.close();
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
            stmt.close();
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
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLoaderDAOTest.class.getName()).log(Level.SEVERE, null, ex);
            assertTrue(false);
        }
    }
    
    @Test
    public void testTheDatabaseWithOneCardSet() {
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate("INSERT INTO CardSet (id, name) "
                    + "VALUES ('01ar', 'Physiology');");
            ResultSet rs = stmt.executeQuery("SELECT * FROM CardSet;");
            assertEquals(rs.getString("name"), "Physiology");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + " : CardSet query didn't work.");
            assertTrue(false);
        }
    }
    
    @Test
    public void testTheDatabaseWithTwoCardSetsFirst() {
        try {
            Statement stmt = dbConnection.createStatement();
            // try with this one
            String cardSetDateTime = "2008-12-26 12:05:05";
            LocalDateTime locDT = LocalDateTime.parse(cardSetDateTime, formatter);
            stmt.executeUpdate("INSERT INTO CardSet (id, name) "
                    + "VALUES ('01ar', 'Physiology');");
            stmt.executeUpdate("INSERT INTO CardSet (id, name) "
                    + "VALUES ('01aq', 'Hierarchy for Heroes');");
            ResultSet rs = stmt.executeQuery("SELECT * FROM CardSet ORDER BY name DESC;");
            assertEquals(rs.getString("name"), "Physiology");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + " : CardSet second query didn't work.");
            assertTrue(false);
        }
    }
    
     @Test
    public void testTheDatabaseWithTwoCardSetsLatter() {
        try {
            Statement stmt = dbConnection.createStatement();
            // try with this one
            stmt.executeUpdate("INSERT INTO CardSet (id, name) "
                    + "VALUES ('01ar', 'Physiology');");
            stmt.executeUpdate("INSERT INTO CardSet (id, name) "
                    + "VALUES ('01aq', 'Hierarchy for Heroes');");
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM CardSet ORDER BY name DESC;");
            rs.next();
            assertEquals(rs.getString("name"), "Physiology");
            rs.next();
            assertEquals(rs.getString("id"), "01aq");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + " : CardSet third query didn't work.");
            assertTrue(false);
        }
    }
    
    
            
}

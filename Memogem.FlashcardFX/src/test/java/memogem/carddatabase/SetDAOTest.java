
package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import memogem.coreapplication.Card;
import memogem.coreapplication.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class SetDAOTest {
    private String testDBname;
    private Connection dbConnection;
    private DateTimeFormatter formatter;
    private CardDAO cD;
    private Set testset;
    private InitDb initDb;
    
    public SetDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testDBname = "testingDatabase.db";
        testset = new Set("TestSet");
        initDb = new InitDb(testDBname);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + testDBname + "");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
//        String cardSetDateTime = "2008-12-26 12:05:05";
//        LocalDateTime locDT = LocalDateTime.parse(cardSetDateTime, formatter);
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
            File f = new File(testDBname);
            boolean success = f.delete();
        } catch (Exception e) {
            System.out.println("No database of name '" + testDBname + "' found when trying to delete the testDatabase.");
        }
    }
    
    @Test
    public void addNewSet() {
        Statement stmt = null;
        ResultSet rs = null;
        SetDAO sD = new SetDAO(testDBname);
        try {
            sD.add(testset);
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void addNewSetAndQuery() {
        Statement stmt = null;
        ResultSet rs = null;
        SetDAO sD = new SetDAO(testDBname);
        Set set2 = new Set("Randomizer2");
        try {
            sD.add(set2);
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet;");
            assertEquals(rs.getString("Name"), "Randomizer2");
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void addNewSetAndDelete() {
        SetDAO sD = new SetDAO(testDBname);
        Statement stmt = null;
        ResultSet rs = null;
        Set set1 = new Set("Randomizer1");
        try {
            sD.add(set1);
            sD.delete(set1);
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet;");
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "",true);
        }
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    public void closeStmtAndResultSet(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException ex) {
            assertFalse("Couldn't close connection: " + ex.getMessage(), true);
        }
    }
    
    public void studyCard(int times, Card card) {
        int nTimes = times;
        for (int i = 0; i < (2 * nTimes); i++) {
            card.getStats().addNewPractise(new Random().nextInt(10000) + 100);
        }
    }
    
}

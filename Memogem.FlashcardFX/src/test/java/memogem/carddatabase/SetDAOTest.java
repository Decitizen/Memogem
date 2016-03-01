
package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            File f = new File(testDBname);
            boolean success = f.delete();
        } catch (Exception e) {
        }
        
        testset = new Set("TestSet");
        initDb = new InitDb(testDBname);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        connect();
    }

    public void connect() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + testDBname + "");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
//        String cardSetDateTime = "2008-12-26 12:05:05";
//        LocalDateTime locDT = LocalDateTime.parse(cardSetDateTime, formatter);
    }
    
    public void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
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
            connect();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet;");
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "",true);
        }
        closeConnection();
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void seeIfearlierEntriesAreInTheDB() {
        Statement stmt = null;
        ResultSet rs = null;
        SetDAO sD = new SetDAO(testDBname);
        try {
            connect();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet;");
            String tulokset = "";
            while (rs.next()) {
                tulokset += "" + rs.getString("Name");
            }
            assertEquals("", tulokset);
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        closeConnection();
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void deleteEarlierTestSets() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connect();
            stmt = dbConnection.createStatement();
            stmt.executeUpdate("DELETE FROM CardSet WHERE Name = '" + testset.getName() + "';");
            stmt.executeUpdate("DELETE FROM CardSet WHERE Name = 'Randomizer2';");
            rs = stmt.executeQuery("SELECT * FROM CardSet;");
                    
            String tulokset = "";
            while (rs.next()) {
                tulokset += "" + rs.getString("Name");
            }
            assertEquals("", tulokset);
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        closeConnection();
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void testAddWithThreeSetsFunctionsProperly() {
        String name1 = "Automation";
        String name2 = "Biology";
        String name3 = "Chemistry";
        String resultsCheck = name1 + name2 + name3;
        Statement stmt = null;
        ResultSet rs = null;
        SetDAO sD = new SetDAO(testDBname);
        create3TestSetsByNameAndDate(name1, name2, name3, sD);
        
        try {
            connect();
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet ORDER BY Name ASC;");
            String tulokset = "";
            while (rs.next()) {
                tulokset += "" + rs.getString("Name");
            }
            assertEquals(resultsCheck, tulokset);
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void testAddWithThreeSetsAndDeleteSecondSetFunctionsProperly() {
        String name1 = "Automation";
        String name2 = "Biology";
        String name3 = "Chemistry";
        String resultsCheck = name1 + name3;
        Statement stmt = null;
        ResultSet rs = null;
        SetDAO sD = new SetDAO(testDBname);
        create3TestSetsByNameAndDate(name1, name2, name3, sD);
        try {
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet WHERE name = '" + name2 + "';");
            String id = rs.getString("Id");
            stmt.close();
            dbConnection.close();
            sD.delete(new Set(id, name2, LocalDateTime.now()));
            
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + testDBname + "");
            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CardSet ORDER BY Name ASC;");
            String tulokset = "";
            while (rs.next()) {
                tulokset += "" + rs.getString("Name");
            }
            assertEquals(resultsCheck, tulokset);
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        closeStmtAndResultSet(stmt, rs);
        
    }
    
    @Test
    public void testGetAllFuntionsProperlyWithThreeSets() {
        SetDAO sD = new SetDAO(testDBname);
        String name1 = "Automation";
        String name2 = "Biology";
        String name3 = "Chemistry";
        String resultsCheck = name1 + name2 + name3;
        create3TestSetsByNameAndDate(name1, name2, name3, sD);
        Statement stmt = null;
        ResultSet rs = null;
        List<Set> setit = new LinkedList<>();
        try {
            setit = sD.getAll();
        } catch (SQLException se) {
            assertTrue("Set SQL-query failed: " + se.getMessage() + "", false);
        }
        String tulokset = "";
        for (Set set : setit) {
            tulokset += set.getName();
        }
        assertEquals(resultsCheck, tulokset);
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
    
    public void create3TestSetsByNameAndDate(String name1, String name2, String name3, SetDAO sD) {
        Set set1 = new Set(name1);
        Set set2 = new Set(name2);
        Set set3 = new Set(name3);
        Timestamp pvmTimestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime locDT = pvmTimestamp.toLocalDateTime();
        set1.setLastTimeStudied(locDT);
        set2.setLastTimeStudied(locDT.minusMonths(1));
        set3.setLastTimeStudied(locDT.minusYears(10));
        try {
            sD.add(set1);
        } catch (SQLException ex) {
            assertTrue("Set SQL-query for adding failed: " + ex.getMessage() + "", false);
        }
        try {
            sD.add(set2);
        } catch (SQLException ex) {
            assertTrue("Set SQL-query for adding failed: " + ex.getMessage() + "", false);
        }
        try {
            sD.add(set3);
        } catch (SQLException ex) {
            assertTrue("Set SQL-query for adding failed: " + ex.getMessage() + "", false);
        }
    }
    
}

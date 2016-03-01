package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import memogem.carddatabase.CardDAO;
import memogem.coreapplication.Card;
import memogem.coreapplication.Set;
import memogem.coreapplication.Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CardDAOTest {
    private String testDBname;
    private Connection dbConnection;
    private CardDAO cD;
    private Set testset;
    private InitDb initDb;
    
    public CardDAOTest() {
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
        
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:" + testDBname + "");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            cD = new CardDAO(testDBname);
        } catch (SQLException se) {
            System.out.println(se.getCause());
            return;
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
    public void cardAddNewCard() {
        Card card1 = new Card("Jalat", "Feet");
        String cardId = card1.getId();
        studyCard(10, card1);
        testset.addCard(card1);
        Statement stmt = null;
        ResultSet rs = null;
        try {
            cD.add(card1);
        } catch (SQLException se) {
            assertTrue("Card SQL-query failed: " + se.getMessage() + "", false);
        }
        try {
            stmt = dbConnection.createStatement();
        } catch (SQLException se) {
            assertTrue("Couldn't create statement", false);
        }
        try {
            rs = stmt.executeQuery("SELECT * FROM Card WHERE id = '" + cardId + "';");
        } catch (SQLException se) {
            assertTrue("Couldn't create Resultset", false);
        }
        try {
            assertEquals("Couldn't find Front of the Card", rs.getString("Front"), "Jalat");
        } catch (SQLException se) {
        }
        closeStmtAndResultSet(stmt, rs);
    }
    public void studyCard(int times, Card card) {
        int nTimes = times;
        for (int i = 0; i < (2 * nTimes); i++) {
            card.getStats().addNewPractise(new Random().nextInt(10000) + 100);
        }
    }
    @Test
    public void cardAddNewCardWithTags() {
        Card card1 = new Card("Jalat", "Feet");
        String cardId = card1.getId();
        
        Tag tag1 = new Tag("Physiology");
        Tag tag2 = new Tag("Advanced Space Studies");
        Tag tag3 = new Tag("Space Propulsion");
        
        card1.addNewTag(tag1);
        card1.addNewTag(tag2);
        card1.addNewTag(tag3);
        
        studyCard(10, card1);
        testset.addCard(card1);
        Statement stmt = null;
        ResultSet rs = null;
        try {
            cD.add(card1);
        } catch (SQLException se) {
            assertTrue("Card SQL-query failed: " + se.getMessage() + "", false);
        }
        try {
            stmt = dbConnection.createStatement();
        } catch (SQLException se) {
            assertTrue("Couldn't create statement", false);
        }
        try {
            rs = stmt.executeQuery("SELECT * FROM Card WHERE id = '" + cardId + "';");
        } catch (SQLException se) {
            assertTrue("Couldn't create Resultset " + se.getMessage(), false);
        }
        try {
            assertEquals("Couldn't find Front of the Card", rs.getString("Front"), "Jalat");
        } catch (SQLException se) {
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
}

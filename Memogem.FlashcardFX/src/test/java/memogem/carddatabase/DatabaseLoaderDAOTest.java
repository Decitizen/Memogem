
package memogem.carddatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import memogem.coreapplication.Card;
import memogem.coreapplication.Set;
import memogem.coreapplication.Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class DatabaseLoaderDAOTest {
    private String testDBname;
    private InitDb initDb;
    private List<Set> setsDummy;
    private List<Set> setsImported;
    
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
        testDBname = "testingDatabase.db";
        initDb = new InitDb(testDBname);
        setsDummy = createDummyDatabase();
    }
    
    @After
    public void tearDown() {
        try {
            File f = new File(testDBname);
            boolean success = f.delete();
        } catch (Exception e) {
        }
    }
    
    @Test
    public void testDatabaseLoaderCreation() {
        //let's add one particular card for search purposes
        List<Set> setsImported = new ArrayList<>();
        List<Card> cardDatabase = new ArrayList<>();
        Map<Tag, List<Card>> tagDatabase = new HashMap<>();
        try {
            DatabaseLoaderDAO dbLoader = new DatabaseLoaderDAO(setsImported, cardDatabase, tagDatabase, testDBname);
        } catch (SQLException ex) {
            assertTrue("Databaseloader creation failed: " + ex.getMessage() + "", false);
        }
    }
    
    @Test
    public void testDatabaseLoadingRightAmountOfSets() {
        List<Set> setsImported = new ArrayList<>();
        List<Card> cardDatabase = new ArrayList<>();
        Map<Tag, List<Card>> tagDatabase = new HashMap<>();
        try {
            DatabaseLoaderDAO dbLoader = new DatabaseLoaderDAO(setsImported, cardDatabase, tagDatabase, testDBname);
            dbLoader.getAll();
            String printout = "";
            for (Set setX : setsImported) {
                printout += setX.getName() + ", id: " + setX.getId() + "\n";
            }
            assertEquals(" printout: " + printout, 1, setsImported.size());
        } catch (SQLException ex) {
            assertTrue(ex.getMessage(), false);
        }
    }
    
    @Test
    public void testDatabaseLoadingRightAmountOfCards() {
        List<Set> setsImported = new ArrayList<>();
        List<Card> cardDatabase = new ArrayList<>();
        Map<Tag, List<Card>> tagDatabase = new HashMap<>();
        int sum = 0;
        for (Set set : setsDummy) {
            sum += set.getCards().size();
        }
        try {
            DatabaseLoaderDAO dbLoader = new DatabaseLoaderDAO(setsImported, cardDatabase, tagDatabase, testDBname);
            dbLoader.getAll();
            assertEquals("Wrong number of cards", sum, cardDatabase.size());
        } catch (SQLException ex) {
            assertTrue(ex.getMessage() + "", false);
        }
        
    }
    
    @Test
    public void testDatabaseLoadingWithTargetCard() {
        List<Set> setsImported = new ArrayList<>();
        List<Card> cardDatabase = new ArrayList<>();
        Map<Tag, List<Card>> tagDatabase = new HashMap<>();
        try {
            DatabaseLoaderDAO dbLoader = new DatabaseLoaderDAO(setsImported, cardDatabase, tagDatabase, testDBname);
            dbLoader.getAll();
            assertNotNull(setsImported.get(0).getCards().get(0).getBack());
        } catch (SQLException ex) {
            assertTrue("Databaseloader creation failed: " + ex.getMessage() + "", false);
        }
        
    }
    
    @Test
    public void compareImportedCardsWithLocalOnes() {
        List<Set> setsImported = new ArrayList<>();
        List<Card> cardDatabase = new ArrayList<>();
        Map<Tag, List<Card>> tagDatabase = new HashMap<>();
        try {
            DatabaseLoaderDAO dbLoader = new DatabaseLoaderDAO(setsImported, cardDatabase, tagDatabase, testDBname);
            dbLoader.getAll();
            Card localCard = setsDummy.get(setsDummy.size()-1).getCards().get
                        (setsDummy.get(setsDummy.size()-1).getCards().size()-1);
            Card importedCard = null;
            for (Card card : cardDatabase) {
                if (localCard.equals(card)) {
                    importedCard = card;
                }
            }
            assertEquals(cardDatabase.size(), 10);
            assertEquals(localCard.getBack(), importedCard.getBack());
            assertEquals(localCard.getFront(), importedCard.getFront());
            assertEquals(localCard.getId(), importedCard.getId());
            assertEquals(localCard.getLastTimeStudied(), importedCard.getLastTimeStudied());
            assertEquals(localCard.getSet().getId(), importedCard.getSet().getLastTimeStudied());
        } catch (SQLException ex) {
            assertTrue("Databaseloader creation failed: " + ex.getMessage() + "", false);
        }
        
    }
    
    public void studyCard(int times, Card card) {
        int nTimes = times;
        for (int i = 0; i < (2 * nTimes); i++) {
            card.getStats().addNewPractise(new Random().nextInt(10000) + 100);
        }
    }
    
    public List<Card> createDummyCards() {
        //create cards
        Card card7 = new Card("What is the capital of Philippines ?", "Manila");
        Card card8 = new Card("What is the highest mountain in the Appalachian range?", "Mt. Mitchell");
        Card card9 = new Card("What is the capital of Gambia ?", "Banjul");
        Card card10 = new Card("What is the basic unit of currency for Morocco ?", "Dirham");
        Card card11 = new Card("What is the basic unit of currency for Switzerland ?", "Franc");
        Card card12 = new Card("What is the capital of Luxembourg?", "Luxembourg");
        Card card13 = new Card("What is the capital of Greece ?", "Athens");
        Card card14 = new Card("What is the basic unit of currency for Dominica ?", "Dollar");
        
        List<Card> cards = new LinkedList<>();
        cards.add(card7);
        cards.add(card8);
        cards.add(card9);
        cards.add(card10);
        cards.add(card11);
        cards.add(card12);
        cards.add(card13);
        cards.add(card14);
        return cards;
    }
    
    private List<Set> createDummyDatabase() {
        CardDAO cD = null;
        
        //create sets
        SetDAO sD = null;
        String name1 = "Automation";
        String name2 = "Geography";
        String name3 = "Chemistry";
        
        Set set1 = new Set(name1);
        Set set2 = new Set(name2);
        
        Card card1 = new Card("Artificial intelligence will take over by which year according to "
                + "Kurzweil?", "ca 2040");
        Card card2 = new Card("What is the concept of singularity?", "Singularity"
                + " is the point where exponentially advancing artificial intell"
                + "igence becomes more intelligent than human beings. The pace o"
                + "f iterations of the AI redesigning itself will become so tiny "
                + "that to human perceptions changes happen almost instantaneously.");
        
        studyCard(5, card1);
        studyCard(5, card2);
        
        set1.addCard(card1);
        set1.addCard(card2);
        
        Set set3 = new Set(name1);
        
        List<Card> cards = createDummyCards();
        //study cards and add them to the set
        for (Card card : cards) {
            studyCard((new Random().nextInt(5) + 3), card);
            set2.addCard(card);
        }
        //create list
        List<Set> sets = new LinkedList<>();
        
        //create timestamps
        Timestamp pvmTimestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime locDT = pvmTimestamp.toLocalDateTime();
        set1.setLastTimeStudied(locDT);
        set2.setLastTimeStudied(locDT.minusMonths(1));
        set3.setLastTimeStudied(locDT.minusYears(10));
        
        sets.add(set2);
        sets.add(set1);
        
        //add sets to the database
        try {
            sD = new SetDAO(testDBname);
            sD.add(set2);
            sD.add(set1);
            
            cD = new CardDAO(testDBname);
            for (Card card : cards) {
                cD.add(card);
            }
            cD.add(card1);
            cD.add(card2);
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return sets;
    }
}

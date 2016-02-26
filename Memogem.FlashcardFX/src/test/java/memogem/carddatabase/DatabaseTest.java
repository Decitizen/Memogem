package memogem.carddatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import memogem.carddatabase.Database;
import memogem.coreapplication.Card;
import memogem.coreapplication.Tag;
import memogem.coreapplication.CardConnection;
import memogem.coreapplication.LogicalConnection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

// Database-testit joudutaan tekemään uudestaan, koska rakenteen kompleksisuuden takia
// SQL-tietokanta sopii paremmin ratkaisuksi

public class DatabaseTest {
    private Database db1;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;
    
    public DatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        db1 = new Database("dummyDatabase.db");
        card1 = new Card("Jalka", "Foot");
        card2 = new Card("Käsi", "Hand");
        card3 = new Card("Ihon on ihmisen...", "suurin elin.");
        card4 = new Card("Niskakipujen yleisimmät syyt", "Ylivoimaisesti yleisin niskakivun "
                + "syy on niskan ja hartioiden lihasjännitys, jonka aiheuttajia ovat fyysiset ja "
                + "henkiset kuormitustekijät, erityisesti hankalat asennot töissä tai harrastuksissa.");
        
        Tag tag1 = new Tag("Anatomia");
        Tag tag2 = new Tag("Englannin kieli");
        Tag tag3 = new Tag("Sairaudet");
        Tag tag4 = new Tag("Fysiologia");
        
        card1.addNewTag(tag1);
        card1.addNewTag(tag2);
        card2.addNewTag(tag1);
        card2.addNewTag(tag2);
        card3.addNewTag(tag4);
        card3.addNewTag(tag1);
        card4.addNewTag(tag4);
        card4.addNewTag(tag3);
        card4.addNewTag(tag1);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAddingIntoDatabase() {
        assertTrue(db1.addNewCard(card1));
    }
    
    @Test
    public void testAddingIntoDatabaseManyCards() {
        assertTrue(db1.addNewCard(card1));
        assertTrue(db1.addNewCard(card2));
        assertTrue(db1.addNewCard(card3));
    }
    
    @Test
    public void testDoesCardBelongIntoDatabaseTrue() {
        db1.addNewCard(card1);
        db1.addNewCard(card2);
        db1.addNewCard(card3);
        db1.addNewCard(card4);
        assertTrue(db1.containsCard(card1));
    }
    
    @Test
    public void testDoesCardBelongIntoDatabaseTrue2() {
        db1.addNewCard(card1);
        db1.addNewCard(card2);
        db1.addNewCard(card3);
        db1.addNewCard(card4);
        assertTrue("Card Id doesn't match amy card on the db", db1.containsCard(card4));
    }
    
    @Test
    public void testDoesCardBelongIntoDatabaseFalse() {
        db1.addNewCard(card1);
        db1.addNewCard(card2);
        db1.addNewCard(card3);
        assertFalse(db1.containsCard(card4));
    }
    
    @Test
    public void testMethodremoveCardWhenCardBelongsToTheDatabase() {
        db1.addNewCard(card1);
        assertTrue(db1.deleteCard(card1));
    }
    
    @Test
    public void testMethodremoveCardWhenCardDoesntBelongToTheDatabase() {
        db1.addNewCard(card4);
        assertFalse(db1.deleteCard(card1));
    }
    
    @Test
    public void testAddNewTagToTagDatabase() {
        Tag tag1 = new Tag("Anatomia");
        Tag tag2 = new Tag("Englannin kieli");
        List<Tag> tagilista = new ArrayList<>();
        tagilista.add(tag1);
        tagilista.add(tag2);
        
        db1.addNewTag(tagilista, card1);
        Map<Tag, List<Card>> tagDatabase = db1.getTagDatabase();
        assertTrue(db1.containsTag(tag2));
    }
    
    @Test
    public void testAddNewTagToTagDatabaseFalse() {
        Tag tag1 = new Tag("Anatomia");
        Tag tag2 = new Tag("Englannin kieli");
        Tag tag4 = new Tag("Fysiologia");
        List<Tag> tagilista = new ArrayList<>();
        tagilista.add(tag1);
        
        db1.addNewTag(tagilista, card1);
        Map<Tag, List<Card>> tagDatabase = db1.getTagDatabase();
        assertFalse(db1.containsTag(tag4));
    }
       
}

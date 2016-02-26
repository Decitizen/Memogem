
package memogem.coreapplication;

import java.util.Random;
import memogem.coreapplication.Card;
import memogem.coreapplication.Tag;
import memogem.coreapplication.CardConnection;
import memogem.coreapplication.CardStats;
import memogem.coreapplication.LogicalConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import javafx.util.converter.LocalDateTimeStringConverter;
import static org.junit.Assert.*;

public class CardTest {
    private Card card1;
    private long viimeisinOpiskelu;
    
    public static void passTime(int x) {
        for (int i = 0; i < (x * 1000000); i++) {
            int y = 5*12/12*12/12*12*6;
            y *= y;
            y *= y;
            y *= y;
        }
    }
    
    public CardTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        int x = new Random().nextInt(10);
        card1 = new Card("Siviili", "Civilian");
        for (int i = 0; i < 4; i++) {
            card1.getStats().startStudying();
            passTime(x*10000);
            card1.getStats().stopStudying();
        }
    }
    
    @Test
    public void testaaUudenTaginLaittaminen() {
        Card card2 = new Card("Kallis", "Expensive");
        Tag tagi1 = new Tag("Hollanti");
        assertFalse(card2.contains(tagi1));
    }
    
    @Test
    public void testaaUudenTaginLaittaminen2() {
        Card card2 = new Card("Kallis", "Expensive");
        Tag tagi1 = new Tag("Hollanti");
        card2.addNewTag(tagi1);
        assertTrue(card2.contains(tagi1));
    }
    
    @Test
    public void testaaTaginLisaaminen() {
        Tag tagi1 = new Tag("Englanti");
        assertTrue(card1.addNewTag(tagi1));
    }
    
    @Test
    public void testaaTaginLisaaminenEiToimiJosToinenOnSama() {
        Tag tagi1 = new Tag("Englanti");
        Tag tagi2 = new Tag("Englanti");
        assertTrue(card1.addNewTag(tagi1));
        assertFalse(card1.addNewTag(tagi2));
    }
    
    @Test
    public void testaaTaginLisaaminenKolmeEriTagia() {
        Tag tagi1 = new Tag("Englanti");
        Tag tagi2 = new Tag("Väri");
        Tag tagi3 = new Tag("Rutherford");
        assertTrue(card1.addNewTag(tagi1));
        assertTrue(card1.addNewTag(tagi2));
        assertTrue(card1.addNewTag(tagi3));
    }
    
    @Test
    public void testaaTaginLisaaminenKolmeEriTagiaEiToimiJosYksiSama() {
        Tag tagi1 = new Tag("Englanti");
        Tag tagi2 = new Tag("Väri");
        Tag tagi3 = new Tag("Englanti");
        assertTrue(card1.addNewTag(tagi1));
        assertTrue(card1.addNewTag(tagi2));
        assertFalse(card1.addNewTag(tagi3));
    }
    
    @Test
    public void testaaMetodiagetLastTimeStudied() {
        LocalDateTime loc = null;
        ArrayList<LocalDateTime> list1 = new ArrayList();
        list1.addAll(card1.getStats().getStudyDates());
        Collections.sort(list1);
        
        assertEquals(card1.getLastTimeStudied(), list1.get(list1.size() - 1));
    }
    
    @Test
    public void testaaMetodiagetLastTimeStudiedKunEiVielaHarjoiteltu() {
        Card card2 = new Card("This rootbug...", "Is venomous.");
        assertNull(card2.getLastTimeStudied());
    }
    
    @Test
    public void testaaYhteydenmaaritteleminen() {
        CardConnection con1 = new CardConnection(LogicalConnection.CAUSE, new Card("Varit syntyvät", "...kun eripituiset valoaallot risteävät keskenään."));
        assertTrue(card1.addNewConnection(con1));
    }
    
    
    @Test
    public void testaaYhteydenmaaritteleminen2() {
        CardConnection con1 = new CardConnection(LogicalConnection.CORRELATION, new Card("Varit syntyvät", "...kun eripituiset valoaallot risteävät keskenään."));
        assertTrue(card1.addNewConnection(con1));
    }
    
    @Test
    public void testaaYhteydenmaaritteleminenKunSama() {
        CardConnection con1 = new CardConnection(LogicalConnection.CORRELATION, new Card("Varit syntyvät", "...kun eripituiset valoaallot risteävät keskenään."));
        CardConnection con2 = new CardConnection(LogicalConnection.CAUSE, new Card("Varit syntyvät", "...kun eripituiset valoaallot risteävät keskenään."));
        card1.addNewConnection(con1);
        card1.addNewConnection(con2);
        assertFalse(card1.addNewConnection(con1));
    }
    
    
    @Test
    public void testaaToimiikoMetodiOnkoTagi() {
        assertFalse(card1.contains(new Tag("Philosophy")));
    }
    
    @Test
    public void testaaToimiikoMetodiOnkoTagi2() {
        Tag tagi1 = new Tag("Englanti");
        card1.addNewTag(tagi1);
        assertTrue(card1.contains(new Tag("Englanti")));
    }
    
    @Test
    public void testaaToimiikoToStringOikein() {
        assertEquals("Card: " + card1.getId() + ", Tags: [] \n" +
                    "   Front: Siviili \n" +
                    "   Back: Civilian", card1.toString());
    }
    
    @Test
    public void testaaToimiikoEqualsOikein() {
        Card card2 = card1;
        assertEquals(card1, card2);
    }
    
    @Test
    public void testaaToimiikoEqualsOikeinKunEriKortit() {
        Card card2 = new Card("Uima...", "lasit.");
        assertNotSame(card1, card2);
    }
    
    @After
    public void tearDown() {
    }
}

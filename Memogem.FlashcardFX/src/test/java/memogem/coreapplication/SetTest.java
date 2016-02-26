package memogem.coreapplication;

import memogem.coreapplication.Card;
import memogem.coreapplication.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SetTest {
    private Set set1;
    private Card card;
    public SetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        set1 = new Set("Fysiologia");
        card = new Card("Angola", "Afrikka");
        
        card.getStats().startStudying();
        
        card.getStats().stopStudying();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testaaMetodiCreateNewCardTrue() {
        boolean luotiinUusi = set1.createNewCard("Jalka", "Foot");
        assertTrue(luotiinUusi);
    }
    
    @Test
    public void testaaMetodiAddNewCardFalseJosKorttiOnJoListassa() {
        Card card1 = new Card("Jalka", "Foot");
        set1.addCard(card1);
        assertFalse(set1.addCard(card1));
    }
    
    @Test
    public void testaaMetodiAddNewCardFalseMonellaKortilla() {
        Card card1 = new Card("Jalka", "Foot");
        Card card2 = new Card("Sisilia", "Cicily");
        Card card3 = new Card("Arto", "Hihavainen");
        Card card4 = new Card("Roots", "Juuret");
     
        set1.addCard(card1);
        set1.addCard(card2);
        set1.addCard(card3);
        set1.addCard(card4);
        assertFalse(set1.addCard(card1));
    }
    
    @Test
    public void testaaMetodinCalculateLastTimeModifiedViimeisinkertaEiKortti1() {
        for (int i = 0; i < 5; i++) {
            card.getStats().startStudying();
            passTime(20);
            card.getStats().stopStudying();
        }
        Card card2 = new Card("Sisilia", "Cicily");
        for (int i = 0; i < 5; i++) {
            card2.getStats().startStudying();
            passTime(20);
            card2.getStats().stopStudying();
        }
        set1.addCard(card);
        assertNotSame(set1.calculateLastTimeModified().getNano(), card.getLastTimeStudied().getNano());
    }
    
    @Test
    public void testaaMetodinCalculateLastTimeModifiedViimeisinkertaOikeaArvoKortti2() {
        for (int i = 0; i < 5; i++) {
            card.getStats().startStudying();
            passTime(20000);
            passTime(20000);
            card.getStats().stopStudying();
            card.getStats().startStudying();
            passTime(20000);
            passTime(20000);
            card.getStats().stopStudying();
        }
        Card card2 = new Card("Sisilia", "Cicily");
        for (int i = 0; i < 5; i++) {
            card2.getStats().startStudying();
            passTime(20000);
            passTime(20000);
            card2.getStats().stopStudying();
            card.getStats().startStudying();
            passTime(20000);
            passTime(20000);
            card.getStats().stopStudying();
        }
        set1.addCard(card);
        set1.addCard(card2);
        assertEquals(set1.calculateLastTimeModified().getNano(), card2.getLastTimeStudied().getNano());
    }
    
    public static void passTime(int x) {
        for (int i = 0; i < (x * 1000000); i++) {
            int y = 5*12/12*12/12*12*6;
            y *= y;
            y *= y;
            y *= y;
        }
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.coreapplication;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Willburner
 */
public class CardEngineTest {
    private CardEngine cE;
    private Set set1;
    private List<Card> cards;
    
    public CardEngineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        cE = new CardEngine();
        set1 = new Set("Geography");
        cards = new LinkedList<>();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testCardEngineInitialization() {
        cE.studySet(set1);
        assertEquals(set1.getName(), cE.getSet().getName());
    }
    
    @Test
    public void testCardEngineInitialization2() {
        cE.studySet(set1);
        assertEquals(set1.getCards().size(), cE.getSet().getCards().size());
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenNull() {
        assertEquals(cE.getStudymode(), 0);
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenSetLoadedAndModeSetTo1() {
        cE.studySet(set1);
        cE.setStudyMode(1);
        assertEquals(1, cE.getStudymode());
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenSetLoadedAndModeSetTo0AndCalculateOrder() {
        createTestSet();
        studyAllSetCards(cards);
        cE.setStudyMode(0);
        cE.studySet(set1);
        boolean different = false;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) != cE.getSortedCards().get(i)) {
                different = true;
            }
        }
        assertFalse(different);
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenSetLoadedAndModeSetTo1AndCalculateOrder() {
        createTestSet();
        studyAllSetCards(cards);
        cE.setStudyMode(1);
        cE.studySet(set1);
        
        boolean different = false;
        double minDifficulty = 33.0;
        double easiestDifficulty = -22.0;
        Card card = null;
        for (int i = 0; i < cards.size(); i++) {
            double iDifficulty = cards.get(i).getStats().calculateAVGDifficulty();
            if (iDifficulty < minDifficulty) {
                minDifficulty = iDifficulty;
            }
            if (iDifficulty > easiestDifficulty) {
                easiestDifficulty = iDifficulty;
                card = cards.get(i);
            }
        }
        int minDifficultyInt = ((int) (minDifficulty) * 1000) / 100;
        int sortedDifficulty1 = ((int) (cE.getSortedCards().get(0).getStats().calculateAVGDifficulty() * 1000)) / 100;
        assertEquals(sortedDifficulty1, minDifficultyInt);
        assertFalse(cE.getSortedCards().contains(card));
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenSetLoadedAndModeSetTo2() {
        cE.studySet(set1);
        cE.setStudyMode(2);
        assertEquals(2, cE.getStudymode());
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenSetLoadedAndModeSetTo3() {
        cE.studySet(set1);
        cE.setStudyMode(3);
        assertEquals(3, cE.getStudymode());
    }
    
    @Test
    public void testCardEngineMethodSetStudyModeWhenSetLoadedAndModeSetTo4() {
        cE.studySet(set1);
        cE.setStudyMode(4);
        assertEquals(0, cE.getStudymode());
    }
    
    private void createTestSet() {
        Card card7 = new Card("What is the capital of Philippines ?", "Manila");
        Card card8 = new Card("What is the highest mountain in the Appalachian range?", "Mt. Mitchell");
        Card card9 = new Card("What is the capital of Gambia ?", "Banjul");
        Card card10 = new Card("What is the basic unit of currency for Morocco ?", "Dirham");
        Card card11 = new Card("What is the basic unit of currency for Switzerland ?", "Franc");
        Card card12 = new Card("What is the capital of Luxembourg?", "Luxembourg");
        Card card13 = new Card("What is the capital of Greece ?", "Athens");
        Card card14 = new Card("What is the basic unit of currency for Dominica ?", "Dollar");
        
        cards.add(card7);
        cards.add(card8);
        cards.add(card9);
        cards.add(card10);
        cards.add(card11);
        cards.add(card12);
        cards.add(card13);
        cards.add(card14);
        set1.addCard(card7);
        set1.addCard(card8);
        set1.addCard(card9);
        set1.addCard(card10);
        set1.addCard(card11);
        set1.addCard(card12);
        set1.addCard(card13);
        set1.addCard(card14);
    }
    
    public void studyCard(int times, Card card) {
        int nTimes = times;
        for (int i = 0; i < (2 * nTimes); i++) {
            card.getStats().addNewPractise(new Random().nextInt(10000) + 100);
        }
    }
    
    public void studyAllSetCards(List<Card> cardst) {
        for (Card card : cardst) {
            studyCard((new Random().nextInt(12) + 2), card);
        }
    }
}



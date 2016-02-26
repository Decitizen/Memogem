package memogem.coreapplication;



import java.time.LocalDateTime;
import memogem.coreapplication.CardStats;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class CardStatsTest {
    private CardStats statsit1;
    public CardStatsTest() {
    }
    
    public static void passTime(int x) {
        for (int i = 0; i < (x * 1000000); i++) {
            int y = 5*12/12*12/12*12*6;
            y *= y;
            y *= y;
            y *= y;
        }
    }
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        statsit1 = new CardStats(28);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testaaMetodinChangeCharCountToimintaa() {
        statsit1.changeCharCount(66);
        assertEquals(66, statsit1.getCharcount());
    }
    
    @Test
    public void testaaMetodinChangeCharCountToimintaaVaarallaSyotteella() {
        statsit1.changeCharCount(66);
        assertNotSame(67, statsit1.getCharcount());
    }
    
    @Test
    public void testaaMetodinaddNewPracticeToimintaaVaarallaSyotteella() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertNotSame(6, statsit1.getPracticeTimes());
    }
    
    @Test
    public void testaaMetodinaddNewPracticeToimintaaNeljastiHarjoiteltu() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        assertEquals(4, statsit1.getPracticeTimes());
    }
    
    @Test
    public void testaaMetodinaddNewPracticeToimintaaViidestiHarjoiteltu() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertEquals(5, statsit1.getPracticeTimes());
    }
    
    @Test
    public void testaaMetodinaddNewPracticeToimintaaViidestiHarjoiteltuKunVainNegatiivisiaSyotteita() {
        statsit1.addNewPractise(-5000);
        statsit1.addNewPractise(-6000);
        statsit1.addNewPractise(-2000);
        statsit1.addNewPractise(-1000);
        statsit1.addNewPractise(-3000);
        assertEquals(0, statsit1.getPracticeTimes());
    }
    
    @Test
    public void testaaMetodinaddNewPracticeToimintaaViidestiHarjoiteltuKunMuutamiaNegatiivisiaSyotteita() {
        statsit1.addNewPractise(-5000);
        statsit1.addNewPractise(-6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertEquals(3, statsit1.getPracticeTimes());
    }
    
    @Test
    public void testaaMetodincalculateTimeStudiedToimintaa1() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertEquals(17000, statsit1.calculateTimeStudied());
    }
    
    @Test
    public void testaaMetodincalculateTimeStudiedToimintaaKunNegatiivinenSyote() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(-6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertEquals(11000, statsit1.calculateTimeStudied());
    }
    
    @Test
    public void testaaMetodincalculateTimeStudiedToimintaaKunNollaSyotteita() {
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        assertEquals(0, statsit1.calculateTimeStudied());
    }
    
    @Test
    public void testaaMetodincalculateAVGSpeedToimintaaKunNollaSyotteita() {
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        assertEquals(0, statsit1.calculateAVGSpeed());
    }
    
    @Test
    public void testaaMetodincalculateAVGSpeedToimintaaKunMuutamiaNollaSyotteita() {
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(0);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(2000);
        assertEquals(2000, statsit1.calculateAVGSpeed());
    }
    
    @Test
    public void testaaMetodincalculateAVGSpeedToimintaaKuusiPositiivistaSyotetta() {
        statsit1.addNewPractise(100);
        statsit1.addNewPractise(200);
        statsit1.addNewPractise(300);
        statsit1.addNewPractise(400);
        statsit1.addNewPractise(500);
        assertEquals(300, statsit1.calculateAVGSpeed());
    }
    
    @Test
    public void testaaAddNewTimestamptoimintaa() {
        statsit1.addNewTimeStamp();
        LocalDateTime loc1 = statsit1.getLastStudyDate();
        assertEquals(loc1.getSecond(), LocalDateTime.now().getSecond());
    }
    
    @Test
    public void testaaMetodinStartStudyingJaStopStudyingToimintaa() {
        statsit1.startStudying();
        passTime(100000);
        passTime(100000);
        statsit1.stopStudying();
       
        assertNotNull(statsit1.getHowfast().get(statsit1.getPracticeTimes() - 1));
    }
    
    @Test
    public void testMethodCalculateVarianceWithFiveStudyTimes() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertEquals(3440000, statsit1.calculateVariance());
    }
    
    @Test
    public void testMethodCalculateVarianceWithFourStudyTimesAndOneZero() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(0);
        assertEquals(2888889, statsit1.calculateVariance());
    }
    
    @Test
    public void testMethodCalculateStandardVariationWithFiveStudyTimes() {
        statsit1.addNewPractise(5000);
        statsit1.addNewPractise(6000);
        statsit1.addNewPractise(2000);
        statsit1.addNewPractise(1000);
        statsit1.addNewPractise(3000);
        assertEquals(1854, statsit1.calculateStandardDeviation());
    }
    
    @Test
    public void testMethodCalculateStandardVariationWithJustOneStudyTime() {
        statsit1.addNewPractise(5000);
        assertEquals(0, statsit1.calculateStandardDeviation());
    }
    
}

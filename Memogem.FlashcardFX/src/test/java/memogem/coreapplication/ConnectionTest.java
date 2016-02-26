package memogem.coreapplication;

import memogem.coreapplication.Card;
import memogem.coreapplication.CardConnection;
import memogem.coreapplication.LogicalConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectionTest {
    private CardConnection con1;
            
    public ConnectionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        con1 = new CardConnection(LogicalConnection.CAUSE, new Card("Jalka...", "Sieni"));
    }
    
    @Test
    public void testataanKonstruktorintoimintaa() {
        assertEquals(con1.getConnectionType(), LogicalConnection.CAUSE);
    }
    
    @Test
    public void testataanMetodiSetSuhdeTyyppia() {
        assertEquals(con1.getConnectionType(), LogicalConnection.CAUSE);
        con1.setConnectionType(LogicalConnection.CORRELATION);
        assertEquals(con1.getConnectionType(), LogicalConnection.CORRELATION);
    }
    
    @Test
    public void testataanMetodiSetSuhdeTyyppiaLuvuilla() {
        assertEquals(con1.getConnectionType(), LogicalConnection.CAUSE);
        con1.setConnectionType(LogicalConnection.CORRELATION);
        assertEquals(con1.getConnectionType().getCode(), LogicalConnection.CORRELATION.getCode());
    }
    
    @Test
    public void testataanMetodinGetConnectionToCardToimintaa() {
        Card card2 = new Card("Silvio...", "Berlusconi");
        CardConnection con2 = new CardConnection(LogicalConnection.EFFECT, card2);
        assertEquals(con2.getConnectionTocard(), card2);
    }
    
    @Test
    public void testataanMetodinGetConnectionToCardToimintaa2() {
        Card card2 = new Card("Silvio...", "Berlusconi");
        Card card1 = new Card("Jordelios", "Berlusconi");
        CardConnection con2 = new CardConnection(LogicalConnection.EFFECT, card2);
        assertNotSame(con2.getConnectionTocard(), card1);
    }
    
    
    @After
    public void tearDown() {
    }
    
}

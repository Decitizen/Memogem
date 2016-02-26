package memogem.carddatabase;

import java.sql.SQLException;
import memogem.carddatabase.CardDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CardDAOTest {
    
    private CardDAO cD;
    
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
        try {
            cD = new CardDAO("databaseCardDao.db");
        } catch (SQLException se) {
            System.out.println(se.getCause());
            return;
        }
    }
    
    @After
    public void tearDown() {
    }
    
}

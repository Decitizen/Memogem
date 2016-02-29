package memogem.ui;

import memogem.carddatabase.*;
import memogem.coreapplication.*;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;


public class MainApp extends Application  {
    private Stage mainStage; 
    private CardEngine cEngine;
    private Database database; 
    private MainWindow mainWindowObject;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        //Create new database
        database = new Database(true, "test.db");
        
        //Create dummysets for testing
        createDummySets();
        
        this.mainStage = stage;
        MainWindow mWindowObject = new MainWindow(mainStage, database, cEngine);
        mWindowObject.createMWindow();
        
        //Show/draw the GUI
        mainStage.show();
    }
   
    public void createDummySets() {
        Card card1 = new Card("Jalka", "Foot");
        Card card2 = new Card("Robot", "Ella");
        Card card3 = new Card("Ihon on ihmisen...", "suurin elin.");
        Card card4 = new Card("Niskakipujen yleisimmät syyt", "Ylivoimaisesti yleisin niskakivun "
                + "syy on niskan ja hartioiden lihasjännitys, jonka aiheuttajia ovat fyysiset ja "
                + "henkiset kuormitustekijät, erityisesti hankalat asennot töissä tai harrastuksissa.");

        Card card5 = new Card("Cardiovascular diseases are the leading cause of death globally."
                + "This is true in all areas of the world except...",
                "Africa.");

        Card card6 = new Card("] Deaths, at a given age, from CVD are more common and "
                + "have been increasing in much of the developing world, while in the developed world... ",
                "rates have declined in most of the developed world since the 1970s.");

        Card card7 = new Card("What is the capital of Philippines ?", "Manila");
        Card card8 = new Card("What is the highest mountain in the Appalachian range?", "Mt. Mitchell");
        Card card9 = new Card("What is the capital of Gambia ?", "Banjul");
        Card card10 = new Card("What is the basic unit of currency for Morocco ?", "Dirham");
        Card card11 = new Card("What is the basic unit of currency for Switzerland ?", "Franc");
        Card card12 = new Card("What is the capital of Luxembourg?", "Luxembourg");
        Card card13 = new Card("What is the capital of Greece ?", "Athens");
        Card card14 = new Card("What is the basic unit of currency for Dominica ?", "Dollar");

        Card card15 = new Card("Atoms make up all things. Subatomic particles make up atoms. "
                + "The main ones are protons, neutrons and electrons, although there are many more. "
                + "Protons contain a positive charge. Neutrons have?", "No charge at all.");
        Card card16 = new Card("According to the Big Bang Theory, "
                + "immediately after the \"Big Bang\" there were only ",
                "matter and antimatter particles. However there were slightly more matter "
                + "particles--something like one extra per billion particles. ");
        Card card17 = new Card("It is easier to roll a stone up a sloping road than to lift it vertical upwards because...",
                "collision between fast neutrons and nitrogen nuclei present in the atmosphere.");
        Card card18 = new Card("It is easier to roll a stone up a sloping road than to lift it vertical upwards because",
                "work done in rolling a stone is less than in lifting it.");
        Card card19 = new Card("The absorption of ink by blotting paper involves", "capillary action phenomenon");

        Card pCard1 = new Card("Mersheimer is known for being...", "a defensive realist.");
        Card pCard2 = new Card("Mersheimer's theory is based on the idea of global...", "Anarchy where nations are primarily self-interested entities.");
        Card pCard3 = new Card("Neoliberal Institutionalism, who authors?", "Keohane and Nye");
        Card pCard7 = new Card("Constructivism, what is it about? Which book was the most influential?", "Social Construction of Reality, Berger & Luckmann (1966): Human ideas contibute to our understanding and therefore also change our behavior. Therefore evolutionary realism and objectivism is dangerous - there is always a chance to create new human cultures. It's up to us.");
        Card pCard6 = new Card("Neoliberal Institutionalism, what is the main contribution of this theory?", "According to neoliberalists peace can be achieved through deepening the interconnetivity between nations. This makes them inevitably dependent of each other and therefore reduces incentives for conflict.");
        Card pCard5 = new Card("Johan Gaultung's big contribution to the understanding of violence:", "Structural violence, violence that is weakens people's"
                + "life possibilities due to oppressive societal structures that (ie inequality and poverty.)");
        Card pCard4 = new Card("Kenneth Waltz's contribution to the Global politics",
                "Walz defined new theory of the Global world order, world as a stage.");

        Set set1 = new Set("Physiology");
        Set set2 = new Set("Geography");
        Set set3 = new Set("Human Anatomy");
        Set set4 = new Set("Physics");
        Set set5 = new Set("Global Politics - Basic Theory");

        set4.addCard(card15);
        set4.addCard(card16);
        set4.addCard(card17);
        set4.addCard(card18);
        set4.addCard(card19);

        set5.addCard(pCard1);
        set5.addCard(pCard2);
        set5.addCard(pCard3);
        set5.addCard(pCard4);
        set5.addCard(pCard5);
        set5.addCard(pCard6);
        set5.addCard(pCard7);

        for (Card card : set4.getCards()) {
            studyCard(new Random().nextInt(4) + 4, card);
        }

        for (Card card : set5.getCards()) {
            studyCard(new Random().nextInt(4) + 4, card);
        }

        studyCard(7, card1);
        studyCard(2, card2);
        studyCard(4, card3);
        studyCard(5, card4);
        studyCard(7, card5);
        studyCard(3, card6);
        studyCard(11, card7);
        studyCard(5, card8);
        studyCard(8, card9);
        studyCard(6, card10);
        studyCard(9, card11);
        studyCard(2, card12);
        studyCard(5, card13);
        studyCard(1, card14);

        set1.addCard(card1);
        set1.addCard(card2);
        set1.addCard(card3);
        set1.addCard(card4);
        set1.addCard(card5);
        set1.addCard(card6);
        set2.addCard(card7);
        set2.addCard(card8);
        set2.addCard(card9);
        set2.addCard(card10);
        set2.addCard(card11);
        set2.addCard(card12);
        set2.addCard(card13);
        set2.addCard(card14);

        System.out.println("Set includes: " + set1.size() + " cards.");
        database.addNewSet(set2);
        database.addNewSet(set1);
        database.addNewSet(set3);
        database.addNewSet(set4);
        database.addNewSet(set5);

        System.out.println(set1);
        System.out.println(set2);
        System.out.println(set3);
    }
    
    public void studyCard(int times, Card card) {
        int nTimes = times;
        for (int i = 0; i < (2 * nTimes); i++) {
            card.getStats().addNewPractise(new Random().nextInt(10000) + 100);
        }
    }
}

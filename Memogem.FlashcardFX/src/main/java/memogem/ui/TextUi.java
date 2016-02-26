
package memogem.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import memogem.carddatabase.Database;
import memogem.coreapplication.*;

/**
 * Class just for testing purposes.
 * @author Willburner
 */
public class TextUi {
    private Database db1;
    private List<Set> sets;

    public TextUi() {
        this.sets = new ArrayList<>();
    }
    
    
    public void run() {
        db1 = new Database(true, "database.db");
        createDummySets();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            int index = 0;
            System.out.println("Choose a set to study:");
            for (Set set : sets) {
                System.out.println((index++) + ":" + set.toString());
            }
            int choose = Integer.parseInt(scanner.nextLine());
            
            studyDeck(choose, scanner);
            
        }
    }

    private void studyDeck(int choose, Scanner scanner) {
        while (true) {
            Set set = sets.get(choose);
            CardEngine engine = new CardEngine();
            engine.studySet(set);
            System.out.println("Choose studyMode: \n"
                    + "1. Freestyle\n"
                    + "2. Optimum\n"
                    + "3. Randomizer\n");
            int parsed = Integer.parseInt(scanner.nextLine());
            engine.setStudyMode(parsed);
            engine.next();
            /* = AVGspeed / (charcount / 10) + kerrat.
            
            If the AVG speed is high (let's say that it's ca 12 seconds)
            12000 ms and the amount of words
            there needs to be a time limit after which that 
            particular study time will be measured as too long.
            
            */
            
            
            System.out.println();
        }
    }
    
    private void createDummySets() {
        Card card1 = new Card("Jalka", "Foot");
        Card card5 = new Card("What is the capital of Philippines ? ", "Manila");
        Card card6 = new Card("What is the highest mountain in the Appalachian range?", "Mt Mitchell");
        Card card7 = new Card("What is the capital of North Carolina?", "Raleigh");
        Card card8 = new Card("What is the capital of Liberia ?", "Monrovia");
        Card card9 = new Card("What is the only country in the world whose name starts with 'O'? ?", "Oman");
        Card card2 = new Card("Käsi", "Hand");
        Card card3 = new Card("Ihon on ihmisen...", "suurin elin.");
        Card card4 = new Card("Niskakipujen yleisimmät syyt", "Ylivoimaisesti yleisin niskakivun "
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
        
        Set set1 = new Set("Fysiologian luennot 1");
        Set set2 = new Set("Biologian luennot 1");
        Set set3 = new Set("Geography");
        set3.addCard(card5);
        set3.addCard(card6);
        set3.addCard(card7);
        set3.addCard(card8);
        set3.addCard(card9);
        set1.addCard(card1);
        set1.addCard(card2);
        set2.addCard(card3);
        set2.addCard(card4);
        
        sets.add(set1);
        sets.add(set2);
        sets.add(set3);
    }

    
}

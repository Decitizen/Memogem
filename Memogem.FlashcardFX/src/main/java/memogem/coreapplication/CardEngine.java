
package memogem.coreapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Card Engine chooses the order and sequence of cards in the set.
 * @author Willburner
 */
public class CardEngine {
    private Set engineSet;
    private List<Card> cards;
    private List<Card> sortedCards;
    
    private int setSuccessrate;
    private int studymode;
    private int cardIndex;
    private List<Integer> sumOfTime;
    
    public CardEngine() {
        this.cards = null;
        this.sortedCards = null;
        this.sumOfTime = null;
        setSuccessrate = 0;
        studymode = 0;
    }

    public void studySet(Set set) {
        engineSet = set;
        cards = set.getCards();
        sortedCards = new ArrayList<>();
        sumOfTime = new LinkedList<>();
        sortedCards.addAll(cards);
        setSuccessrate = 0;
        calculateCardOrder();
        cardIndex = 0;
    }
    /**
     * Selection for different modes of study.
     * @param mode 
     */
    public void setStudyMode(int mode) {
        if (mode > 0 && mode < 4) {
            studymode = mode;
        }
    }
    /**
     * Calculates the study order according to the studymode.
     * Modes are: 
     * 1. by difficulty (removes easiest half of the list)
     * 2. by Occurrence (study the least studied cards first)
     * 3. by Fully Random order
     */
    public void calculateCardOrder() {
        if (sortedCards != null) {
            if (studymode == 1) {
                Collections.sort(sortedCards, new DifficultyComparator());
                if (sortedCards.size() > 3) {
                    for (int i = 0; i < (sortedCards.size() / 2); i++) {
                        sortedCards.remove(sortedCards.size() - 1);
                    }
                }
            }
            if (studymode == 2) {
                Collections.sort(sortedCards, new OccurrenceComparator());
            }
            if (studymode == 3) { //fully randomized order
                Collections.shuffle(sortedCards);
            }
        }
    }
    /**
     * Gives new card.
     * @return 
     */
    public Card next() {
        if ((cards == null)) {
            return null;
        } else if (cardIndex == (cards.size() - 1)) {
            return null;
        } else {
            Card returnCard = sortedCards.get(cardIndex);
            cardIndex++;
            return returnCard;
        }
    }

    private void showFront(Card card) {
        System.out.println("Front: " + card.getFront());
    }

    private void inputRating(Card card, Scanner scanner) {
        System.out.println("\n1 for Again\n"
                + "2 for Good\n"
                + "3 for Difficult\n");
        int parsed = Integer.parseInt(scanner.nextLine());
        card.getStats().addNewDifficulty(parsed-2);
    }

    private void keyPress(Scanner scanner) {
        System.out.println("Input any sequence of characters to show answer");
        scanner.nextLine();
    }

    public Set getSet() {
        return engineSet;
    }

    public int getStudymode() {
        return studymode;
    }

    public List<Card> getSortedCards() {
        return sortedCards;
    }
    
    private void showBack(Card card) {
        System.out.println("\nBack: " + card.getBack());
    }
    
}


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
    private Set engineSet; //currently studied set of cards
    private List<Card> sortedCards; //set's card ordered according to studymode
    private int studymode; //studymode
    private int cardIndex; //keep's track of set's place
    private List<Integer> sumOfTime; //overall sum of time of this study-session
    
    public CardEngine() {
        this.sortedCards = null;
        this.sumOfTime = null;
        studymode = 0;
    }
    
    /**
     * Begins the study of chosen set of cards.
     * @param set set that is to be studied
     */
    public void studySet(Set set) {
        engineSet = set;
        sortedCards = new ArrayList<>();
        sumOfTime = new LinkedList<>();
        sortedCards.addAll(engineSet.getCards());
        calculateCardOrder();
        cardIndex = 0;
    }
    /**
     * Selection for different modes of study.
     * 1. Sort by difficulty (only half of the set, most difficult first)
     * 2. Sort by longest review times
     * 3. Fresh, randomized order
     * @param mode 
     */
    public void setStudyMode(int mode) {
        if (mode > 0 && mode < 4) {
            studymode = mode;
        }
    }
    
    /*
    Algorithm that calculates this round's mastery percentage and
    moves already mastered cards to basket: mastered. Could be based on
    the average difficulty of last three hours of studytime or if more 
    than two rounds based on three last rounds' avg. 
    (Even better could calculate the whole of this study session's 
    duration and amount of activity and relativize on that.)
    
    Whole sessions activity could be based on the amount of cards studied during
    last half hour. Long pauses (more than minute) between cards could be ignored.
    Keep count of cards studied overall during this session. Maybe even do a small
    SQL table just to represent that. Table "Session" could be combined with sets and cards.
    */
    
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
     * Returns next card that will be studied.
     * @return Card-object
     */
    public Card next() {
        if ((engineSet.getCards() == null)) {
            return null;
        } else if (cardIndex == (engineSet.getCards().size() - 1)) {
            return null;
        } else {
            Card returnCard = sortedCards.get(cardIndex);
            cardIndex++;
            return returnCard;
        }
    }
    
    /**
     * Adds new user-evaluated rating to the list of ratings
     * in the card's statistics. 
     * @param rating (value as integer from easy to difficult: 2,1,0,-1,-2)
     */
    public void inputRating(int rating) {
        if (rating >= -2 && rating <= 2) {
            sortedCards.get(cardIndex).getStats().addNewDifficulty(rating);
        }
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
    
}

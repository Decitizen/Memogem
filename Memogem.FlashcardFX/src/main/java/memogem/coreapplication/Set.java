
package memogem.coreapplication;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;

/**
*Set holds all the cards that are included in different study-topics saved by the user,
*and it is above the Card-class in the final hierarchy of the program-structure.  
 */
public class Set {
    private String name; //name of the set
    private String id; //id in UUID format
    private LocalDateTime lastTimeStudied; //last time any of the set's cards were studied
    private List<Card> cards; //list of cards belonging to the set

    public Set() {
        this("");
    }
    
    public Set(String name) {
        this.name = name;
        cards = new ArrayList<>();
        UUID uid = UUID.randomUUID();
        id = "" + uid;
        lastTimeStudied = null;
    }

    public Set(String id, String name, LocalDateTime lastTimeStudied) {
        this.name = name;
        this.id = id;
        this.lastTimeStudied = lastTimeStudied;
        this.cards = new ArrayList<>();
    }
    /**
     * Creates new card
     * @param front String, text-field on the front of the card
     * @param back String, text-field on the back of the card
     * @return boolean
     */
    public boolean createNewCard(String front, String back) {
        Card newCard = new Card(front, back);
        return addCard(newCard);
    }
    /**
     * Adds new card to the deck.
     * @param card Added card
     * @return boolean
     */
    public boolean addCard(Card card) {
        if (!cards.contains(card)) {
            card.setSet(this);
            return cards.add(card);
        }
        return false;
    }
    /**
     * Finds the last time that any of the cards was studied.
     * @return LocalDateTime object
     */
    public LocalDateTime calculateLastTimeModified() {
        if (!cards.isEmpty()) {
            LocalDateTime lastTime = cards.get(0).getLastTimeStudied();
        
            for (Card card : cards) {
                if (card.getLastTimeStudied() == null) {
                    continue;
                }
                if (lastTime.isBefore(card.getLastTimeStudied())) { //goes through all the dates and saves the latest to the variable lastTime
                    lastTime = card.getLastTimeStudied();
                }
            }
            this.lastTimeStudied = lastTime;
            return lastTime;
        }
        return null;
    }
    
    /**
     * Calculates average of the user-evaluated difficulty.
     * @return The result as double (between -2 and 2)
     */
    public double calculateAVGDifficulty() {
        double averageDifficulty = 0;
        if (cards.isEmpty()) {
            return -99;
        } else {
            
            for (Card card : cards) {
                if (!card.getStats().isEmpty()) {
                    averageDifficulty += card.getStats().calculateAVGDifficulty();
                }
            }
        }
        return (1.0 * averageDifficulty / cards.size());
    }
    /**
     * Returns a worded version of set's overall 
     * difficulty as self-evaluated by the user.
     * 
     * @return String ("Excellent, Very Good, Good,
     * Balanced, Almost There, Somewhat Difficult,
     * Really Difficult and Unrecognized.
     */
    public String getAVGDifficultyAsString() {
        double avgD = calculateAVGDifficulty();
        if (avgD <= 2.0 && avgD > 1.5) {
            return "Excellent";
        }
        if (avgD <= 1.5 && avgD > 1.0) {
            return "Very Good";
        }
        if (avgD <= 1.0 && avgD > 0.5) {
            return "Good";
        }
        if (avgD <= 0.5 && avgD > 0.0) {
            return "Balanced";
        }
        if (avgD <= 0.0 && avgD > -0.5) {
            return "Almost There";
        }
        if (avgD <= -0.5 && avgD > -1.0) {
            return "Somewhat Difficult";
        }
        if (avgD <= -1.0 && avgD > -1.5) {
            return "Rather Difficult";
        }
        if (avgD <= -1.5 && avgD > -2.0) {
            return "Really Difficult";
        } else {
            return "Unrecognized";
        }
    }
    
    /**
     * Returns overall study time in milliseconds.
     * @return integer
     */
    public int calculateOverallTimeStudied() {
        int sum = 0;
        for (Card card : this.cards) {
            if (card.getStats().getPracticeTimes() != 0) {
                sum += card.getStats().calculateTimeStudied();
            }
        }
        return sum;
    }

    //getters and setters
    public List<Card> getCards() {
        return cards;
    }

    public int size() {
        return cards.size();
    }
    
    public String getId() {
        return id;
    }

    public LocalDateTime getLastTimeStudied() {
        return lastTimeStudied;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setLastTimeStudied(LocalDateTime lastTimeStudied) {
        this.lastTimeStudied = lastTimeStudied;
    }

    @Override
    public String toString() {
        String setInformation = "Set Name: " + this.getName();
        setInformation += "\nSet Id: " + this.id;
        setInformation += "\nCards (" + this.cards.size() +") \n";
        
        for (Card card : this.cards) {
            setInformation += card.toString() + "\n";
            
        }
        return setInformation;
        
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        
        if (o.getClass() != this.name.getClass()) return false;
        
        Set set1 = (Set) o;
        
        return set1.getId().equals(this.getId());
    }
    
    
    
    

}

 

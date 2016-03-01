
package memogem.coreapplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/** 
*This is the main class which holds much of the information that is essential to the way
*that the program works. There is a lot of information included into the objects that are
*created out of the Card-class.
*/
public class Card {
    private String id; //individual unique id in UUID format
    private String front; //front-text-field of the flashcard
    private String back; //backside-text-field of the flashcard
    private CardType type; //type of card: answerable question vs flashcard
    private List<Tag> tags; //tags
    private Set set; //set
    private List<CardConnection> connections; //logical connections to other cards **Still unused
    private CardStats stats; //holds all the statistical information about the cards
    
    public Card() {
        this("","", CardType.FLASHCARD);
    }

    public Card(String front, String back) {
        this(front, back, CardType.FLASHCARD);
    }

    public Card(String front, String back, CardType type) {
        this.front = front;
        this.back = back;
        this.tags = new ArrayList<>();
        this.stats = new CardStats(front.length() + back.length());
        this.type = type;
        
        UUID uid = UUID.randomUUID();
        this.id = "" + uid;
        this.connections = new ArrayList<>();
    }

    public Card(String id, String front, String back, CardType type, List<Tag> tags, 
                        Set set, CardStats stats) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.type = type;
        this.tags = tags;
        this.set = set;
        this.connections = new ArrayList<>();
        this.stats = stats;
    }
    
    /**
     * Returns the last time that the card-object was studied.
     * @return LocalDateTime object
     */
    public LocalDateTime getLastTimeStudied() {
        return stats.getLastStudyDate();
    }
    
    /**
     * Returns true if the collection embedded into the Card-object contains
     * given Tag.
     * @param tag Tag-object that will be searched for.
     * @return true if the given Tag is embedded into the Card
     */
    public boolean contains(Tag tag) {       
        return this.tags.contains(tag);
    }
    /**
     * Adds new Connection into the Card's list of connections.
     * @param connection Connection to be added into the list
     * @return returns true if the card is added. Returns false if the given connection
     * is already included in the collection.
     */
    public boolean addNewConnection(CardConnection connection) {
        if (!this.connections.contains(connection)) {
            return this.connections.add(connection);
        }
        return false;
    }
    /**
     * Adds new Tag into the Card's list of connections.
     * @param tag Tag to be added into the list
     * @return returns true if the Tag is added successfully. Returns false if the given Tag-object
     * is already included in the collection.
     */
    public boolean addNewTag(Tag tag) {
        if (!contains(tag)) { //tarkistetaan löytyykö tagi listasta
            this.tags.add(tag); //jos ei löydy, niin lisätään listaan
            return true;
        }
        return false;
    }
    
    //setters and getters    
    public List<CardConnection> getConnections() {
        return connections;
    }
    
    public String getFront() {
        return front;
    }
    
    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getId() {
        return id;
    }
    
    public List<Tag> getTags() {
        return this.tags;
    }

    public CardStats getStats() {
        return stats;
    }

    public CardType getType() {
        return type;
    }
    
    public Set getSet() {
        return set;
    }
    
    public void setSet(Set set) {
        this.set = set;
    }

    public void setStats(CardStats stats) {
        this.stats = stats;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dates = "";
        
        for (LocalDateTime locDT : this.stats.getStudyDates()) {
            dates += locDT.format(formatter) + "\n";
        }
        String stats = "";
        if (this.stats != null) {
            stats += "\n\n       Average difficulty: " + this.stats.calculateAVGDifficulty();
            stats += "\n       Average time per review: " + this.stats.calculateAVGSpeed();
            stats += "\n       Overall time studied: " + this.stats.calculateTimeStudied();
            stats += "\n       Times studied: " + this.stats.getHowfast().size();
            stats += "\n       Standard deviation time per review: " + this.stats.calculateStandardDeviation();
        }
        
        return "Card: " + id + ", Tags: " + this.tags + " \n   Front: " + this.front
                + " \n   Back: " + this.back
                + " \n   SetId: " + this.getSet().getId()
                + " \n   ConnectionType: " + this.type
                + " \n   Studydates: " + dates + stats;
    }
    
}

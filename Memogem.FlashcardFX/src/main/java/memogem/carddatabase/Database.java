
package memogem.carddatabase;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import memogem.coreapplication.Card;
import memogem.coreapplication.Tag;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import memogem.coreapplication.Set;

/** 
*Database-class is responsible for the management of the cards
*and their collections. This includes saving and loading the information
*to and from files.
*/

public class Database {
    
    private List<Set> sets;
    private List<Card> cardDatabase;
    private Map<Tag, List<Card>> tagDatabase;
    private Scanner scanner;
    private boolean deBugMode;
    private String dbAddress;
    
    public Database(String dbAddress) { //Address to the database as a parameter
        this(false, dbAddress);
    } 
    
    public Database(boolean debug, String dbAddress) {
        deBugMode = debug;
        this.sets = new ArrayList<>();
        this.cardDatabase = new ArrayList<>();
        this.tagDatabase = new HashMap<>();
        
        if (!connectDatabase(dbAddress)) {
            if (deBugMode) System.out.println("/n Initializing new database...");
            initializeEmptyDatabase();
        }
    }
    
    /**
     * Method tries to connect to the default SQL database.
     * Return false if the connection is not successful.
     * 
     * @param filex Given File-object that will be written onto.
     * @return boolean-type of object
     */
    public boolean connectDatabase(String dbAddress) {
        try {
            if (deBugMode) System.out.println("Connecting to database...");
            
            Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CardSet;");
            closeConnection(dbConnection, statement);
            
            if (deBugMode) System.out.println("Connection established to the db.");
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (deBugMode) System.out.println("Connection failed.");
        }
        return false;
    }
    /**
     * During startup this method is responsible for fetching all the necessary
     * information from the SQL-database.
     */
    public void readDatabase() {
        
        if (deBugMode) System.out.println("/n Running method readDatabase()...");
        try {
            DatabaseLoaderDAO dbLoader = new DatabaseLoaderDAO(sets, cardDatabase, tagDatabase, dbAddress);
            if (deBugMode) System.out.println("Importing cards...");
        } catch (SQLException se) {
            System.out.println(se.getMessage() + " Couldn't download database.");
        }
    }

    /**
     * Method adds a new card to the database. Returns false if the card is already in the database,
     * or if the card's text-fields are empty.
     * 
     * @param card Card-type of object that is added into the database.
     * @return Returns a boolean-type of object.
     */
    public boolean addNewCard(Card card) {
        if (deBugMode) System.out.println("/n Adding card " + card.getId() + " to the db.");
        if (!(card.getFront().isEmpty() && card.getBack().isEmpty() && !this.containsCard(card))) {
            
            try {
                CardDAO cardDao = new CardDAO(dbAddress);
                cardDao.add(card);
                if (deBugMode) System.out.println("/n Card added successfully.");
                return cardDatabase.add(card);
                
            } catch (SQLException se) {
                System.out.println(se.getMessage() + "couldn't add a new Card.");
            }
        } 
        return false;
    }
    
    /**
     * Method adds a new tag to the collection
     * 
     * @param tags List of tags hold by the Card-object 
     * @param taggedCard Card-object
     * 
     */
    public void addNewTag(List<Tag> tags, Card taggedCard) {
        if (deBugMode) System.out.println("/n Adding a list of tags to the db-list.");
        for (Tag tagx : tags) {
            boolean foundAnother = false;
            
            for (Tag tagy : tagDatabase.keySet()) {
                if (tagy.equals(tagx)) {
                    foundAnother = true;
                    if (deBugMode) System.out.println("Tag is already in the db.");
                    break;
                }
            }
            if (!foundAnother) {
                ArrayList<Card> tagList = new ArrayList<>();
                tagList.add(taggedCard);
                tagDatabase.put(tagx, tagList);
                if (deBugMode) System.out.println("Tag '" + tagx.getName() + "' added to the database.");
            }
        }
    }
    
    /**
     * Method adds a new set to the collection.
     * @param set Set-object
     */
    public boolean addNewSet(Set set) {
        if (!sets.contains(set)) {
            
            try {
                SetDAO setDao = new SetDAO(dbAddress);
                setDao.add(set);
                return sets.add(set);
                
            } catch (SQLException se) {
                System.out.println(se.getMessage() + "couldn't add a new set.");
            }
        }
        return false;
    }
    
    
    /**
     * Method takes a parameter of class Tag and searches the database for that exact object.
     * Returns true if the database contains the particular Tag.
     * 
     * @param tag Tag-object that will be searched for. 
     * 
     * @return returns true if the database contains the given Tag-object
     */
    public boolean containsTag(Tag tag) {
        return tagDatabase.containsKey(tag);
    }
    
    /**
     * Method takes a parameter of class Card and searches the database for that exact object.
     * Returns true if the database contains that particular Card-object.
     * 
     * @param eCard Given Card-object that needs to be checked.
     * @return Returns true if the database contains the given parameter.
     */
    public boolean containsCard(Card eCard) {
        return cardDatabase.contains(eCard);
    }
    
    public boolean saveAndCloseDatabase() {
        //not working yet
        return true;
    }
    
    /**
     * Method takes a parameter of class Set and searches the database for that exact object.
     * Returns true if the database contains that particular Set-object.
     * 
     * @param set Given Set-object that needs to be checked.
     * @return Returns true if the database contains the given object.
     */
    public boolean containsSet(Set set) {
        return sets.contains(set);
    }
    
    
    /**
     * Deletes the given card from the database. Returns false if the card is not in the database.
     * 
     * @param card Card-type object that will be deleted from the database.
     * @return boolean type object returned.
     */
    public boolean deleteCard(Card card) {
        if (containsCard(card)) {
            try {
                CardDAO cardDao = new CardDAO(dbAddress);
                cardDao.delete(card);
                return cardDatabase.remove(card);
                
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Prints out all the cards in a text-format.
     */
    public void printAllCards() {
        System.out.println("Printing all the cards...");
        for (Card cardx : cardDatabase) {
            System.out.println(cardx + "\n");
        }
    }
    
    public Map<Tag, List<Card>> getTagDatabase() {
        return tagDatabase;
    }

    public List<Card> getCardDatabase() {
        return cardDatabase;
    }

    public List<Set> getSets() {
        return sets;
    }
    
    private void closeConnection(Connection dbConnection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    /**
     * If the database can't be found during startup, this method initializes
     * new database.
     */
    private void initializeEmptyDatabase() {
//        InitDb initDb = new InitDb(dbAddress);
    }
}

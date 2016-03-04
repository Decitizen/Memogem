/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.carddatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import memogem.coreapplication.Card;
import memogem.coreapplication.CardStats;
import memogem.coreapplication.CardType;
import memogem.coreapplication.Set;
import memogem.coreapplication.Tag;

/**
 *Responsible for importing cards from SQL-database during the startup and exporting them
 * back to the database in the end. 
 */
public class DatabaseLoaderDAO implements Dao<Set> {
    
    private Connection dbConnection; // Connection to the database
    private List<Set> sets; // All of the sets included in the database
    private List<Card> cardDatabase; // All of the cards included in the database
    private Map<Tag, List<Card>> tagDatabase; // Cards in HashMap by their tags
    private String dbAddress; // link to the name of the database

    public DatabaseLoaderDAO(List<Set> sets, List<Card> cardDatabase, Map<Tag, List<Card>> tagDatabase, String dbAddress) throws SQLException {
        this.sets = sets;
        this.cardDatabase = cardDatabase;
        this.tagDatabase = tagDatabase;
        this.dbAddress = dbAddress;
    }

    public void connect() throws SQLException {
        this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbAddress + "");
    }
    
    private void addTagsToDb(List<Tag> cardTags, Card card) {
        for (Tag tag : cardTags) {
            if (tagDatabase.containsKey(tag)) {
                tagDatabase.get(tag).add(card);
                continue;
            } else {
                List<Card> listOfCards = new ArrayList<>();
                listOfCards.add(card);
                tagDatabase.put(tag, listOfCards);
            }
        }
    }

    @Override
    public void delete(Set key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(Set key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Set key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method takes care of loading the necessary information into the
     * local Database during program's startup. The whole operation will take
     * only 1 main SQL-query. But because of the structure tags need to be added
     * to the cards by individual SQL-queries (1per Card-object).
     * @return List of Set-objects.
     * @throws SQLException 
     */
    @Override
    public List<Set> getAll() throws SQLException {
        connect();
        Statement statement = dbConnection.createStatement();
        //SQL query:
        ResultSet rs = statement.executeQuery("SELECT * FROM CardSet s, Card c, StudySpeed ss "
                + "WHERE s.id = c.CardSetId "
                + "AND c.id = ss.CardId "
                + "ORDER BY c.CardSetId ASC, c.id ASC;");
        Set set = null;
        Card card = null;
        
        String cardSetId = null;
        String cardId = null;
        
        String studySpeedId = null;
        List<Card> cards = new LinkedList();
        List<Integer> studySpeeds = new LinkedList<>();
        List<Integer> studyDifficulty = new LinkedList<>();
        List<LocalDateTime> studyDates = new LinkedList<>();

        boolean newCardId = true;
        
        while (rs.next()) {
            newCardId = false;
            //if cardId has changed (or is null), new card-object will be created
            if (cardId == null || !(rs.getString("cardId").equals(cardId))) {
                newCardId = true;
                
                if (set != null) {
                    // card id has changed -> card's attributes will be collected together
                    CardStats tempStats = new CardStats(studySpeeds, studyDates, studyDifficulty, 
                                            (card.getFront().length() + card.getBack().length()));
                    card.setStats(tempStats);
                    studyDates = new ArrayList<>();
                    studySpeeds = new ArrayList<>();
                    studyDifficulty = new ArrayList<>();
                    set.addCard(card); //adding the ready card to the set
                    cardDatabase.add(card);
                }
                
                //extracting new card's basic information
                cardId = rs.getString("CardId");
                String front = rs.getString("Front");
                String back = rs.getString("Back");

                int cardType = rs.getInt("CardType");
                CardType ctype = checkCardType(cardType);
                card = new Card(back, front, back, ctype, null, null, null);
            }
            
            //if set has changed, extracting set's basic information
            if (cardSetId == null) {
                if (!rs.getString("CardSetId").equals(cardSetId)) {
                    cardSetId = rs.getString("CardSetId");
                    String cardSetName = rs.getString("Name");

                    //date conversion
                    String cardSetDateTime = rs.getString("LastTimeStudied");
                    LocalDateTime locDT = null;
                    if (!cardSetDateTime.isEmpty()) {
                        locDT = LocalDateTime.parse(cardSetDateTime,
                                DateTimeFormatter.ISO_DATE_TIME);
                    }
                    set = new Set(cardSetId, cardSetName, locDT);
                    sets.add(set);
                }
            }
            
            //extracting necessary tags
            if (newCardId) {
                ResultSet rsTag = statement.executeQuery("SELECT TagName FROM Card, CardTag "
                        + "WHERE Card.Id = CardTag.CardId AND CardTag.CardId = '" + cardId + "' "
                        + "ORDER BY CardTag.TagName ASC;");
                while (rsTag.next()) {
                    Tag newTag = new Tag(rsTag.getString("TagName").toLowerCase().trim());
                    card.addNewTag(newTag);
                    if (!tagDatabase.containsKey(newTag)) {
                        List<Card> cardsTagged = new ArrayList<>();
                        cardsTagged.add(card);
                        tagDatabase.put(newTag, cardsTagged);
                    } else {
                        tagDatabase.get(newTag).add(card);
                    }
                }
            }
                
            //extracting statistics
            if (studySpeedId == null || !(rs.getString("ssid").equals(studySpeedId))) {
                //set new id to studySpeedId for comparison purposes
                studySpeedId = rs.getString("ssid");
                if (!studySpeedId.isEmpty()) {
                    
                    Integer studySpeed1 = rs.getInt("Speed");
                    Integer studyDifficulty1 = rs.getInt("StudyDifficulty");
                    String studyDate1 = rs.getString("StudyDate");

                    if (studySpeed1 != null) {
                        studySpeeds.add(studySpeed1);
                        studyDifficulty.add(studyDifficulty1);
                    }

                    if (!studyDate1.isEmpty()) {
                        LocalDateTime locDT = LocalDateTime.parse(studyDate1, DateTimeFormatter.ISO_DATE_TIME);
                        studyDates.add(locDT);
                    }
                }
            }   
        }
        if (!cardDatabase.isEmpty()) {
            if (!(cardDatabase.get(cardDatabase.size()-1).equals(card))) {
                CardStats tempStats = new CardStats(studySpeeds, studyDates, studyDifficulty, 
                                            (card.getFront().length() + card.getBack().length()));
                card.setStats(tempStats);
                cardDatabase.add(card);
                set.addCard(card);
            }
        } 
        closeConnection(statement);
        return sets;
    }

    public CardType checkCardType(int cardType) {
        CardType ctype = null;
        if (cardType == 1) {
            ctype = CardType.FLASHCARD;
        } else {
            ctype = CardType.ANSWER;
        }
        return ctype;
    }
    
    private void closeConnection(Statement statement) {
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
}
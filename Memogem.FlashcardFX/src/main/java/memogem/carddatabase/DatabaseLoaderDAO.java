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
    
    private Connection dbConnection;
    private Statement statement;
    private List<Set> sets;
    private List<Card> cardDatabase;
    private Map<Tag, List<Card>> tagDatabase;
    private String dbAddress;
    

    public DatabaseLoaderDAO(List<Set> sets, List<Card> cardDatabase, Map<Tag, List<Card>> tagDatabase, String dbAddress) throws SQLException {
        connect();
        this.sets = sets;
        this.cardDatabase = cardDatabase;
        this.tagDatabase = tagDatabase;
        this.dbAddress = dbAddress;
    }

    public void connect() throws SQLException {
        this.dbConnection = DriverManager.getConnection("jcdb:sqlite:" + dbAddress + ".db");
        statement = dbConnection.createStatement();
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

    @Override
    public List<Set> getAll() throws SQLException {

        //SQL query:
        ResultSet rs = statement.executeQuery("SELECT * FROM CardSet s, Card c, CardTag ct, StudySpeed ss "
                + "WHERE s.id = c.CardSetId "
                + "AND c.id = ct.CardId "
                + "AND c.id = ss.CardId "
                + "ORDER BY c.CardSetId ASC, c.id ASC;");
        
        Set set = null;
        Card card = null;
        
        String cardSetId = null;
        String cardId = null;
        
        String studySpeedId = null;
        String tagName = null;
        List<Integer> studySpeeds = null;
        List<Integer> studyDifficulty = null;
        List<LocalDateTime> studyDates = null;
        List<Tag> cardTags = null;
        boolean newCardId = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        while (rs.next()) {
            newCardId = false;
            //if cardId has changed (or is null), new card-object will be created
            if (cardId == null || rs.getString("c.Id") != cardId) {
                
                newCardId = true;
                
                if (set != null) {
                    
                    // card id has changed -> card's attributes will be collected together
                    card.setTags(cardTags);
                    CardStats tempStats = new CardStats(studySpeeds, studyDates, studyDifficulty, 
                                            (card.getFront().length() + card.getBack().length()));
                    card.setStats(tempStats);
                    studyDates = new ArrayList<>();
                    studySpeeds = new ArrayList<>();
                    studyDifficulty = new ArrayList<>();
                    cardTags = new ArrayList<>();
                    set.addCard(card); //adding the ready card to the set
                }
                
                //extracting new card's basic information
                cardId = rs.getString("c.Id");
                String front = rs.getString("c.Front");
                String back = rs.getString("c.Back");

                int cardType = rs.getInt("c.CardType");
                CardType ctype = checkCardType(cardType);
                card = new Card(back, front, back, ctype, null, null, null);
            }
            
            //if set has changed, extracting set's basic information
            if (cardSetId == null || rs.getString("c.CardSetId") != cardSetId) {
                cardSetId = rs.getString("c.CardSetId");
                String cardSetName = rs.getString("s.Name");
                
                //date conversion
                String cardSetDateTime = rs.getString("s.LastTimeStudied");
                LocalDateTime locDT = null;
                if (!cardSetDateTime.isEmpty()) {
                    locDT = LocalDateTime.parse(cardSetDateTime, formatter);
                }
                set = new Set(cardSetId, cardSetName, locDT);
                sets.add(set);
            }
            
            //extracting tags
            if (tagName == null || rs.getString("ct.TagName") != tagName || newCardId) {
                tagName = rs.getString("ct.TagName");
                
                if (!tagName.isEmpty()) {
                    cardTags.add(new Tag(tagName));
                }
            }
            //extracting statistics
            if (studySpeedId == null || rs.getString("ss.id") != studySpeedId) {
                //set new id to studySpeedId for comparison purposes
                studySpeedId = rs.getString("ss.id");
                if (!studySpeedId.isEmpty()) {
                
                    Integer studySpeed1 = rs.getInt("ss.Speed");
                    Integer studyDifficulty1 = rs.getInt("ss.StudyDifficulty");
                    String studyDate1 = rs.getString("ss.StudyDate");

                    if (studySpeed1 != null) {
                        studySpeeds.add(studySpeed1);
                        studyDifficulty.add(studyDifficulty1);
                    }

                    if (!studyDate1.isEmpty()) {
                        LocalDateTime locDT = LocalDateTime.parse(studyDate1, formatter);
                        studyDates.add(locDT);
                    }
                }
            }   
        }
        closeConnection();
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
    
    private void closeConnection() {
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
    
    //    public List<Card> getAll2() throws SQLException {
//        SetDAO setDAO = new SetDAO(dbAddress);
//        sets.addAll(setDAO.getAll());
//        //SQL query:
//        ResultSet rs = statement.executeQuery("SELECT * FROM Card;");
//        
//        while (rs.next()) {
//            //extracting card's basic information
//            String id = rs.getString("id");
//            String setId = rs.getString("CardSetId");
//            String front = rs.getString("Front");
//            String back = rs.getString("Back");
//            int cardType = rs.getInt("CardType");
//            
//            CardType ctype = null;
//            if (cardType == 1) {
//                ctype = CardType.FLASHCARD;
//            } else {
//                ctype = CardType.ANSWER;
//            }
//            
//            //extracting card's Stats
//            CardStatsDAO cardStatsDAO = new CardStatsDAO(dbConnection, statement);
//            CardStats cardStats = cardStatsDAO.getByCardId(rs.getString(id));
//            
//            //extracting necessary tags
//            TagDAO tagDAO = new TagDAO(dbAddress);
//            List<Tag> cardTags = tagDAO.getByCardId(rs.getString(id));
//            
//            Set temporarySet = null;
//            //creating Card-object
//            for (Set set : sets) {
//                if (set.getId().equals(id)) {
//                    temporarySet = set;
//                }
//            }
//            Card card = new Card(id, front, back, ctype, cardTags, temporarySet, cardStats);
//            //adding
//            temporarySet.addCard(card);
//            addTagsToDb(cardTags, card);
//            
//        }
//        
//        closeConnection();
//        return cardDatabase;
//    }
}
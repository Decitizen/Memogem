
package memogem.carddatabase;

import memogem.coreapplication.Card;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import memogem.coreapplication.Tag;

/**
 *Is responsible for the importing and exporting of Card-objects to and from
 *SQL-database.
 */
public class CardDAO implements Dao<Card> {
    private Connection dbConnection; //Connection to SQL database
    private String dbAddress; //Address of the SQL database
    private boolean debug;
    
    public CardDAO(String dbAddress) throws SQLException {
        this.dbAddress = dbAddress;
    }
    /**
     * Connects to the SQL-database.
     * @throws SQLException if unable to connect
     */
    private void connect() {
        try {
            this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbAddress + "");
            if (debug) System.out.println("CardDAO: Connection created successfully.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + " COULDN'T CREATE CONNECTION!");
        }
    }
    /**
     * Method closes the SQL-connection.
     */
    private void closeConnection() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    
    /**
     * Responsible for deleting card from the SQL-database.
     * @param card
     * @throws SQLException 
     */
    @Override
    public void delete(Card card) throws SQLException {
        connect();
        Statement statement = dbConnection.createStatement();
        String id = card.getId();
        
        //first delete card's stats, CardTags and then the Card itself
        statement.executeQuery("PRAGMA foreign_keys = ON;");
        
        statement.executeQuery("DELETE FROM Card WHERE id = '" + id + "';");
        statement.close();
        closeConnection();
    }
    /**
     * Responsible for adding card to the SQL-database.
     * @param card
     * @throws SQLException 
     */
    @Override
    public void add(Card card) throws SQLException {
        this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbAddress + "");
        if (debug) System.out.println("CardDAO: Connection created successfully.");
        Statement statement = dbConnection.createStatement();
        if (debug) System.out.println("CardDAO: Statement created successfully.");
        statement.executeUpdate("INSERT INTO Card(id, CardSetId, Front, Back, CardType) "
                + "VALUES " // Add Cards
                + "('" + card.getId() + "', "
                + "'" + card.getSet().getId() + "', "
                + "'" + card.getFront() + "', "
                + "'" + card.getBack() + "', "
                + "" + card.getType().getCode() + ");");
        
        if (!card.getStats().getHowfast().isEmpty()) { // if any, add card's stats
            for (int i = 0; i < card.getStats().getHowfast().size(); i++) {
                statement.executeUpdate("INSERT INTO StudySpeed(CardId, Speed, StudyDate, StudyDifficulty) "
                        + "VALUES "
                        + "('" + card.getId() + "', "
                        + "'" + card.getStats().getHowfast().get(i) + "', "
                        + "'" + createDateTime(card.getStats().getStudyDates().get(i)) + "', "
                        + "'" + card.getStats().getStudyDifficulty().get(i) + "');");
            }
        }
        if (!card.getTags().isEmpty()) { // if any, add card's tags            
            for (Tag tagx : card.getTags()) {
                try {
                statement.executeUpdate("INSERT INTO CardTag(CardId, TagName) "
                        + "VALUES "
                        + "('" + card.getId() + "', "
                            + "'" + tagx.getName() + "');");
                } catch (SQLException se) {
                    statement.executeUpdate("INSERT INTO Tag(TagName) "
                            + "VALUES ('" + tagx.getName() + "');");
                    statement.executeUpdate("INSERT INTO CardTag(CardId, TagName) "
                            + "VALUES "
                            + "('" + card.getId() + "', "
                            + "'" + tagx.getName() + "');");
                }
            }
        }
        statement.close();
        closeConnection();
    }
    /**
     * Update function not supported.
     * @param card
     * @throws SQLException 
     */
    @Override
    public void update(Card card) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * getAll is not supported.
     * @return
     * @throws SQLException 
     */
    @Override
    public List<Card> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    void deleteCardsBySetId(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    private String createDateTime(LocalDateTime localDT) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDT.format(formatter);
    }
    
}

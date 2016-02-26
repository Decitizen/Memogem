
package memogem.carddatabase;

import memogem.coreapplication.Card;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *Is responsible for the importing and exporting of Card-objects to and from
 *SQL-database.
 */
public class CardDAO implements Dao<Card> {
    private Connection dbConnection;
    private Statement statement;
    private String dbAddress;
    
    public CardDAO(String dbAddress) throws SQLException {
        connect();
        this.dbAddress = dbAddress;
    }

    public void connect() throws SQLException {
        this.dbConnection = DriverManager.getConnection("jcdb:sqlite:" + dbAddress + ".db");
        statement = dbConnection.createStatement();
    }
    
    @Override
    public void delete(Card card) throws SQLException {
        String id = card.getId();
        
        statement.executeQuery("DELETE FROM Card WHERE id = '" + id + "';");
        closeConnection();
    }

    @Override
    public void add(Card card) throws SQLException {
        
        statement.executeQuery("INSERT INTO Card(id, CardSetId, Front, Back, CardType) "
                + "VALUES "
                + "('" + card.getId() + "', "
                + "'" + card.getSet().getId() + "', "
                + "'" + card.getFront() + "', "
                + "'" + card.getBack() + "', "
                + "'" + card.getType().getCode() + "');");
        closeConnection();
    }

    @Override
    public void update(Card card) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    void deleteCardsBySetId(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Card> getAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

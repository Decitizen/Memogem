
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
import memogem.coreapplication.Card;
import memogem.coreapplication.Set;

/**
 *Is responsible for the importing and exporting of Set-objects to and from
 *SQL-database.
 */
public class SetDAO implements Dao<Set> {
    private Connection dbConnection; //Connection to SQL database
    private Statement statement; //Used to make queries to the SQL database
    private String dbAddress; //Address of the SQL database

    public SetDAO(String dbAddress) {
        this.dbAddress = dbAddress;
    }

    public void connect() {
        try {
            this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbAddress + "");
            statement = dbConnection.createStatement();
        }catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    
    /**
     * Delete set from the SQL-database.
     * @param set
     * @throws SQLException 
     */
    @Override
    public void delete(Set set) throws SQLException {
        connect();
        //deletes everything because of the ON DELETE CASCADE
        statement.executeUpdate("DELETE FROM CardSet WHERE Name = '" + set.getName() + "';");
        closeStatementAndConnection();
    }

    /**
     * Adds new Set-object to the SQL-database.
     * @param set
     * @throws SQLException 
     */
    @Override
    public void add(Set set) throws SQLException {
        connect();
        if (set.getLastTimeStudied() != null) {
            statement.executeUpdate("INSERT INTO CardSet(Id, Name, LastTimeStudied) VALUES "
                    + "('" + set.getId() + "', "
                    + "'" + set.getName() + "', "
                    + "'" + set.getLastTimeStudied() + "');");
        } else {
            statement.executeUpdate("INSERT INTO CardSet(Id, Name) VALUES "
                    + "('" + set.getId() + "', "
                    + "'" + set.getName() + "');");
        } 
        closeStatementAndConnection();
    }

    /**
     * Update not supported
     */
    @Override
    public void update(Set key) throws SQLException {
        //not supported
    }
    
    /**
     * Returns all the sets stored into the SQL database.
     * @return List of Set-objects
     * @throws SQLException 
     */
    @Override
    public List<Set> getAll() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Set;");
        List<Set> sets = new ArrayList<>();
        
        while(resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("Name");            
            String dateString = resultSet.getString("LastTimeStudied");
            
            //formatting and extracting date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime locDT = (LocalDateTime) formatter.parse(dateString);
            
            Set queriedSet = new Set(id, name, locDT);
            sets.add(queriedSet);
        }
        return sets;
    }
    /**
     * Closes connection to the SQL-database.
     */
    private void closeStatementAndConnection() {
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

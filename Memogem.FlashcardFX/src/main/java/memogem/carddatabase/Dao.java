
package memogem.carddatabase;

import java.sql.SQLException;
import java.util.List;

/**
 *Implemented by classes querying the SQL database.
 * 
 */
public interface Dao<T> {
    /**
     * Removes given key from the SQL database.
     * @param key Generic (Card, Set, Tag, CardStats)
     * @throws SQLException 
     */
    void delete(T key) throws SQLException;

    /**
     *Adds given key to the SQL database. Takes generic objects.
     * @param key Generic (Card, Set, Tag, CardStats)
     * @throws SQLException
     */
    void add(T key) throws SQLException;
    
    /**
     * Updates given key in the SQL database. Takes generic objects.
     * @param key Generic (Card, Set, Tag, CardStats)
     * @throws SQLException 
     */
    void update(T key) throws SQLException;
    
    /**
     * Exports all of the given generic-type objects from the database.
     * @return List of given key (Implementation ArrayList)
     * @throws SQLException 
     */
    List<T> getAll() throws SQLException;
    
}

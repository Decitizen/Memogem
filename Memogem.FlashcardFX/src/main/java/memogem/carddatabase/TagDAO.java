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
import java.util.ArrayList;
import java.util.List;
import memogem.coreapplication.Tag;

/**
 *Is responsible for the importing and exporting of Tag-objects to and from
 *SQL-database.
 */
public class TagDAO implements Dao<Tag> {
    private Connection dbConnection;
    private Statement statement;
    private String dbAddress;

    public TagDAO(String dbAddress) {
        this.dbAddress = dbAddress;
    }

    public void connect() {
        try {
            this.dbConnection = DriverManager.getConnection("jcdb:sqlite:" + dbAddress + ".db");
            statement = dbConnection.createStatement();
        }catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    
    public List<Tag> getByCardId(String CardId) throws SQLException {
        connect();
        
        List<Tag> tagList = new ArrayList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM CardTag, Card "
                + "WHERE Tag.CardId = Card.id AND Tag.CardId = '" + CardId + "';");
        closeConnection();
        
        while (rs.next()) {
            Tag tag = new Tag(rs.getString("Name"));
            tagList.add(tag);
        }
        return tagList;
    }
    
    @Override
    public void delete(Tag tag) throws SQLException {
        connect();
        String name = tag.getName();
        statement.executeQuery("DELETE FROM Tag WHERE Name = '" + name + "';");
        closeConnection();
    }
    
    @Override
    public void add(Tag tag) throws SQLException {
        connect();
        String name = tag.getName();
        statement.executeQuery("INSERT INTO Tag(Name) VALUES ('" + name + "');");
        closeConnection();
    }

    @Override
    public void update(Tag tag) throws SQLException {
        //not supported
    }

    @Override
    public List<Tag> getAll() throws SQLException {
        connect();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Tag;");
        closeConnection();
        List<Tag> tags = new ArrayList<>();
        
        while(resultSet.next()) {
            tags.add(new Tag(resultSet.getString("Name")));
        }
        return tags;
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

    
    
}

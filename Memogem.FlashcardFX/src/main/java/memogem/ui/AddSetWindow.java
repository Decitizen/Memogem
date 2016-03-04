/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import memogem.carddatabase.Database;
import memogem.coreapplication.CardEngine;
import memogem.coreapplication.Set;
import static memogem.ui.MainWindow.closeProgram;

/**
 * This class is responsible for the functionality and layout of
 * the window where the user can add new Sets to the database.
 */
public class AddSetWindow {
    private Database database; //Session's database
    private Stage addSetWindow; // Primary stage of this window

    public AddSetWindow(Database database, Stage addSetWindow) {
        this.database = database;
        this.addSetWindow = new Stage();
    }
    
    public void createAddSetWindow() {
        //Give title
        addSetWindow.setTitle("MemoGem - Add Set");
        
        //Set action on close
        addSetWindow.setOnCloseRequest(e -> {
            closeProgram(addSetWindow, database);
        });
        
        //Set window-modality
        addSetWindow.initModality(Modality.APPLICATION_MODAL);
        
        //Create layout
        createComponents();
    }
    
    private void createComponents() {
        //Create Label, textfield and Finish-button for adding new set's name
        Label labelSetName = new Label("Set Name: ");
        
        TextField setNameField = new TextField();
        setNameField.minWidth(100);
        
        Button finishButton = new Button("Finish");
        finishButton.setOnAction(e -> {
            saveSet(setNameField.getText());
        });
        
    }

    private void saveSet(String text) {
        Set newSet = new Set(text);
        if (!database.containsSet(newSet)) {
            database.addNewSet(newSet);
        } else {
            
        }
    }
    
    
    
}

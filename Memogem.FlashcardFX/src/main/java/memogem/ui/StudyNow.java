/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import memogem.carddatabase.Database;
import memogem.coreapplication.Card;
import memogem.coreapplication.CardEngine;
import memogem.coreapplication.Set;

    
/**
 * Class is responsible for creating the studyWindow. Takes advantage of 
 * FXML and CSS files.
 */
public class StudyNow {
    
    private Database database; //Session's database
    private CardEngine cEngine; // CardEngine that handles all the card's operations
    private Set currentSet; // Holds the current set under study
    private Stage studyWindow; // Holds current Stage-object
    private Card currentCard; // Holds currently studied card

    
    public StudyNow(Database database, CardEngine cEngine, Set currentSet) {
        this.database = database;
        this.cEngine = cEngine;
        this.currentSet = currentSet;
        studyWindow = new Stage();
    }
    public void createStudyWindow() throws Exception {
        FXMLDocumentController studyController = new FXMLDocumentController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLDocumentB.fxml"));
        loader.setController(studyController);
        
        
        Parent rootBefore = loader.load();
        
        
        Scene scene1 = new Scene(rootBefore);

        studyWindow.setScene(scene1);
        scene1.getStylesheets().add
        (StudyNow.class.getResource("/StudyNowScene.css").toExternalForm());
        
        studyWindow.setResizable(false);
        cEngine.studySet(currentSet);
        cEngine.calculateCardOrder();
        currentCard = cEngine.next();
        eventHandling(studyController);
        currentCard.getStats().startStudying();
        studyWindow.showAndWait();
    }

    public CardEngine getcEngine() {
        return cEngine;
    }

    public Set getCurrentSet() {
        return currentSet;
    }

    public Database getDatabase() {
        return database;
    }

    private void eventHandling(FXMLDocumentController sController) {
        Button buttonShow = sController.getButtonShowAnswer();
        sController.label.setText(currentCard.getFront());
        buttonShow.setOnAction(e -> {
            sController.label2.setText(currentCard.getBack());
            changeScene(true);
            currentCard.getStats().stopStudying();
        });
        sController.buttonAgain.setOnAction(e -> {
            currentCard.getStats().addNewDifficulty(-2);
            currentCard = cEngine.next();
            currentCard.getStats().startStudying();
            changeScene(false);
        });
        
        sController.buttonGood.setOnAction(e -> {
            currentCard.getStats().addNewDifficulty(0);
            currentCard = cEngine.next();
            currentCard.getStats().startStudying();
            changeScene(false);
        });
        
        sController.buttonEasy.setOnAction(e -> {
            currentCard.getStats().addNewDifficulty(2);
            currentCard = cEngine.next();
            currentCard.getStats().startStudying();
            changeScene(false);
        });
        
        sController.buttonEnd.setOnAction(e -> {
            studyWindow.close();
        });
        
    }
    
    public void changeScene(boolean isItFrontView) {
        Stage stage;
        Parent root = null;
        if (isItFrontView) {
            try {
                root = FXMLLoader.load(getClass().getResource("/FXMLDocumentA.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                root = FXMLLoader.load(getClass().getResource("/FXMLDocumentB.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(StudyNow.class.getResource("/StudyNowScene.css").toExternalForm());
        studyWindow.setScene(scene);
        studyWindow.show();
    }
   
    
    

    
    
}

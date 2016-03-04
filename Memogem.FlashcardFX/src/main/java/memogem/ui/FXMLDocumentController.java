/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import memogem.carddatabase.Database;
import memogem.coreapplication.CardEngine;
import memogem.coreapplication.Set;
import memogem.ui.StudyNow;

/**
 * FXML Controller class
 *
 * @author Willburner
 */
public class FXMLDocumentController implements Initializable {
    
    public Label label;
    public Label label2;
    public Button buttonShowAnswer;
    public Button buttonEnd;
    public Button buttonGood;
    public Button buttonEasy;
    public Button buttonAgain;
    public Stage parentStage;

    public FXMLDocumentController() {
    }
   
    public void handleButtonAction2(ActionEvent event) throws IOException {
//            Stage stage;
//            Parent root;
//            if (event.getSource() == buttonShowAnswer) {
//                //get reference to the button's stage         
//                stage = (Stage) buttonShowAnswer.getScene().getWindow();
//                //load up OTHER FXML document
//                root = FXMLLoader.load(getClass().getResource("/FXMLDocumentA.fxml"));
//            } else {
//                stage = (Stage) buttonGood.getScene().getWindow();
//                if (event.getSource() == buttonGood) {
//                    //Add new stats
//                }
//                if (event.getSource() == buttonEasy) {
//                    //Add new stats
//                }
//                if (event.getSource() == buttonAgain) {
//                    //Add new stats
//                }
//                
//                root = FXMLLoader.load(getClass().getResource("/FXMLDocumentB.fxml"));
//            }
//            //create a new scene with root and set the stage
//            Scene scene = new Scene(root);
//            scene.getStylesheets().add
//            (StudyNow.class.getResource("/StudyNowScene.css").toExternalForm());
//            stage.setScene(scene);
//            stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        parentStage.setScene(scene);
        parentStage.show();
    }
    
    public void setOnActions(Stage parentStage) {
        this.parentStage = parentStage;
        buttonGood.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(false);
            }
        });
        
        buttonEasy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(false);
            }
        });
        
        buttonAgain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(false);
            }
        });
        
        buttonEnd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parentStage.close();
            }
        });
        
        buttonShowAnswer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(true);
            }
        });
        
    }

    public Button getButtonAgain() {
        return buttonAgain;
    }

    public Button getButtonEasy() {
        return buttonEasy;
    }

    public Button getButtonEnd() {
        return buttonEnd;
    }

    public Button getButtonGood() {
        return buttonGood;
    }

    public Button getButtonShowAnswer() {
        return buttonShowAnswer;
    }

    public Label getLabel() {
        return label;
    }

    public Label getLabel2() {
        return label2;
    }
    
    
    
    
}

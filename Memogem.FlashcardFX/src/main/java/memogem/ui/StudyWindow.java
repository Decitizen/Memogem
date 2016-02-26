/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.ui;

import java.awt.event.KeyEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;
import memogem.carddatabase.Database;
import memogem.coreapplication.Card;
import memogem.coreapplication.CardEngine;
import memogem.coreapplication.Set;
import static memogem.ui.MainWindow.closeProgram;

/**
 *Creates the primary window where the cards are viewed and studied.
 */
public class StudyWindow {
    private Database database; // Session's database
    private Stage studyWindow; // Primary stage of this window
    private BorderPane borderPane; // Borderpane, parent of other layouts
    private CardEngine cEngine;  // CardEngine that handles all the card's operations
    private Set currentSet;  // Holds the current set under study

    public StudyWindow(Database database, Stage editWindow, CardEngine cEngine, Set currentSet, int studymode) {
        this.database = database;
        this.cEngine = cEngine;
        this.studyWindow = new Stage();
        this.borderPane = new BorderPane();
    }
    /**
     * Creates new window for the actual studying of the cards.
     */
    public void createSWindow() {
        //Give title
        studyWindow.setTitle("MemoGem - Study Set");
        
        //Set action on close
        studyWindow.setOnCloseRequest(e -> {
            closeProgram(studyWindow, database);
        });
        
        //Set window-modality
        studyWindow.initModality(Modality.APPLICATION_MODAL);
        
        //Create layout
        createComponents();
        
        //Create scene-object
        Scene scene = new Scene(borderPane, 600, 700);
        borderPane.centerProperty();
        borderPane.autosize();
        
        //Set scene
        cEngine.setStudyMode(1);
        cEngine.studySet(currentSet);
        cEngine.calculateCardOrder();
        studyWindow.setScene(scene);
        studyWindow.showAndWait();
        
    }
    
    private void createComponents() {
        //Create Labels
        Label labelFront = new Label("Front");
        labelFront.setPadding(new Insets(20));
        Label labelBack = new Label("Back");
        labelBack.setPadding(new Insets(20));
        
        //Create text-fields
        Text frontText = new Text();
        frontText.setFont(new Font(16));
        frontText.setFill(Color.DARKGRAY);
        Text backText = new Text("");
        backText.setFont(new Font(16));
        backText.setFill(Color.DARKGRAY);
        
        //Create necessary options-buttons
        Button endButton = new Button("End Session");
        Button editButton = new Button("Edit Card");
        
        //Hbox to populate options
        HBox optionsBox = new HBox(20);
        optionsBox.setPadding(new Insets(5, 15, 5, 15));
        optionsBox.getChildren().addAll(endButton, editButton);
        
        //Create timer - text and Pause/Start Timer button
        Text timer = new Text("Timer here");
        Button pauseTimerButton = new Button("Pause Timer");
        pauseTimerButton.setOnAction( e -> {
            toggleTimerButton(pauseTimerButton.getText(), pauseTimerButton);
        });
        
        //Create necessary study-buttons
        Button showAnswerButton = new Button("Show Answer");
        Button againButton = new Button("Again");
        Button goodButton = new Button("Good");
        Button easyButton = new Button("Easy");
        easyButton.setOnAction(e -> {
            changeCards(backText, frontText);
        });
        goodButton.setOnAction(e -> {
            changeCards(backText, frontText);
        });
        againButton.setOnAction(e -> {
            changeCards(backText, frontText);
        });
        
        againButton.setMinWidth(50);
        goodButton.setMinWidth(50);
        
        //Action when finishing
        endButton.setOnAction(e -> {      
            studyWindow.close();
            });
        
        //Create vertical box for the center
        VBox centerLayout = new VBox(8);
        centerLayout.setAlignment(Pos.CENTER);
        
        //Create separator
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setMaxWidth(200);
        centerLayout.getChildren().addAll(labelFront, 
                                        frontText, separator, labelBack, backText, timer);
        centerLayout.setPadding(new Insets(20, 50, 20, 20));
        
        //Create Hbox for show answer-button
        HBox answerBox = new HBox(20);
        answerBox.setPadding(new Insets(5, 15, 5, 15));
        answerBox.getChildren().addAll(showAnswerButton, pauseTimerButton);
        
        //Create Hbox for review buttons
        HBox hboxStudy2 = new HBox(20);
        hboxStudy2.setPadding(new Insets(5, 15, 5, 15));
        hboxStudy2.getChildren().addAll(againButton, goodButton, easyButton);
        
        //Action that shows the back, stats and review options
        showAnswerButton.setOnAction( e -> {
            backText.setText("Æthelstan");
            borderPane.setBottom(hboxStudy2);
         });
        borderPane.setBottom(answerBox);
        borderPane.setCenter(centerLayout);
        borderPane.setTop(optionsBox);
    }

    public void changeCards(Text backText, Text frontText) {
        Card newCard = cEngine.next();
        if (newCard != null) {
            backText.setText("");
            frontText.setText(newCard.getFront());
        } else {
            studyWindow.close();
        }
    }
    
    public void toggleTimerButton(String state, Button button) {
        if (state.equals("Pause")) {
            button.setText("Continue");
            // add here link to timer from CardEngine
        } else {
            button.setText("Pause");
            // add here link to timer from CardEngine
        }
    }
}    

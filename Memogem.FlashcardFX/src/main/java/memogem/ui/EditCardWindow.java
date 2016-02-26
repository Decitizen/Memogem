
package memogem.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import memogem.carddatabase.Database;
import memogem.coreapplication.Card;
import memogem.coreapplication.CardEngine;
import memogem.coreapplication.Set;
import memogem.coreapplication.Tag;
import static memogem.ui.MainWindow.closeProgram;

/**
 * This class is responsible for the functionality and layout of
 * the window where the user can add or edit new Cards to/in the database.
 */

public class EditCardWindow {
    private Database database;
    private Stage editWindow;
    private GridPane gridPane;
    private CardEngine cEngine;
    private Set currentSet;
    
    public EditCardWindow(Database database, CardEngine cEngine, Set currentSet) {
        this.database = database;
        this.editWindow = new Stage();
        this.gridPane = new GridPane();
        this.cEngine = cEngine;
        this.currentSet = currentSet;
    }
    
    public void createMWindow() {
        //Give title
        editWindow.setTitle("MemoGem - Edit Card");
        
        //Set action on close
        editWindow.setOnCloseRequest(e -> {
            closeProgram(editWindow, database);
        });
        
        //Set window-modality
        editWindow.initModality(Modality.APPLICATION_MODAL);
        
        //Create layout
        createComponents();
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setVgap(8);
        gridPane.setHgap(10);
        
        //Create scene-object
        Scene scene = new Scene(gridPane, 400, 430);
        
        //Set scene
        editWindow.setScene(scene);
        editWindow.showAndWait();
        
    }
    
    private void createComponents() {
        //Create Labels
        Label labelWriteFront = new Label("Front");
        Label labelWriteBack = new Label("Back");
        
        //Create input
        TextArea frontTArea = new TextArea();
        TextArea backTArea = new TextArea();
        
        frontTArea.setMinHeight(100);
        backTArea.setMinHeight(150);
        frontTArea.setMinWidth(200);
        frontTArea.setPromptText("Write description or a question. Be mindful about the length.");
        backTArea.setPromptText("Write your best answer here.");
        
        //Create textfield for tags
        TextField tagField = new TextField();
        tagField.setMinWidth(50);
        
        //Create List for the added tags
        List<String> tags = new LinkedList<>();
        
        //Create necessary buttons
        Button finishButton = new Button("Finish");
        Button cancelButton = new Button("Cancel");
        Button tagButton = new Button("Add Tag");
        tagButton.setPadding(new Insets(5));
        tagButton.setPadding(new Insets(5));
        
        tagButton.setOnAction(e -> {
            tags.add(tagField.getText());
            tagField.setText("");
        });
        
        cancelButton.setOnAction(e -> editWindow.close());
        
        //Create HBox for Tag-button and field
        HBox tagHbox = new HBox();
        tagHbox.setPadding(new Insets(5, 5, 5, 5));
        tagHbox.getChildren().addAll(tagField, tagButton);
        
        
        
        //Create Hbox for buttons
        HBox hbox = new HBox(20);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.getChildren().addAll(finishButton, cancelButton);
        
        //Create gridlayout/populate the window
        GridPane.setConstraints(labelWriteFront, 0, 0);
        GridPane.setConstraints(frontTArea, 0, 1);
        GridPane.setConstraints(labelWriteBack, 0, 2);
        GridPane.setConstraints(backTArea, 0, 3);
        GridPane.setConstraints(tagHbox, 0, 4);
        GridPane.setConstraints(hbox, 0, 5);
        
        //Create action when finishing
        finishButton.setOnAction(e -> {
            saveCard(frontTArea.getText(), backTArea.getText(), tags);
            editWindow.close();
            });
        
        gridPane.getChildren().addAll(labelWriteFront, labelWriteBack, 
                                frontTArea, backTArea, tagHbox, hbox);
    }

    /**
     * Saves newly created card to the database as well as to the currentSet-object.
     * @param front Card's front text.
     * @param back Card's back text.
     * @param tags List of tags.
     */
    private void saveCard(String front, String back, List<String> tags) {
        
        if (front.isEmpty() && back.isEmpty()) {
            return;
        }
        //Create new Card-object
        Card newCard = new Card(front, back);
        
        //Add tags
        if (!tags.isEmpty()) {
            List<String> tags2 = splitTagNames(tags);
            for (String tagName : tags2) {
                newCard.addNewTag(new Tag(tagName.trim().toLowerCase()));
            }
        }
        currentSet.addCard(newCard);
        database.addNewCard(newCard);
    }
    
    private List<String> splitTagNames(List<String> tagNames) {
        List<String> singleTags = new ArrayList<>();
        if (!tagNames.isEmpty()) {
            return singleTags;
        }
        
        for (String name : tagNames) {
            if (name.contains(",")) {
                
                String[] tagNamesSplitted = name.split(",");
                for (String splitTags : tagNamesSplitted) {
                    singleTags.add(splitTags);
                }
                
            } else {
                singleTags.add(name);
            }
        }
        return singleTags;
    }
    
    /**
     * Draws the edit card-window
     */
    public void show() {
        editWindow.show();
    }
}

package memogem.ui;


import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *Works as a splash screen at the startup. Shows progressbar.
 */
public class WelcomeBox {
    
    public static void display(String title, String message) {
        Stage wmWindow = new Stage();
        
        wmWindow.initModality(Modality.APPLICATION_MODAL);
        wmWindow.setTitle(title);
        wmWindow.setMinWidth(250);
        wmWindow.setMinHeight(290);
        
        Label label = new Label();
        label.setText(message);
        Image img = new Image("file:src/main/resources/images/Memogem_logo.jpg");
        ImageView imgView = new ImageView(img);
        
        //Create progressbar
        ProgressBar loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(100);
        
        
        Label progressText = new Label("Loading database...");
        progressText.setPadding(new Insets(0, 0, 5, 0));
        progressText.setAlignment(Pos.CENTER);
        BorderPane bPane = new BorderPane();
        
        
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(imgView, loadProgress, progressText);
        layout.setAlignment(Pos.BASELINE_CENTER);
        
        Scene scene = new Scene(layout);
        wmWindow.setScene(scene);
        wmWindow.showAndWait();
        wmWindow.close();
        
    }
}
  

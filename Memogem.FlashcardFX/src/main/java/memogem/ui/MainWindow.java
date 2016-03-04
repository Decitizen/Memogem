
package memogem.ui;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.REMAINING;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import memogem.carddatabase.Database;
import memogem.coreapplication.Card;
import memogem.coreapplication.CardEngine;
import memogem.coreapplication.Set;

/**
 *Creates the program's main "Control Panel"-window.
 */
public class MainWindow {
    private Database database; // Session's database
    private Stage mainWindow; // Primary stage of this window
    private GridPane gridPane; // GridPane for the top elements, child of borderpane
    private BorderPane borderPane; // Borderpane, parent of other layouts
    private CardEngine cEngine; // CardEngine that handles all the card's operations
    private Set currentSet; // Holds the current set under study
    
    private ChoiceBox<String> setNames;
    private Button addSet;
    private Button addCard;
    private Button studyNow;
    
    final NumberAxis xAxis = new NumberAxis(1, 31, 1);
    final NumberAxis yAxis = new NumberAxis();
 
    public MainWindow(Stage mainWindow, Database database, CardEngine cEngine) {
        this.database = database;
        this.cEngine = cEngine;
        this.mainWindow = mainWindow;
        this.gridPane = new GridPane();
        currentSet = null;
    }
    /**
     * Creates new Main-window
     */
    public void createMWindow() {
        //Give title
        
        mainWindow.setTitle("MemoGem - Control Panel");
        
        //Set action on close
        mainWindow.setOnCloseRequest(e -> {
            closeProgram(mainWindow, database);
        });
        //calculate last studytimes for sets
        for (Set set : database.getSets()) {
            set.calculateLastTimeModified();
        }
        
        //Create layout
        createComponents();
        
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setVgap(4);
        gridPane.setHgap(8);
        
        //Create scene-object
        Scene scene = new Scene(borderPane, 980, 760);
        
        //Set scene
        mainWindow.setScene(scene);
        
        //Show welcome screen
        WelcomeBox.display("MemoGem", "Welcome to Memogem");
    }
    
    /**
     * Creates all the necessary graphical components of this window
     */
    public void createComponents() {
        //Create Labels
        Label chooseSetLabel = new Label("Choose topic:");
        
        //Create choicebox
        setNames = new ChoiceBox<>();
        if (!database.getSets().isEmpty()) {
            currentSet = database.getSets().get(0);
        }
        //Fetch set-names:
        
        if (currentSet != null) {
            setNames.setValue(currentSet.getName());
        } else {
            currentSet = new Set("<No sets>");
        }
        updateChoiceBox();
        setNames.setMinWidth(100);
        setNames.setMaxWidth(300);
        
        //Create buttons
        addSet = new Button("Add Set");
        addCard = new Button("Add Card");
        studyNow = new Button("Study Set");
        
        //Create MenuBar
        MainMenubar mainMenuBar = new MainMenubar(database, cEngine, mainWindow);
        MenuBar menuBar = mainMenuBar.createMenuBar();
        
        //Create piechart
        PieChart pieChart = pieChart();
        
        //Add actions to buttons
        addCard.setOnAction(e -> {
            EditCardWindow editWindow = new EditCardWindow(database, cEngine, currentSet);
            editWindow.createMWindow();
            updateChoiceBox();
            setNames.setValue(currentSet.getName());
        });
        
        //"Add Set"- action
        addSet.setOnAction(e -> createAddSetWindow(pieChart, setNames));
        
        //Create Hbox for buttons
        HBox hbox = new HBox(20);
        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.getChildren().addAll(addSet, addCard, studyNow);
        
        //Setup gridpane span and column constraints
        GridPane.setColumnSpan(setNames, 100);
        GridPane.setColumnSpan(menuBar, 5);
        
        GridPane.setMargin(setNames, new Insets(0, 0, 0, 25));
        GridPane.setMargin(chooseSetLabel, new Insets(0, 0, 0, 25));
        
        GridPane.setColumnSpan(chooseSetLabel, 100);
        GridPane.setColumnSpan(menuBar, REMAINING);
        
        GridPane.setConstraints(chooseSetLabel, 0, 1);
        GridPane.setConstraints(setNames, 0, 2);
        GridPane.setConstraints(menuBar, 0, 0);
        
        //Populate gridPane
        gridPane.getChildren().addAll(menuBar, setNames, chooseSetLabel);
        
        //Create area chart for statistics
        StackedAreaChart<Number, Number> studyChart = areaChart();
        
        //Listen for selection changes
        setNames.getSelectionModel().selectedItemProperty().addListener
        ((v, oldValue, newValue) -> {
            mainWindow.setScene(new Scene(new BorderPane(), 980, 760));
            mainWindow.show();
            setSelectedSet(newValue, database.getSets());
            StackedAreaChart<Number, Number> aC = areaChart();
            borderPane = new BorderPane(pieChart, gridPane, areaChart(), hbox, null);
            mainWindow.setScene(new Scene(borderPane, 980, 760));
            mainWindow.show();
        });
        
        
        
        //Study now! action
        studyNow.setOnAction(e -> {
            String alertText = null;
            if (!(currentSet != null || currentSet.getCards().isEmpty())) {
                StudyNow studyWindowNew = new StudyNow(database, cEngine, currentSet);
                try {
                    studyWindowNew.createStudyWindow();
                    return;
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (currentSet == null) {
                alertText = "Select proper set first";
            } else {
                alertText = "Set is empty. Add cards.";
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Nothing to study here.");
            alert.setContentText(alertText);
            alert.showAndWait();
            });
        
        final Tooltip tooltipStudyNow = new Tooltip();
        tooltipStudyNow.setText("Begin studying the selected set.");
        studyNow.setTooltip(tooltipStudyNow);
        
        //Create parent layout, borderpane
        borderPane = new BorderPane(pieChart, gridPane, studyChart, hbox, null);
        BorderPane.setMargin(hbox, new Insets(15));
    }
    

    /**
     * Populates piechart with the AVG difficulties of the sets.
     * @return Java FX PieChart object
     */
    public PieChart pieChart() {
        //Create piechart for statistics
        String difficulty = currentSet.getAVGDifficultyAsString();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Set set : database.getSets()) {
            if (!set.getCards().isEmpty()) {
                pieChartData.add(new PieChart.Data(set.getName(),
                        set.calculateOverallTimeStudied()));
            }
        }
        final PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Aggregate Time Studied (/Set)");
        pieChart.autosize();
        return pieChart;
    }
    
    public static void closeProgram(Stage mainWindow, Database database) {
        //Save database and close mainwindow
        database.saveAndCloseDatabase();
        mainWindow.close();
    }
    
    //Set oldValue to newValue and update currentSet
    public void setSelectedSet(String string, List<Set> sets) {
        String newValue = string;
        for (Set set : sets) {
            if (newValue.equals(set.getName())) {
                currentSet = set;
            }
        }
    }
    /**
     * Creates Java FX areaChart from set's study-speed data.
     * @return StackedAreaChart
     */
    public StackedAreaChart<Number, Number> areaChart() {
        StackedAreaChart<Number, Number> studyAreaChart =
            new StackedAreaChart<>(xAxis, yAxis);
        
        //Set title
        studyAreaChart.setTitle("Time statistics");
        XYChart.Series<Number, Number> seriesAVGSpeed = new XYChart.Series<>();
        
        //Setup first (red) graph
        seriesAVGSpeed.setName("Overall Average Time (s/Card)");
        
        //Setup second (orange) graph
        XYChart.Series<Number, Number> seriesAVGSpeedThree = new XYChart.Series<>();
        seriesAVGSpeedThree.setName("Averaged Time Based on 3 Latest Viewings (s/Card)");
        
        //Add data-points
        if (currentSet.size() != 0) {
            int j = 0;
            for (Card card : currentSet.getCards()) {
                int i = (40 / currentSet.getCards().size()) * j;
                if (!card.getStats().isEmpty()) {
                    seriesAVGSpeed.getData().add(new XYChart.Data(i, (card.getStats().calculateAVGSpeed() / 1000.0)));
                    seriesAVGSpeedThree.getData().add(new XYChart.Data(i, (card.getStats().calculateAVGSpeedLastThree()) / 1000.0));
                }
                j++;
            }
        }
        
        xAxis.setMinorTickCount(20);        
        Scene scene = new Scene(studyAreaChart, 700, 300);
        studyAreaChart.getData().addAll(seriesAVGSpeed, seriesAVGSpeedThree);
        return studyAreaChart;
    }
    
    private void createAddSetWindow(PieChart pieChart,
                                    ChoiceBox<String> setNames) {
        
        TextInputDialog addSetWindow = new TextInputDialog("Name here");
        addSetWindow.setTitle("Name Input Dialog");
        addSetWindow.setHeaderText("Give new Name");
        
        
        Optional<String> result = addSetWindow.showAndWait();
        result.ifPresent(name -> {
            if (!saveSetAndSetCurrent(name)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Something went wrong. Couldn't add new Set.");

                alert.showAndWait();
            }
            updateChoiceBox();
            setNames.setValue(name);
        });
    }
    
    private boolean saveSetAndSetCurrent(String text) {
        Set newSet = new Set(text);
        if (!database.containsSet(newSet)) {
            currentSet = newSet;
            return database.addNewSet(newSet);
        } else {
            return false;
        }
    }

    private void updateChoiceBox() {
        List<Set> sets = database.getSets();
        setNames = new ChoiceBox<>();
        for (Set set : sets) {
            setNames.getItems().add(set.getName());
        }
        if (currentSet != null) {
            setNames.setValue(currentSet.getName());
        }
    }

}

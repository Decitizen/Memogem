
package memogem.ui;

import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
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
    private BorderPane borderPane; // Borderpane, parent of 
    private CardEngine cEngine; // CardEngine that handles all the card's operations
    private Set currentSet; // Holds the current set under study
    
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
        ChoiceBox<String> setNames = new ChoiceBox<>();
        
        //Fetch set-names:
        List<Set> sets = database.getSets();
        
        for (Set set : sets) {
            setNames.getItems().add(set.getName());
        }
        currentSet = sets.get(0);
        setNames.setValue(currentSet.getName());
        
        
        // Create texts and hboxes
        List<HBox> hboxes = setSetStatsText();
        HBox statisticsBox1 = hboxes.get(0);
        HBox statisticsBox2 = hboxes.get(1);
        HBox statisticsBox3 = hboxes.get(2);
        
        setNames.setValue(currentSet.getName());
        setNames.setMinWidth(100);
        setNames.setMaxWidth(300);
        
        //Create buttons
        Button addSet = new Button("Add Set");
        Button addCard = new Button("Add Card");
        Button studyNow = new Button("Study Set");
        
        //Add actions to buttons
        addCard.setOnAction(e -> {
            EditCardWindow editWindow = new EditCardWindow(database,cEngine, currentSet);
            editWindow.createMWindow();
        });
        
        //Create MenuBar
        MainMenubar mainMenuBar = new MainMenubar(database, cEngine, mainWindow);
        MenuBar menuBar = mainMenuBar.createMenuBar();
        
        //Create piechart
        PieChart pieChart = pieChart();
        
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
        
        GridPane.setColumnSpan(statisticsBox1, REMAINING);
        GridPane.setColumnSpan(statisticsBox2, REMAINING);
        GridPane.setColumnSpan(statisticsBox3, REMAINING);

        GridPane.setConstraints(statisticsBox1, 0, 3);
        GridPane.setConstraints(statisticsBox2, 0, 4);
        GridPane.setConstraints(statisticsBox3, 0, 5);

        GridPane.setMargin(statisticsBox1, new Insets(0, 0, 0, 25));
        GridPane.setMargin(statisticsBox2, new Insets(0, 0, 0, 25));
        GridPane.setMargin(statisticsBox3, new Insets(0, 0, 0, 25));
        
        //Populate gridPane
        gridPane.getChildren().addAll(menuBar, setNames, statisticsBox1, 
                        statisticsBox2, statisticsBox3, chooseSetLabel);
        
        //Create area chart for statistics
        StackedAreaChart<Number, Number> studyChart = areaChart();
        
        //Listen for selection changes
        setNames.getSelectionModel().selectedItemProperty().addListener
        ((v, oldValue, newValue) -> {

            setSelectedSet(newValue, sets);
            StackedAreaChart<Number, Number> aC = areaChart();
            List<HBox> hboxes1 = setSetStatsText();
            
            HBox statisticsBox11 = hboxes1.get(0);
            HBox statisticsBox22 = hboxes1.get(1);
            HBox statisticsBox33 = hboxes1.get(2);
            
            gridPane.getChildren().add(2, statisticsBox11);
            gridPane.getChildren().add(3, statisticsBox22);
            gridPane.getChildren().add(4, statisticsBox33);
            
            GridPane.setColumnSpan(statisticsBox11, REMAINING);
            GridPane.setColumnSpan(statisticsBox22, REMAINING);
            GridPane.setColumnSpan(statisticsBox33, REMAINING);
            
            GridPane.setConstraints(statisticsBox11, 0, 3);
            GridPane.setConstraints(statisticsBox22, 0, 4);
            GridPane.setConstraints(statisticsBox33, 0, 5);
            
            GridPane.setMargin(statisticsBox11, new Insets(0, 0, 0, 25));
            GridPane.setMargin(statisticsBox22, new Insets(0, 0, 0, 25));
            GridPane.setMargin(statisticsBox33, new Insets(0, 0, 0, 25));
            
            borderPane = new BorderPane(pieChart, gridPane, areaChart(), hbox, null);
            mainWindow.setScene(new Scene(borderPane, 980, 760));
            mainWindow.show();
        });
        
        //Study now! action
        studyNow.setOnAction(e -> {
            StudyWindow studyWindow = new StudyWindow(database, mainWindow, cEngine, currentSet, cEngine.getStudymode());
            studyWindow.createSWindow();
        });
        
        //Create parent layout, borderpane
        borderPane = new BorderPane(pieChart, gridPane, studyChart, hbox, null);
        BorderPane.setMargin(hbox, new Insets(15));
        
    }
    /**
     * Creates/Updates statistics
     * @return List of HBoxes
     */
    public List<HBox> setSetStatsText() {
        //Create set statistics
        Label setStatsCardAmounLabel = new Label("Cards:");
        Label setDifficultyLabel = new Label("Difficulty:");
        Label setStatsLastTimeStudiedLabel = new Label("Date Last Studied:");
        
        Text setStatsCardAmountText = new Text("" + currentSet.getCards().size());
        Text setDifficultyText = new Text("" + currentSet.getAVGDifficultyAsString());
        
        setStatsCardAmountText.setFill(Color.LIGHTGREEN);
        setDifficultyText.setFill(Color.LIGHTGREEN);
        setDifficultyText.setFont(Font.font("Verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        setStatsCardAmountText.setFont(Font.font("Verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        Text setStatsLastTimeStudiedText = new Text("Not Studied Yet");
        
        if (currentSet.getLastTimeStudied() != null) {
            setStatsLastTimeStudiedText = new Text(""
                    + currentSet.getLastTimeStudied().getDayOfMonth() + "."
                    + currentSet.getLastTimeStudied().getMonthValue() + "."
                    + currentSet.getLastTimeStudied().getYear());
            setStatsLastTimeStudiedText.setFill(Color.LIGHTGREEN);
            setStatsLastTimeStudiedText.setFont(Font.font("Verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        }
        
        //Create 3 Hboxes for statistics
        List<HBox> hboxes = new LinkedList<>();
        HBox statisticsBox1 = new HBox(setStatsCardAmounLabel, 
                                            setStatsCardAmountText);
        
        
        HBox statisticsBox2 = new HBox(setStatsLastTimeStudiedLabel, 
                                        setStatsLastTimeStudiedText);
        
        
        HBox statisticsBox3 = new HBox(setDifficultyLabel, setDifficultyText);
        
        hboxes.add(statisticsBox1);
        hboxes.add(statisticsBox2);
        hboxes.add(statisticsBox3);
        
        return hboxes;
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
    
    


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memogem.ui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Stage;
import memogem.carddatabase.Database;
import memogem.coreapplication.CardEngine;
import static memogem.ui.MainWindow.closeProgram;

/**
 *
 * @author Willburner
 */
public class MainMenubar {
    private Database database; // Session's database
    private CardEngine cEngine; // CardEngine that handles all the card's operations
    private Stage mainWindow; // Parent graphical stage, MainWindow

    public MainMenubar(Database database, CardEngine cEngine, Stage mainWindow) {
        this.database = database;
        this.cEngine = cEngine;
        this.mainWindow = mainWindow;
    }
    
    /**
     * Creates the mainWindow's menu-bar.
     * @return fully populated MenuBar-object
     */
    public MenuBar createMenuBar() {
        final MenuBar menuBar = new MenuBar();
        // Prepare left-most 'File' drop-down menu
        final Menu fileMenu = new Menu("File");
        final Menu optionsMenu = new Menu("Options");
        final Menu modeMenu = new Menu("Study Mode");
        
        MenuItem mode1 = new MenuItem("By Difficulty");
        MenuItem mode2 = new MenuItem("By Least Studied");
        MenuItem mode3 = new MenuItem("Fresh");
        
        mode1.setOnAction(e -> cEngine.setStudyMode(1));
        mode2.setOnAction(e -> cEngine.setStudyMode(2));
        mode3.setOnAction(e -> cEngine.setStudyMode(3));
        
        modeMenu.getItems().add(mode1);
        modeMenu.getItems().add(mode2);
        modeMenu.getItems().add(mode3);
        MenuItem exitMenu = new MenuItem("Exit");
        exitMenu.setOnAction( e -> closeProgram(mainWindow, database));
        
        fileMenu.getItems().add(new SeparatorMenuItem());
        fileMenu.getItems().add(exitMenu);
        
        menuBar.getMenus().addAll(fileMenu, optionsMenu, modeMenu);
        
        return menuBar;
    }
}

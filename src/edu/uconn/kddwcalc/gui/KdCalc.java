package edu.uconn.kddwcalc.gui;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * <code>Main</code> application class that launches program
 * 
 * @author Alex R
 */
public class KdCalc extends Application {
    
    public static final String VERSION = "2.0";
    
    private static final double LOADING_SCREEN_TOTAL_TIME = 7000; // ms
    
    /**
     * Gets FXML and loads scene and menuStage
     * 
     * @param menuStage the primary menuStage to show
     * 
     * @throws IOException if problem occurs while loading .fxml file 
     */
    @Override
    public void start(Stage menuStage) throws IOException {  
        
        Parent menuRoot = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        
        menuStage.setTitle("KdCalc - Menu");
        
        Scene menu = new Scene(menuRoot);
        
        menu.getStylesheets().add(
            getClass().getResource("MenuStyleSheet.css").toExternalForm());
        
        
        menuStage.initStyle(StageStyle.UNDECORATED);
        menuStage.setScene(menu);
        menuStage.setX(50);
        menuStage.setY(50);
        
        runLoadingScreenAndShowMenu(new Stage(), menuStage);
    }

    @Override
    public void init() throws Exception {
        Parameters p = getParameters();
        if (p.getUnnamed().contains("-version")) {
            System.out.println("Version: " + VERSION);
            Platform.exit();
        }
    }
    
    /**
     * Program <code>main</code> method where execution starts.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Runs the loading screen and then shows the menu after a {@Link PauseTransition} occurs
     * 
     * @param stage the loading scene stage
     * @param menuStage the menu stage
     * 
     * @throws IOException if it cant load the loading screen fxml or css file
     */
    private void runLoadingScreenAndShowMenu(Stage stage, Stage menuStage) throws IOException {

        PauseTransition delay = new PauseTransition(Duration.millis(LOADING_SCREEN_TOTAL_TIME));
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent loadingParent = fxmlLoader.load(getClass().getResource("LoadingScreen.fxml"));
        LoadingScreenController loadingScreenController = fxmlLoader.getController();
        
        
        Scene loadingScene = new Scene(loadingParent);
        
        loadingScene.getStylesheets().add(
            getClass().getResource("LoadingScreenStyleSheet.css").toExternalForm());

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(loadingScene);
        stage.show();
        
        delay.setOnFinished(event -> {
            stage.close();
            menuStage.show();    
            });
        
        delay.play();    
    } 
} // end class KdCalc

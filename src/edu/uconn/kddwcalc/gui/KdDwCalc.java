package edu.uconn.kddwcalc.gui;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Main application class that launches program
 * 
 * @author Alex R
 */
public class KdDwCalc extends Application {
    
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
        
        Scene menu = new Scene(menuRoot);
        
        menu.getStylesheets().add(
            getClass().getResource("MenuStyleSheet.css").toExternalForm());
        
        
        menuStage.initStyle(StageStyle.UNDECORATED);
        menuStage.setScene(menu);
        
        menuStage.setX(50);
        menuStage.setY(50);
        
        runLoadingScreen(new Stage(), menuStage);
    }

    /**
     * Program <code>main</code> method where execution starts.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void runLoadingScreen(Stage stage, Stage menuStage) throws IOException {

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

    
    
    
    
    
    
} // end class KdDwCalc

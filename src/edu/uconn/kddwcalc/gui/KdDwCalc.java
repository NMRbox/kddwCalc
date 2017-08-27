package edu.uconn.kddwcalc.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class that launches program
 * 
 * @author Alex R
 */
public class KdDwCalc extends Application {
    
    /**
     * Gets FXML and loads scene and stage
     * 
     * @param stage the primary stage to show
     * 
     * @throws IOException if problem occurs while loading .fxml file 
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SlowExchangeGUI.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Program <code>main</code> method where execution starts.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
} // end class KdDwCalc

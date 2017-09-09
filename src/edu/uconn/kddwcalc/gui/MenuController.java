package edu.uconn.kddwcalc.gui;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *  Controller class for the application menu
 * 
 * @author Alex Ri
 */
public class MenuController {
    private Object stage;
    
    public void quitButtonPressed(ActionEvent e) {
        Platform.exit();
        System.exit(0);
    }
    
    public void slowExchangeGUIPressed(ActionEvent e) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("SlowExchangeGUI.fxml"));
        
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add(
            getClass().getResource("SlowExchangeGUIStyleSheet.css").toExternalForm());
        
        Stage stage = new Stage();
        
        stage.setTitle("Slow Exchange NMR Titration Data Input");
        
        stage.setScene(scene);
        stage.show();
    }
}

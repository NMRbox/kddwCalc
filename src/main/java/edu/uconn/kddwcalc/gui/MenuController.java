package edu.uconn.kddwcalc.gui;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for the application menu
 * 
 * @author Alex Ri
 */
public class MenuController {
    
    /**
     * Exits the program
     * 
     * @param e the {@link ActionEvent} that occurred
     */
    public void quitButtonPressed(ActionEvent e) {
        Platform.exit();
        System.exit(0);
    }
    
    /**
     * Opens a fast exchange data input gui (FastExchangeGUI.fxml)
     * 
     * @param e he {@link ActionEvent} that occurred 
     * 
     * @see FastExchangeGUIController
     * 
     * @throws IOException if cant get the FXML file
     */
    public void fastExchangeGUIPressed(ActionEvent e) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("FastExchangeGUI.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
            getClass().getResource("FastExchangeGUIStyleSheet.css").toExternalForm());
        
        Stage fastExchangeStage = new Stage();
        fastExchangeStage.setResizable(false);
        fastExchangeStage.setTitle("KdCalc - Fast Exchange NMR Titration Data Input");
        fastExchangeStage.setScene(scene);
        fastExchangeStage.show();
    }
}

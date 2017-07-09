/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uconn.kdCalc.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 *
 * @author home
 */
public class InputGUIController implements Initializable 
{
    @FXML
    public ComboBox<String> typeOfTitration; 
    
    @FXML
    public ComboBox<String> resonanceOrderSelector;
        
    
    ObservableList<String> listOfSpectra = 
        FXCollections.observableArrayList("1H-15N HSQC", "1H-13C methyl HMQC");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        typeOfTitration.setItems(listOfSpectra);
        resonanceOrderSelector.setItems(FXCollections.observableArrayList("Please select type of spectrum first"));
        
        
    }    
    
    
    public void typeOfTitrationSelected(ActionEvent event)
    {
        if(typeOfTitration.getValue().equals("1H-15N HSQC"))
            resonanceOrderSelector.setItems(FXCollections.observableArrayList("Nitrogen Proton", "Proton Nitrogen"));
        
        if(typeOfTitration.getValue().equals("1H-13C methyl HMQC"))
            resonanceOrderSelector.setItems(FXCollections.observableArrayList("Carbon Proton", "Proton Carbon"));
    }
}

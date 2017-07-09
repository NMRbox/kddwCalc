/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uconn.kdCalc.gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 *
 * @author home
 */
public class InputGUIController implements Initializable 
{
    @FXML
    private ComboBox<String> typeOfTitration; 
    
    @FXML
    private ComboBox<String> resonanceOrderSelector;
    
    @FXML
    private Button chooser1;
    
    @FXML
    private Button chooser2;
    
    @FXML
    private Button chooser3;
    
    @FXML
    private Button chooser4;
    
    @FXML
    private Button chooser5;
    
    @FXML
    private Button chooser6;
    
    @FXML
    private Button chooser7;
    
    @FXML
    private Button chooser8;
    
    @FXML
    private Button chooser9;
    
    @FXML
    private Button chooser10;
    
    @FXML
    private Button chooser11;
    
    @FXML
    private Button chooser12;
    
    @FXML
    private Button chooser13;
    
    @FXML
    private Button chooser14;
    
    
    private Button chooser15;
    
    private File selection1 = null;
    private File selection2 = null;
    private File selection3 = null;
    private File selection4 = null;
    private File selection5 = null;
    private File selection6 = null;
    private File selection7 = null;
    private File selection8 = null;
    private File selection9 = null;
    private File selection10 = null;
    private File selection11 = null;
    private File selection12 = null;
    private File selection13 = null;
    private File selection14 = null;
    private File selection15 = null; 
    
    @FXML 
    private TextField fileName1;
    
    @FXML 
    private TextField fileName2;
    
    @FXML 
    private TextField fileName3;
    
    @FXML 
    private TextField fileName4;
    
    @FXML 
    private TextField fileName5;
    
    @FXML 
    private TextField fileName6;
    
    @FXML 
    private TextField fileName7;
    
    @FXML 
    private TextField fileName8;
    
    @FXML 
    private TextField fileName9;
    
    @FXML 
    private TextField fileName10;
    
    @FXML 
    private TextField fileName11;
    
    @FXML 
    private TextField fileName12;
    
    @FXML 
    private TextField fileName13;
    
    @FXML 
    private TextField fileName14;
    
    @FXML 
    private TextField fileName15;
    
    
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
    
    public void Button1pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection1 = chooser.showOpenDialog(null);
        
        if(selection1 != null)
            fileName1.setText(selection1.getName());
    }
    
    public void Button2pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection2 = chooser.showOpenDialog(null);
        
        if(selection2 != null)
            fileName2.setText(selection2.getName());
    }
    
    public void Button3pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection3 = chooser.showOpenDialog(null);
        
        if(selection3 != null)
            fileName3.setText(selection3.getName());
    }
    
    public void Button4pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection4 = chooser.showOpenDialog(null);
        
        if(selection4 != null)
            fileName4.setText(selection4.getName());
    }
    
    public void Button5pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection5 = chooser.showOpenDialog(null);
        
        if(selection5 != null)
            fileName5.setText(selection5.getName());
    }
    
    public void Button6pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection6 = chooser.showOpenDialog(null);
        
        if(selection6 != null)
            fileName6.setText(selection6.getName());
    }
    
    public void Button7pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection7 = chooser.showOpenDialog(null);
        
        if(selection7 != null)
            fileName7.setText(selection7.getName());
    }
    
    public void Button8pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection8 = chooser.showOpenDialog(null);
        
        if(selection8 != null)
            fileName8.setText(selection8.getName());
    }
    
    public void Button9pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection9 = chooser.showOpenDialog(null);
        
        if(selection9 != null)
            fileName9.setText(selection9.getName());
    }
    
    public void Button10pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection10 = chooser.showOpenDialog(null);
        
        if(selection10 != null)
            fileName10.setText(selection10.getName());
    }
    
    public void Button11pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection11 = chooser.showOpenDialog(null);
        
        if(selection11 != null)
            fileName11.setText(selection11.getName());
    }
    
    public void Button12pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection12 = chooser.showOpenDialog(null);
        
        if(selection12 != null)
            fileName12.setText(selection12.getName());
    }
    
    public void Button13pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection13 = chooser.showOpenDialog(null);
        
        if(selection13 != null)
            fileName13.setText(selection13.getName());
    }
    
    public void Button14pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection14 = chooser.showOpenDialog(null);
        
        if(selection14 != null)
            fileName14.setText(selection14.getName());
    }
    
    public void Button15pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        selection15 = chooser.showOpenDialog(null);
        
        if(selection15 != null)
            fileName15.setText(selection15.getName());
    }
    
    
    
    
}

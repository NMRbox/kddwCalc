// major editing done on 170609 and 170610 by Alex Ri

// i need a better way to process the button and lines


package edu.uconn.kdCalc.gui;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
    @FXML private ComboBox<String> typeOfTitration; 
    @FXML private ComboBox<String> resonanceOrderSelector;
    @FXML TextField multiplierTextField;
    
    @FXML private Button chooser1;
    @FXML private Button chooser2;
    @FXML private Button chooser3;
    @FXML private Button chooser4;
    @FXML private Button chooser5;
    @FXML private Button chooser6;
    @FXML private Button chooser7;
    @FXML private Button chooser8;
    @FXML private Button chooser9;
    @FXML private Button chooser10;
    @FXML private Button chooser11;
    @FXML private Button chooser12;
    @FXML private Button chooser13;
    @FXML private Button chooser14; 
    @FXML private Button chooser15;
    
    @FXML TextField receptorConc1;
    @FXML TextField receptorConc2;
    @FXML TextField receptorConc3;
    @FXML TextField receptorConc4;
    @FXML TextField receptorConc5;
    @FXML TextField receptorConc6;
    @FXML TextField receptorConc7;
    @FXML TextField receptorConc8;
    @FXML TextField receptorConc9;
    @FXML TextField receptorConc10;
    @FXML TextField receptorConc11;
    @FXML TextField receptorConc12;
    @FXML TextField receptorConc13;
    @FXML TextField receptorConc14;
    @FXML TextField receptorConc15;
    
    @FXML TextField ligandConc1;
    @FXML TextField ligandConc2;
    @FXML TextField ligandConc3;
    @FXML TextField ligandConc4;
    @FXML TextField ligandConc5;
    @FXML TextField ligandConc6;
    @FXML TextField ligandConc7;
    @FXML TextField ligandConc8;
    @FXML TextField ligandConc9;
    @FXML TextField ligandConc10;
    @FXML TextField ligandConc11;
    @FXML TextField ligandConc12;
    @FXML TextField ligandConc13;
    @FXML TextField ligandConc14;
    @FXML TextField ligandConc15;
    
    @FXML private TextField fileName1;    
    @FXML private TextField fileName2;   
    @FXML private TextField fileName3;   
    @FXML private TextField fileName4;    
    @FXML private TextField fileName5;   
    @FXML private TextField fileName6;
    @FXML private TextField fileName7;
    @FXML private TextField fileName8;
    @FXML private TextField fileName9;
    @FXML private TextField fileName10;
    @FXML private TextField fileName11;
    @FXML private TextField fileName12; 
    @FXML private TextField fileName13;  
    @FXML private TextField fileName14;  
    @FXML private TextField fileName15;
    
    @FXML private Label errorLabel;
    
    private final List<Button> chooserButtonList = new ArrayList<>(15);
    private final List<File> fileList = new ArrayList<>(15);
    private final List<Path> pathList = new ArrayList<>(15);
    private final List<TextField> ligandConcTextFieldList = new ArrayList<>(15);
    private final List<TextField> receptorConcTextFieldList = new ArrayList<>(15);
    
    private final List<Double> ligandConcList = new ArrayList<>();
    private final List<Double> receptorConcList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        typeOfTitration.setItems(FXCollections.observableArrayList("1H-15N HSQC", "1H-13C methyl HMQC"));
        resonanceOrderSelector.setItems(FXCollections.observableArrayList("Please select type of spectrum first"));
        
        addChoosersToList(); // create the List<Button>, these are pressed to bring up file chooser for data
        addLigandConcTextFieldsToList();  // create the List<TextField>
        addReceptorConcTextFieldsToList();  // also creates List<TextField>
        //addFilesToList();  // create List<File>
        
    } 
    
    public void executeButtonPressed(ActionEvent event)
    {   
        readUserDataAndRunValidation();
        
        
        
        
        
        
        
        
    }
    
    private void readUserDataAndRunValidation()
    {
        removeNullFilesAndMakePaths();
        
        makeSureConcsCanBeParsed();
        
        makeSureEqualNumFilesAndConcs();
    }
    
    private void removeNullFilesAndMakePaths()
    {
        pathList.addAll(fileList.stream()
                                .filter(file -> file != null)
                                .map(File::toPath)
                                .collect(Collectors.toList()));
    }
    
    private void convertTextFieldsToConcsAndTruncate()
    {
        ligandConcList.addAll(ligandConcTextFieldList.stream()
                                                 .map(TextField::getText) // get the string from each text field
                                                 .filter(text -> !(text.equals(""))) // deletes blank text fields
                                                 .map(Double::valueOf)      // turn string to a double
                                                 .collect(Collectors.toList()));
        receptorConcList.addAll(receptorConcTextFieldList.stream()
                                                 .map(TextField::getText) // get the string from each text field
                                                 .filter(text -> !(text.equals(""))) // deletes blank text fields
                                                 .map(Double::valueOf)      // turn string to a double
                                                 .collect(Collectors.toList()));  
    }
    
    private void makeSureConcsCanBeParsed()
    {
        // if text fields have something that cant be parsed, this will catch that
        //    and give an error message in a label that printed to user
        try
        {
            convertTextFieldsToConcsAndTruncate();
        }
        catch(NumberFormatException e)
        {
            System.err.println(e);
            errorLabel.setText("Conc cant be parsed");
        }
    }
    
    private void makeSureEqualNumFilesAndConcs()
    {
        try
        {
            if (pathList.size() != ligandConcList.size()
              ||pathList.size() != receptorConcList.size()
              ||ligandConcList.size() != receptorConcList.size())
            {
                throw new IllegalArgumentException();
            }
        }
        catch(IllegalArgumentException e)
        {
            System.err.println("Dont have same number of files and concentrations");
            errorLabel.setText("Dif number of files and concs");
            
            // reset some of the objects
            ligandConcList.clear();
            receptorConcList.clear();
            pathList.clear();
            
        }
    }
   
    
    // this code is coupled to specific titration information. this must be updated if new types of tirations
    //     are added
    public void typeOfTitrationSelected(ActionEvent event)
    {
        if(typeOfTitration.getValue().equals("1H-15N HSQC"))
        {
            resonanceOrderSelector.setItems(FXCollections.observableArrayList("Nitrogen Proton", "Proton Nitrogen"));
            multiplierTextField.setText("0.1");
        }
        
        if(typeOfTitration.getValue().equals("1H-13C methyl HMQC"))
        {
            resonanceOrderSelector.setItems(FXCollections.observableArrayList("Carbon Proton", "Proton Carbon"));
            multiplierTextField.setText("0.25");
        }
    }
    
    private void addChoosersToList()
    {
        chooserButtonList.add(chooser1);
        chooserButtonList.add(chooser2);
        chooserButtonList.add(chooser3);
        chooserButtonList.add(chooser4);
        chooserButtonList.add(chooser5);
        chooserButtonList.add(chooser6);
        chooserButtonList.add(chooser7);
        chooserButtonList.add(chooser8);
        chooserButtonList.add(chooser9);
        chooserButtonList.add(chooser10);
        chooserButtonList.add(chooser11);
        chooserButtonList.add(chooser12);
        chooserButtonList.add(chooser13);
        chooserButtonList.add(chooser14);
        chooserButtonList.add(chooser15);  
    }
    
    private void addLigandConcTextFieldsToList()
    {
        ligandConcTextFieldList.add(ligandConc1);
        ligandConcTextFieldList.add(ligandConc2);
        ligandConcTextFieldList.add(ligandConc3);
        ligandConcTextFieldList.add(ligandConc4);
        ligandConcTextFieldList.add(ligandConc5);
        ligandConcTextFieldList.add(ligandConc6);
        ligandConcTextFieldList.add(ligandConc7);
        ligandConcTextFieldList.add(ligandConc8);
        ligandConcTextFieldList.add(ligandConc9);
        ligandConcTextFieldList.add(ligandConc10);
        ligandConcTextFieldList.add(ligandConc11);
        ligandConcTextFieldList.add(ligandConc12);
        ligandConcTextFieldList.add(ligandConc13);
        ligandConcTextFieldList.add(ligandConc14);
        ligandConcTextFieldList.add(ligandConc15);
    }
    
    private void addReceptorConcTextFieldsToList()
    {
        receptorConcTextFieldList.add(receptorConc1);
        receptorConcTextFieldList.add(receptorConc2);
        receptorConcTextFieldList.add(receptorConc3);
        receptorConcTextFieldList.add(receptorConc4);
        receptorConcTextFieldList.add(receptorConc5);
        receptorConcTextFieldList.add(receptorConc6);
        receptorConcTextFieldList.add(receptorConc7);
        receptorConcTextFieldList.add(receptorConc8);
        receptorConcTextFieldList.add(receptorConc9);
        receptorConcTextFieldList.add(receptorConc10);
        receptorConcTextFieldList.add(receptorConc11);
        receptorConcTextFieldList.add(receptorConc12);
        receptorConcTextFieldList.add(receptorConc13);
        receptorConcTextFieldList.add(receptorConc14);
        receptorConcTextFieldList.add(receptorConc15);   
    }
    
    public void Button1pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(0, chooser.showOpenDialog(null));
        
        if(fileList.get(0) != null)
        {
            fileName1.setText(fileList.get(0).getName());
            chooser2.setDisable(false);
            receptorConc1.setEditable(true);
        }
    }
    
    public void Button2pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(1, chooser.showOpenDialog(null));
        
        if(fileList.get(1) != null)
        {
            fileName2.setText(fileList.get(1).getName());
            chooser3.setDisable(false);
            receptorConc2.setEditable(true);
            ligandConc2.setEditable(true);
        }
    }
    
    public void Button3pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(2, chooser.showOpenDialog(null));
        
        if(fileList.get(2) != null)
        {
            fileName3.setText(fileList.get(2).getName());
            chooser4.setDisable(false);
            receptorConc3.setEditable(true);
            ligandConc3.setEditable(true);
        }
    }
    
    public void Button4pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(3, chooser.showOpenDialog(null));
        
        if(fileList.get(3) != null)
        {
            fileName4.setText(fileList.get(3).getName());
            chooser5.setDisable(false);
            receptorConc4.setEditable(true);
            ligandConc4.setEditable(true);
        }
    }
    
    public void Button5pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(4, chooser.showOpenDialog(null));
        
        if(fileList.get(4) != null)
        {
            fileName5.setText(fileList.get(4).getName());
            chooser6.setDisable(false);
            receptorConc5.setEditable(true);
            ligandConc5.setEditable(true);
        }
    }
    
    public void Button6pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(5, chooser.showOpenDialog(null));
        
        if(fileList.get(5) != null)
        {
            fileName6.setText(fileList.get(5).getName());
            chooser7.setDisable(false);
            receptorConc6.setEditable(true);
            ligandConc6.setEditable(true);
        }
    }
    
    public void Button7pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(6, chooser.showOpenDialog(null));
        
        if(fileList.get(6) != null)
        {
            fileName7.setText(fileList.get(6).getName());
            chooser8.setDisable(false);
            receptorConc7.setEditable(true);
            ligandConc7.setEditable(true);
        }
    }
    
    public void Button8pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(7, chooser.showOpenDialog(null));
        
        if(fileList.get(7) != null)
        {
            fileName8.setText(fileList.get(7).getName());
            chooser9.setDisable(false);
            receptorConc8.setEditable(true);
            ligandConc8.setEditable(true);
        }
    }
    
    public void Button9pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(8, chooser.showOpenDialog(null));
        
        if(fileList.get(8) != null)
        {
            fileName9.setText(fileList.get(8).getName());
            chooser10.setDisable(false);
            receptorConc9.setEditable(true);
            ligandConc9.setEditable(true);
        }
    }
    
    public void Button10pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(9, chooser.showOpenDialog(null));
        
        if(fileList.get(9) != null)
        {
            fileName10.setText(fileList.get(9).getName());
            chooser11.setDisable(false);
            receptorConc10.setEditable(true);
            ligandConc10.setEditable(true);
        }
    }
    
    public void Button11pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(10, chooser.showOpenDialog(null));
        
        if(fileList.get(10) != null)
        {
            fileName11.setText(fileList.get(10).getName());
            chooser12.setDisable(false);
            receptorConc11.setEditable(true);
            ligandConc11.setEditable(true);
        }
    }
    
    public void Button12pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(11, chooser.showOpenDialog(null));
        
        if(fileList.get(11) != null)
        {
            fileName12.setText(fileList.get(11).getName());
            chooser13.setDisable(false);
            receptorConc12.setEditable(true);
            ligandConc12.setEditable(true);
        }
    }
    
    public void Button13pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(12, chooser.showOpenDialog(null));
        
        if(fileList.get(12) != null)
        {
            fileName13.setText(fileList.get(12).getName());
            chooser14.setDisable(false);
            receptorConc13.setEditable(true);
            ligandConc13.setEditable(true);
        }
    }
    
    public void Button14pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(13, chooser.showOpenDialog(null));
        
        if(fileList.get(13) != null)
        {
            fileName14.setText(fileList.get(13).getName());
            chooser15.setDisable(false);
            receptorConc14.setEditable(true);
            ligandConc14.setEditable(true);
        }
    }
    
    public void Button15pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.add(14, chooser.showOpenDialog(null));
        
        if(fileList.get(14) != null)
        {
            fileName15.setText(fileList.get(14).getName());
            receptorConc15.setEditable(true);
            ligandConc15.setEditable(true);
        }
    }
    
    
} // end class InputGUIController

// major editing done on 170609 and 170610 by Alex Ri

// i need a better way to process the button and lines


package edu.uconn.kdCalc.gui;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private ComboBox<String> typeOfTitration; 
    @FXML private ComboBox<String> resonanceOrderSelector;
    @FXML TextField multiplier;
    
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
    
    private final List<Button> chooserButtonList = new ArrayList<>();
    private final List<File> fileList = new ArrayList<>();
    private final List<Path> pathList = new ArrayList<>();
    private final List<TextField> ligandConcTextFieldList = new ArrayList<>();
    private final List<TextField> receptorConcTextFieldList = new ArrayList<>();
    
    private final List<Double> ligandConcList = new ArrayList<>();
    private final List<Double> receptorConcList = new ArrayList<>();

    ObservableList<String> listOfSpectra = 
        FXCollections.observableArrayList("1H-15N HSQC", "1H-13C methyl HMQC");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        typeOfTitration.setItems(listOfSpectra);
        resonanceOrderSelector.setItems(FXCollections.observableArrayList("Please select type of spectrum first"));
        
        addChoosersToList();
        addLigandConcTextFieldsToList();  // create the List<TextField>
        addReceptorConcTextFieldsToList();  // also creates List<TextField>
        addFilesToList();
    }    
    
    public void typeOfTitrationSelected(ActionEvent event)
    {
        if(typeOfTitration.getValue().equals("1H-15N HSQC"))
            resonanceOrderSelector.setItems(FXCollections.observableArrayList("Nitrogen Proton", "Proton Nitrogen"));
        
        if(typeOfTitration.getValue().equals("1H-13C methyl HMQC"))
            resonanceOrderSelector.setItems(FXCollections.observableArrayList("Carbon Proton", "Proton Carbon"));
    }
    
    public void addChoosersToList()
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
    
    public void addLigandConcTextFieldsToList()
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
    
    public void addReceptorConcTextFieldsToList()
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
    
    public void addFilesToList()
    {
        fileList.add(selection1);
        fileList.add(selection2);
        fileList.add(selection3);
        fileList.add(selection4);
        fileList.add(selection5);
        fileList.add(selection6);
        fileList.add(selection7);
        fileList.add(selection8);
        fileList.add(selection9);
        fileList.add(selection10);
        fileList.add(selection11);
        fileList.add(selection12);
        fileList.add(selection13);
        fileList.add(selection14);
        fileList.add(selection15); 
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
    
    // 
    public void executeButtonPressed(ActionEvent event)
    {
        if (selection1 != null)
        {
            fileList.add(selection1.toPath());
            ligandConcList.add(Double.valueOf(receptorConc1.getText()));
            receptorConcList.add(Double.valueOf(ligandConc1.getText()));
        }
        
        if (selection2 != null)
        {
            fileList.add(selection2.toPath());
            ligandConcList.add(Double.valueOf(receptorConc2.getText()));
            receptorConcList.add(Double.valueOf(ligandConc2.getText()));
        }
        
        if (selection3 != null)
        {
            fileList.add(selection3.toPath());
            ligandConcList.add(Double.valueOf(receptorConc3.getText()));
            receptorConcList.add(Double.valueOf(ligandConc3.getText()));
        }
        
        if (selection4 != null)
        {
            fileList.add(selection4.toPath());
            ligandConcList.add(Double.valueOf(receptorConc4.getText()));
            receptorConcList.add(Double.valueOf(ligandConc4.getText()));
        }
        
        if (selection5 != null)
        {
            fileList.add(selection5.toPath());
            ligandConcList.add(Double.valueOf(receptorConc5.getText()));
            receptorConcList.add(Double.valueOf(ligandConc5.getText()));
        }
        
        if (selection6 != null)
        {
            fileList.add(selection6.toPath());
            ligandConcList.add(Double.valueOf(receptorConc6.getText()));
            receptorConcList.add(Double.valueOf(ligandConc6.getText()));
        }
        
        if (selection7 != null)
        {
            fileList.add(selection7.toPath());
            ligandConcList.add(Double.valueOf(receptorConc7.getText()));
            receptorConcList.add(Double.valueOf(ligandConc7.getText()));
        }
        
        if (selection8 != null)
        {
            fileList.add(selection8.toPath());
            ligandConcList.add(Double.valueOf(receptorConc8.getText()));
            receptorConcList.add(Double.valueOf(ligandConc8.getText()));
        }
        
        if (selection9 != null)
        {
            fileList.add(selection9.toPath());
            ligandConcList.add(Double.valueOf(receptorConc9.getText()));
            receptorConcList.add(Double.valueOf(ligandConc9.getText()));
        }
        
        if (selection10 != null)
        {
            fileList.add(selection10.toPath());
            ligandConcList.add(Double.valueOf(receptorConc10.getText()));
            receptorConcList.add(Double.valueOf(ligandConc10.getText()));
        }
        
        if (selection11 != null)
        {
            fileList.add(selection11.toPath());
            ligandConcList.add(Double.valueOf(receptorConc11.getText()));
            receptorConcList.add(Double.valueOf(ligandConc11.getText()));
        }
        
        if (selection12 != null)
        {
            fileList.add(selection12.toPath());
            ligandConcList.add(Double.valueOf(receptorConc12.getText()));
            receptorConcList.add(Double.valueOf(ligandConc12.getText()));
        }
        
        if (selection13 != null)
        {
            fileList.add(selection13.toPath());
            ligandConcList.add(Double.valueOf(receptorConc13.getText()));
            receptorConcList.add(Double.valueOf(ligandConc13.getText()));
        }
        
        if (selection14 != null)
        {
            fileList.add(selection14.toPath());
            ligandConcList.add(Double.valueOf(receptorConc14.getText()));
            receptorConcList.add(Double.valueOf(ligandConc14.getText()));
        }
        
        if (selection15 != null)
        {
            fileList.add(selection15.toPath());
            ligandConcList.add(Double.valueOf(receptorConc15.getText()));
            receptorConcList.add(Double.valueOf(ligandConc15.getText()));
        }
        
        
    }
    
    
    
    
}

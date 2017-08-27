package edu.uconn.kddwcalc.gui;

import edu.uconn.kddwcalc.data.AbsFactory;
import edu.uconn.kddwcalc.data.ArraysInvalidException;
import edu.uconn.kddwcalc.data.DataArrayValidator;
import edu.uconn.kddwcalc.data.FactoryMaker;
import edu.uconn.kddwcalc.data.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.RawData;
import edu.uconn.kddwcalc.data.Results;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.data.TypesOfTitrations;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import org.controlsfx.dialog.*;

/**
 * Controller class for input of slow exchange NMR titration data. For user data input.
 *  
 * @author Alex R.
 * 
 * @since 1.8
 */
public class SlowExchangeGUIController implements Initializable {   
    
// <editor-fold>
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
    
    @FXML TextField multiplierTextField;
    
    @FXML ToggleGroup typeOfTitrationToggleGroup;
    @FXML RadioButton amideHSQCradioButton;
    @FXML RadioButton methylHMQCradioButton;
    
    @FXML ToggleGroup nucleiToggleGroup;
    @FXML RadioButton orderNucleiFirstRadioButton;
    @FXML RadioButton orderNucleiSecondRadioButton;
    
    @FXML Button dataOutputButton;
    @FXML TextField dataOutputTextField;
    
    @FXML Button resultsOutputButton;
    @FXML TextField resultsOutputTextField;
    
    @FXML Button loadButton;
    @FXML Button saveButton;
    
    // </editor-fold>
    
    // set this way so that each the List<File> has 15 elements of <code>null</code>. The
    // program will add new elements and later remove elements if null. Best way I could
    // quickly think of. would be better if this wasnt so global.
    private final List<File> fileList = new ArrayList<>(Arrays.asList(new File[15]));
    private File dataOutputFile = null;
    private File resultsOutputFile = null;

    /**
     * Initializes the GUI.  enums are set as <code>userData</code> for the {@link RadioButton}
     * objects.  
     * 
     * @param url a url
     * @param rb resource bundle
     * 
     * @see TypesOfTitrations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        
        amideHSQCradioButton.setUserData(TypesOfTitrations.AMIDEHSQC);
        methylHMQCradioButton.setUserData(TypesOfTitrations.METHYLHMQC);
        
        orderNucleiFirstRadioButton.setUserData(false);
        orderNucleiSecondRadioButton.setUserData(true);
    } 
    
    /**
     * Executes when the analyze button is pressed. In short, this method contains the meat of the program.
     * It creates the {@link AbsFactory} subclass, prepares the data and then sorts the peak lists. This forms a 
     * {@link TitrationSeries} which is passed as an argument to {@link LeastSquaresFitter#fit} to return a 
     * {@link Results} object. The {@link Results} can be printed to disk.
     * 
     * Note: all exceptions should climb back to the catch block in <code>analyzeButtonPressed</code>
     * and show its message in the dialog box. If an exception does occur, the user can close the box and
     * should be able to edit any of the information in the GUI.
     * 
     * @param event generated by pressing the Analyze button in the GUI
     * 
     * @see AbsFactory
     * @see TitrationSeries
     * @see Results
     * @see RawData
     */
    @FXML
    private void analyzeButtonPressed(ActionEvent event) {   
        try {
            
            // <editor-fold desc="Puts GUI objects into Lists">
            List<TextField> ligandConcTextFieldList = makeListOfObjects(ligandConc1, ligandConc2, ligandConc3,
                                                                        ligandConc4, ligandConc5, ligandConc6,
                                                                        ligandConc7, ligandConc8, ligandConc9,
                                                                        ligandConc10, ligandConc11, ligandConc12,
                                                                        ligandConc13, ligandConc14, ligandConc15); 
            
            List<TextField> receptorConcTextFieldList = makeListOfObjects(receptorConc1, receptorConc2, receptorConc3,
                                                                          receptorConc4, receptorConc5, receptorConc6,
                                                                          receptorConc7, receptorConc8, receptorConc9,
                                                                          receptorConc10, receptorConc11, receptorConc12,
                                                                          receptorConc13, receptorConc14, receptorConc15);
            // </editor-fold>
            
            AbsFactory factory = 
                FactoryMaker.createFactory((TypesOfTitrations)typeOfTitrationToggleGroup.getSelectedToggle()
                                                                                        .getUserData());
            RawData rawDataInstance = 
                prepAndMakeRawDataObject(ligandConcTextFieldList, receptorConcTextFieldList);
            
            TitrationSeries series = factory.analyzeDataFiles(rawDataInstance);
            
            series.printTitrationSeries(dataOutputFile);
        
            Results results = LeastSquaresFitter.fit(series);
            results.writeResultsToDisk(resultsOutputFile);
            
            displayResultsWrittenPopUp();
        }
        // note: NumberFormatException will be caught by its superclass IllegalArgumentException
        //       FileNotFoundException hanled by IOException
        catch(IllegalArgumentException | NullPointerException | IOException | SecurityException |
            FormatterClosedException | NoSuchElementException | ArraysInvalidException e) { 
            
            ExceptionDialog dialog = new ExceptionDialog(e);
            dialog.showAndWait();   
        }
    } // end method executeButtonPressed
    
    
    /**
     * Prepares the data from the user for {@link AbsFactory} by performing some validation of the input
     * and creating the {@link RawData} instance. {@link RawData} is passed to {@link AbsFactory}.  
     * 
     * @param ligandConcTextField contains a ligand concentration from the user
     * @param receptorConcTextField contains a receptor concentration from the user
     * 
     * @return preliminary unsorted peak lists and ligand concentrations
     * 
     * @throws IOException 
     */
    private RawData prepAndMakeRawDataObject(List<TextField> ligandConcTextField,
                                             List<TextField> receptorConcTextField) 
                                             throws IOException {

        List<Path> pathList = removeNullFilesAndMakePaths();
        List<Double> ligandConcList = getListDoubleFromListTextField(ligandConcTextField);
        List<Double> receptorConcList = getListDoubleFromListTextField(receptorConcTextField);
        
        // make sure they all have the same length
        if(!DataArrayValidator.isListLengthsAllEqual(pathList, ligandConcList, receptorConcList))
            throw new IllegalArgumentException("Lists have different length in prepAndMakeRawDataObject");
        
        double multiplier = Double.valueOf(multiplierTextField.getText());
        
        boolean resonanceReversal = (boolean) nucleiToggleGroup.getSelectedToggle().getUserData();
       
        final RawData rawDataInstance = RawData.createRawData(pathList, ligandConcList, 
            receptorConcList, multiplier, resonanceReversal);
        
        if (rawDataInstance == null)
            throw new NullPointerException(
                "rawDataInstance was null before return in method prepAndMakeRawDataObject");
        
        return rawDataInstance;
    }
    
    /**
     * Takes the <code>List{@literal <}File{@literal >}</code> and turns it into a
     * <code>List{@literal <}Path{@literal >}</code> . Also removes the null references from the end of the end.
     * 
     * @return {@link List} objects with locations of NMR chemical shift peak list.
     */
    private List<Path> removeNullFilesAndMakePaths() {
        
        return new ArrayList<>(fileList.stream()
                                       .filter(file -> file != null)
                                       .map(File::toPath)
                                       .collect(Collectors.toList()));
    }
    
    /**
     * Makes <code>List{@literal <}TextField{@literal >}</code> a <code>List{@literal <}Double@literal >}</code>
     * 
     * @param textFieldList
     * 
     * @throws NumberFormatException if the {@link TextField} can't be parsed.
     * 
     * @return the protein concentrations in a <code>List{@literal <}Double@literal >}</code>
     */
    private List<Double> getListDoubleFromListTextField(List<TextField> textFieldList) {
        
        return new ArrayList<>(textFieldList.stream()
                                      .map(TextField::getText) // get the string from each text field
                                      .filter(text -> !(text.equals(""))) // deletes blank text fields
                                      .map(Double::valueOf)      // turn string to a double
                                      .collect(Collectors.toList()));
    }

    /**
     * Makes a {@link List} from a variable number of arguments array. 
     * 
     * @param <T> object type that will go into the {@link List}
     * 
     * @param object individual instance to add to a {@link List}
     * 
     * @return array containing the elements that were formerly in a {@link List}
     */ 
    private <T> List<T> makeListOfObjects(T... object) {   
        return new ArrayList<>(Arrays.asList(object));
    }
    
    /**
     * Allows the user to choose a file to write the final results
     * 
     * @param event the {@link ActionEvent} that occurred
     */
    @FXML
    private void resultsOutputButtonPressed(ActionEvent event) {
        
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(null);
        
        if (file != null) {
            resultsOutputTextField.setText(file.getName());
            resultsOutputFile = file;
        }
    }
    
    /**
     * Allows the user to choose a file to write the sorted peak lists. Inspection of this would
     * make an error obvious.
     * 
     * @param event the {@link ActionEvent} that occurred
     */
    @FXML
    private void dataOutputButtonPressed(ActionEvent event) {
        
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(null);
        
        if (file != null) {
            dataOutputTextField.setText(file.getName());
            dataOutputFile = file;
        }
    }

    /**
     * If the program reaches this method, then a {@link Results} object was written to disk. This informs
     * the user that this has happened.
     */
    private void displayResultsWrittenPopUp() {
      
        Alert alert = 
           new Alert(Alert.AlertType.CONFIRMATION, "Results were written to disk (a good sign!)");
        
        alert.showAndWait();
    }
    
    /**
     * Responds to user selections a methyl HMQC titration type. 
     * 
     * @param event user has chosen a 1H-13C methyl HMQC titration type
     */
    @FXML
    private void methylHMQCSelected(ActionEvent event) {
       
        orderNucleiFirstRadioButton.setText("Carbon Proton");
        orderNucleiSecondRadioButton.setText("Proton Carbon");
        multiplierTextField.setText("0.25");
    }
    
    /**
     * Responds to user selections a methyl HMQC titration type. 
     * 
     * @param event user has chosen a 1H-13C methyl HMQC titration type
     */
    @FXML
    private void amideHSQCSelected(ActionEvent event) { 
       
        orderNucleiFirstRadioButton.setText("Nitrogen Proton");
        orderNucleiSecondRadioButton.setText("Proton Nitrogen");
        multiplierTextField.setText("0.1");
    }
    
    
    @FXML
    private void loadButtonPressed(ActionEvent event) {
        
        
        
    }
    
    @FXML
    private void saveButtonPressed(ActionEvent event) {
        
    }
    
    
    // an embarressing and ridiculous duplication of code.
    // TODO rework
    // <editor-fold>
    @FXML
    private void Button1pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        this.fileList.set(0, chooser.showOpenDialog(null));
        
        if(fileList.get(0) != null)
        {
            fileName1.setText(fileList.get(0).getName());
            chooser2.setDisable(false);
            receptorConc1.setEditable(true);
        }
    }
    
    @FXML
    private void Button2pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(1, chooser.showOpenDialog(null));
        
        if(fileList.get(1) != null)
        {
            fileName2.setText(fileList.get(1).getName());
            chooser3.setDisable(false);
            receptorConc2.setEditable(true);
            ligandConc2.setEditable(true);
        }
    }
    
    @FXML
    private void Button3pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        
        fileList.set(2, chooser.showOpenDialog(null));
        
        if(fileList.get(2) != null)
        {
            fileName3.setText(fileList.get(2).getName());
            chooser4.setDisable(false);
            receptorConc3.setEditable(true);
            ligandConc3.setEditable(true);
        }
    }
    
    @FXML
    private void Button4pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(3, chooser.showOpenDialog(null));
        
        if(fileList.get(3) != null)
        {
            fileName4.setText(fileList.get(3).getName());
            chooser5.setDisable(false);
            receptorConc4.setEditable(true);
            ligandConc4.setEditable(true);
        }
    }
    
    @FXML
    private void Button5pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(4, chooser.showOpenDialog(null));
        
        if(fileList.get(4) != null)
        {
            fileName5.setText(fileList.get(4).getName());
            chooser6.setDisable(false);
            receptorConc5.setEditable(true);
            ligandConc5.setEditable(true);
        }
    }
    
    @FXML 
    private void Button6pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(5, chooser.showOpenDialog(null));
        
        if(fileList.get(5) != null)
        {
            fileName6.setText(fileList.get(5).getName());
            chooser7.setDisable(false);
            receptorConc6.setEditable(true);
            ligandConc6.setEditable(true);
        }
    }
    
    @FXML 
    private void Button7pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(6, chooser.showOpenDialog(null));
        
        if(fileList.get(6) != null)
        {
            fileName7.setText(fileList.get(6).getName());
            chooser8.setDisable(false);
            receptorConc7.setEditable(true);
            ligandConc7.setEditable(true);
        }
    }
    
    @FXML 
    private void Button8pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(7, chooser.showOpenDialog(null));
        
        if(fileList.get(7) != null)
        {
            fileName8.setText(fileList.get(7).getName());
            chooser9.setDisable(false);
            receptorConc8.setEditable(true);
            ligandConc8.setEditable(true);
        }
    }
    
    @FXML 
    private void Button9pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(8, chooser.showOpenDialog(null));
        
        if(fileList.get(8) != null)
        {
            fileName9.setText(fileList.get(8).getName());
            chooser10.setDisable(false);
            receptorConc9.setEditable(true);
            ligandConc9.setEditable(true);
        }
    }
    
    @FXML 
    private void Button10pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(9, chooser.showOpenDialog(null));
        
        if(fileList.get(9) != null)
        {
            fileName10.setText(fileList.get(9).getName());
            chooser11.setDisable(false);
            receptorConc10.setEditable(true);
            ligandConc10.setEditable(true);
        }
    }
    
    @FXML 
    private void Button11pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(10, chooser.showOpenDialog(null));
        
        if(fileList.get(10) != null)
        {
            fileName11.setText(fileList.get(10).getName());
            chooser12.setDisable(false);
            receptorConc11.setEditable(true);
            ligandConc11.setEditable(true);
        }
    }
    
    @FXML 
    private void Button12pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(11, chooser.showOpenDialog(null));
        
        if(fileList.get(11) != null)
        {
            fileName12.setText(fileList.get(11).getName());
            chooser13.setDisable(false);
            receptorConc12.setEditable(true);
            ligandConc12.setEditable(true);
        }
    }
    
    @FXML 
    private void Button13pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(12, chooser.showOpenDialog(null));
        
        if(fileList.get(12) != null)
        {
            fileName13.setText(fileList.get(12).getName());
            chooser14.setDisable(false);
            receptorConc13.setEditable(true);
            ligandConc13.setEditable(true);
        }
    }
    
    @FXML 
    private void Button14pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(13, chooser.showOpenDialog(null));
        
        if(fileList.get(13) != null)
        {
            fileName14.setText(fileList.get(13).getName());
            chooser15.setDisable(false);
            receptorConc14.setEditable(true);
            ligandConc14.setEditable(true);
        }
    }
    
    @FXML 
    private void Button15pressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        fileList.set(14, chooser.showOpenDialog(null));
        
        if(fileList.get(14) != null)
        {
            fileName15.setText(fileList.get(14).getName());
            receptorConc15.setEditable(true);
            ligandConc15.setEditable(true);
        }
    }
    // </editor-fold>
} // end class SlowExchangeGUIController
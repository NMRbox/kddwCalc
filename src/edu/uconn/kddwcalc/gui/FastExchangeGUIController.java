package edu.uconn.kddwcalc.gui;

import edu.uconn.kddwcalc.analyze.FastExchangeDataAnalyzer;
import edu.uconn.kddwcalc.analyze.AbsFactory;
import edu.uconn.kddwcalc.analyze.ArraysInvalidException;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.data.TypesOfTitrations;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.controlsfx.dialog.ExceptionDialog;  // see copyright notice at bottom

/**
 * Controller class for input of fast exchange NMR titration data. For user data
 * input.
 *
 * @author Alex R.
 *
 * @since 1.8
 */
public class FastExchangeGUIController {

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

    // </editor-fold>
    @FXML Label scalingFactorLabel;
    @FXML TextField scalingFactorTextField;

    @FXML Label typeOfTitrLabel;
    @FXML ToggleGroup typeOfTitrationToggleGroup;
    @FXML RadioButton amideHSQCRadioButton;
    @FXML RadioButton methylHMQCRadioButton;

    @FXML Label orderLabel;
    @FXML ToggleGroup nucleiToggleGroup;
    @FXML RadioButton orderNucleiFirstRadioButton;
    @FXML RadioButton orderNucleiSecondRadioButton;

    @FXML Button resultsOutputButton;
    @FXML TextField resultsOutputTextField;

    @FXML Button loadButton;
    @FXML Button saveButton;
    @FXML Button clearButton;
    @FXML Button analyzeButton;
    
    private static final double AMIDE_HSQC_DEFAULT_MULT = 0.10136;
    private static final double METHYL_HMQC_DEFAULT_MULT = 0.25143;
    private static final String DEFAULT_OUTPUT_RESULTS_DIRECTORY_NAME = "Results";
    private static final int MAX_NUM_EXP_PTS = 15;
    private static final String AMIDE_FIRST_RADIO_BUTTON_MESSAGE = "Nitrogen Proton";
    private static final String AMIDE_SECOND_RADIO_BUTTON_MESSAGE = "Proton Nitrogen";
    private static final String METHYL_FIRST_RADIO_BUTTON_MESSAGE = "Carbon Proton";
    private static final String METHYL_SECOND_RADIO_BUTTON_MESSAGE = "Proton Carbon";
    private static final String AMIDE_HSQC_LABEL = "1H-15N amide HSQC";
    private static final String METHYL_HMQC_LABEL = "1H-13C methyl HMQC";

    // its global because i cant figure out how to pass it to the analyze and save button handlers
    private ReadOnlyObjectWrapper<Path> wrappedResultsOutputFile;
    
    // creates a list with File objects that have references to null.
    // 
    private final ObservableList<Path> fileList = 
            FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(new Path[15])));

            
    private List<TextField> fileNameTextFieldList;
    
    private List<TextField> ligandConcTextFieldList;
    private List<TextField> receptorConcTextFieldList;

    
    
    /**
     * Initializes the GUI. enums are set as <code>userData</code> for the
     * {@link RadioButton} objects. Default set to 1H-15N HSQC settings. This is
     * set redunanly in both the fxml file (so one can see an initialized GUI in
     * scenebuilder) but also in this method for clarity
     *
     * @see TypesOfTitrations
     */
    public void initialize() {

        initializeRadioButtons();
        
        ligandConcTextFieldList = compileLigandConcTextFields();
        receptorConcTextFieldList = compileReceptorConcTextFields();

        Path resultsOutputPath = null;
        wrappedResultsOutputFile = new ReadOnlyObjectWrapper<>(resultsOutputPath, "wrappedResultsOutputFile");
        
        fileNameTextFieldList = compileDataFileTextField();

        initializeAllListeners();

        setToDefaultGUIValues(); // must be after initializeAllListeners()
        
        setTooltips();
    }

    /**
     * Initializes the GUI with amide HSQC values as default.
     */
    private void setToDefaultGUIValues() {
        
        clearFileListAndMakeTextFieldsUneditable(); // on open, doesnt do much. needed for clear button
        
        ligandConc1.setText("0.0"); // line must come after clearFileList...method
        
        typeOfTitrationToggleGroup.selectToggle(amideHSQCRadioButton);
        
        nucleiToggleGroup.selectToggle(orderNucleiFirstRadioButton);
        
        scalingFactorTextField.setText(Double.toString(AMIDE_HSQC_DEFAULT_MULT));

        resultsOutputTextField.setText(DEFAULT_OUTPUT_RESULTS_DIRECTORY_NAME);
        wrappedResultsOutputFile.setValue(Paths.get(
                System.getProperty("user.home"), DEFAULT_OUTPUT_RESULTS_DIRECTORY_NAME).getFileName());
        
        
        // make buttons to choose data files unlickable until. always allow 
        // the button beforehand was chosen. this enforces some order
        // on what the user can do. first line can always be cohosen
        compileDataFileChooserButtons().stream()
                                       .filter(button -> !button.equals(chooser1))
                                       .forEach(button -> button.disableProperty().setValue(true));
    }

    /**
     * Sets userData for all {@link RadioButton} objects.
     */
    private void initializeRadioButtons() {

        amideHSQCRadioButton.setUserData(TypesOfTitrations.AMIDEHSQC);
        methylHMQCRadioButton.setUserData(TypesOfTitrations.METHYLHMQC);
        amideHSQCRadioButton.setText(AMIDE_HSQC_LABEL);
        methylHMQCRadioButton.setText(METHYL_HMQC_LABEL);
        
        orderNucleiFirstRadioButton.setUserData(false);
        orderNucleiSecondRadioButton.setUserData(true);
        orderNucleiFirstRadioButton.setText(AMIDE_FIRST_RADIO_BUTTON_MESSAGE);
        orderNucleiSecondRadioButton.setText(AMIDE_SECOND_RADIO_BUTTON_MESSAGE);
    }

    /**
     * Encapsulates all the listener initializations.
     */
    private void initializeAllListeners() {

        // if the name and location to write the results change, update the textfield below it
        wrappedResultsOutputFile.addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null)
                resultsOutputTextField.setText(newValue.getFileName().toString());
        });

        // if the type of spectrum changes, change the labels for the 
        // order of nuclei to reflect the type of specrum chosen
        typeOfTitrationToggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {

                switch ( (TypesOfTitrations) newValue.getUserData()) {

                    case AMIDEHSQC:
                        orderNucleiFirstRadioButton.setText(AMIDE_FIRST_RADIO_BUTTON_MESSAGE);
                        orderNucleiSecondRadioButton.setText(AMIDE_SECOND_RADIO_BUTTON_MESSAGE);
                        scalingFactorTextField.setText(Double.toString(AMIDE_HSQC_DEFAULT_MULT));
                        break;

                    case METHYLHMQC:
                        orderNucleiFirstRadioButton.setText(METHYL_FIRST_RADIO_BUTTON_MESSAGE);
                        orderNucleiSecondRadioButton.setText(METHYL_SECOND_RADIO_BUTTON_MESSAGE);
                        scalingFactorTextField.setText(Double.toString(METHYL_HMQC_DEFAULT_MULT));
                        break;

                    default:
                        throw new IllegalArgumentException(
                            "Was unable to select type in FastExchangeGUIControler.initializeAllListeners()");
                }
            });
        
        // so textFields update when files are set (either by chooser with button or loading from .ser file)
        fileList.addListener(new ListChangeListener<Path>() {
            
            @Override
            public void onChanged(ListChangeListener.Change change) {
                List<Button> fileChooserButtonList = compileDataFileChooserButtons();
                
                // fileNameTextFieldList is what needs to be updated
                // ObservableList<File> changes and textFields need to update
                for(int ctr = 0; ctr < MAX_NUM_EXP_PTS; ctr++) {
                    
                    if(fileList.get(ctr) != null) {
                        
                        fileNameTextFieldList.get(ctr).setText(fileList.get(ctr).getFileName().toString());
                        
                        if(!(ligandConcTextFieldList.get(ctr).equals(ligandConc1)))
                            ligandConcTextFieldList.get(ctr).setEditable(true);
                        
                        receptorConcTextFieldList.get(ctr).setEditable(true);
                        
                        if(ctr < MAX_NUM_EXP_PTS - 1)
                            fileChooserButtonList.get(ctr + 1).setDisable(false);
                    }  
                    if(fileList.get(ctr) == null) {
                        fileNameTextFieldList.get(ctr).setText("");
                        ligandConcTextFieldList.get(ctr).setEditable(false);
                        receptorConcTextFieldList.get(ctr).setEditable(false);
                    }
                }
            }
        }); 
    }

    /**
     * Executes when the analyze button is pressed. In short, this method
     * begins the meat of the program. The data is compiled into a 
     * <code>RawData</code> object which is the only public interface for
     * the gui package and sent to the analyze package for sorting, fitting, 
     * and output of results.
     *
     * Note: all exceptions should climb back to the catch block in
     * <code>analyzeButtonPressed</code> and show its message in the dialog
     * box. If an exception does occur, the user can close the box and
     * should be able to edit any of the information in the GUI.
     *
     * @param event generated by pressing the Analyze button in the GUI
     *
     * @see AbsFactory
     * @see TitrationSeries
     * @see FastExchangeDataAnalyzer
     * @see RawData
     */
    @FXML 
    private void analyzeButtonPressed (ActionEvent event) {   
        
        Instant start = Instant.now();
        
        try {
           FastExchangeDataAnalyzer.analyze(prepAndMakeRawDataObject());
           
           Instant end = Instant.now(); // for clocking the calculation
           
            System.out.println(Duration.between(start, end).toNanos() / 1_000_000L + " ms");
           
            displayResultsWrittenPopUp(); // only reach this if extensive validation passed
        }

        // note: NumberFormatException will be caught by its superclass IllegalArgumentException
        //       FileNotFoundException hanled by IOException
        catch (IllegalArgumentException | NullPointerException | IOException | SecurityException |
            FormatterClosedException | NoSuchElementException | ArraysInvalidException e) {

            showExceptionDialog(e);
        }
    } // end method executeButtonPressed

    /**
     * Allows the user the save the data from the GUI to a binary file which
     * can be read later by pressing <code>loadButtonPressed</code>. Note
     * that all data must be filled out to allow the save.
     *
     * @param event the {@link ActionEvent} that occurred
     *
     * @throws IOException if can't write the {@link FastExchangeGUISave}
     * object
     */
    @FXML
    private void saveButtonPressed(ActionEvent event) 
        throws IOException, ArraysInvalidException {
        
        try {
            RawData instanceToSave = prepAndMakeRawDataObject();
        
            Optional<Path> saveFile = 
                useSaveChooser("Save Input Data", "inputDataForGUI.ser");

            if (saveFile.isPresent()) {

                try (ObjectOutputStream output = 
                    new ObjectOutputStream(Files.newOutputStream(saveFile.get()))) {

                    output.writeObject(instanceToSave);

                    // if all went well, reached this point
                    displayResultsWrittenPopUp();
                } 
            }
        } 
        catch (IllegalArgumentException | NullPointerException | SecurityException |
                    NoSuchElementException | ClassCastException | IOException e) {
            showExceptionDialog(e);
        }  
    }

    /**
     * A method to read a previously saved {@link FastExchangeGUISave}
     * object. This populates the data into the GUI, then the user must
     * press the "analyze" button to finish. Effectively, this method unwraps
     * an {@link RawData} object.
     *
     * Note: based on the listeners in <code>initialize</code> which update
     * the {@link TextField} with the multiplier, the multiplier must be set
     * after the {@link RadioButton} for the type of titration has been
     * selected
     *
     * @param event the {@link ActionEvent} that occurred
     *
     * @throws IOException if can't read the chosen data file
     */
    @FXML
    private void loadButtonPressed
    (ActionEvent event) throws IOException, ArraysInvalidException {

        Optional<Path> openFile = useOpenChooser("Open Saved Data", "Serialized Files", "*.ser");

        if (openFile.isPresent()) {

            
            try (ObjectInputStream input = 
                new ObjectInputStream(Files.newInputStream(openFile.get()))) {

                RawData savedData = (RawData) input.readObject();

                // best way i could think of quickly with least dependencies.
                typeOfTitrationToggleGroup.selectToggle(
                    typeOfTitrationToggleGroup.getToggles()
                    .stream()
                    .filter(toggle -> 
                        toggle.getUserData() == savedData.getType())
                    .limit(1)
                    .findFirst()
                    .get());

                nucleiToggleGroup.selectToggle(
                    nucleiToggleGroup.getToggles()
                    .stream()
                    .filter(toggle -> 
                        (boolean) toggle.getUserData() == savedData.getResonanceReversal())
                    .limit(1)
                    .findFirst()
                    .get());

                scalingFactorTextField.setText(
                    savedData.getScalingFactor().toString());

                // listeners update the corresponding textfields
                wrappedResultsOutputFile.setValue(
                    savedData.getResultsDirectoryFile().toPath().toAbsolutePath());

                fillProteinConcTextFields(savedData.getLigandConcs(), 
                                          savedData.getReceptorConcs());

                
                
                List<Path> savedFileList = 
                    savedData.getDataFiles()
                             .stream()
                             .map(File::toPath)
                             .collect(Collectors.toList());
                    
                for(int ctr = 0; ctr < MAX_NUM_EXP_PTS; ctr++) {
                    if (ctr < savedFileList.size())
                        fileList.set(ctr, savedFileList.get(ctr));
                    else 
                        fileList.set(ctr, null);
                }


            } catch (IllegalArgumentException | NullPointerException | 
                SecurityException | NoSuchElementException | ClassCastException | 
                IOException | ClassNotFoundException e) {

                showExceptionDialog(e);
            }
        }
    }


    /**
     * Clears the GUI and resets to default parameters.
     * 
     * @param event The {@link ActionEvent} that occurred when the clear button was pressed
     */
    @FXML
    private void clearButtonPressed(ActionEvent event) {
            setToDefaultGUIValues(); 
        }

    private void clearFileListAndMakeTextFieldsUneditable() {

        for (int ctr = 0; ctr < fileList.size(); ctr++) {
            fileList.set(ctr, null);
        }
        
        ligandConcTextFieldList.stream()
            .forEach(field -> {
                field.clear();
                field.setEditable(false);
            });
        
        receptorConcTextFieldList.stream()
            .forEach(field -> {
                field.clear();
                field.setEditable(false);
            });
    }
        
        
    /**
     * Prepares the data from the user for {@link AbsFactory} or saving by performing
     * some validation of the input (validation inside {@link RawData} and creating the {@link RawData}
     * instance. {@link RawData} is passed to {@link AbsFactory} or saved.
     *
     * @return preliminary unsorted peak lists and ligand concentrations
     *
     * @throws IOException
     * @throws Arrays
     */
    private RawData prepAndMakeRawDataObject()throws IOException, ArraysInvalidException {

        return RawData.createRawData(fileList.stream()
                                             .filter(path -> path != null)
                                             .map(Path::toFile)
                                             .collect(Collectors.toList()), 
                                     ligandConcTextFieldList,
                                     receptorConcTextFieldList, 
                                     scalingFactorTextField.getText(), 
                                     getResonanceReversal(),
                                     getTypeOfTitration(),
                                     wrappedResultsOutputFile.get().toFile());
    }

    /**
     * Makes a {@link List} from a variable number of arguments array.
     *
     * @param <T> object type that will go into the {@link List}
     *
     * @param object individual instance to add to a {@link List}
     *
     * @return array containing the elements that were formerly in a
     * {@link List}
     * 
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

        Optional<Path> path = useSaveChooser("Save Results", DEFAULT_OUTPUT_RESULTS_DIRECTORY_NAME);
        
        if (path.isPresent()) {
             wrappedResultsOutputFile.setValue(path.get());
        }
       
    }

    /**
     * If the program reaches this method, then results were written to disk. 
     * This informs the user that this has happened.
     */
    private void displayResultsWrittenPopUp() {

        Alert alert
            = new Alert(Alert.AlertType.INFORMATION, "Results were written to disk (a good sign!)");

        alert.setTitle("Application Status");

        alert.showAndWait();
    }

    // an embarressing and ridiculous duplication of code.
    // TODO rework
    // <editor-fold>
    @FXML
    private void Button1pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        this.fileList.set(
            0, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button2pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            1, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button3pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            2, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button4pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            3, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button5pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            4, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button6pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            5, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button7pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            6, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button8pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            7, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button9pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            8, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button10pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            9, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button11pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            10, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button12pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            11, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button13pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            12, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button14pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            13, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }

    @FXML
    private void Button15pressed(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        fileList.set(
            14, chooser.showOpenDialog(null).toPath().toAbsolutePath());
    }
    // </editor-fold>

    /**
     * Aggregates the ligand concentration {@link TextField} objects into a
     * {@link List}
     *
     * @return the ligand concentration {@link TextField} objects in a list
     */
    private List<TextField> compileLigandConcTextFields() {
        return makeListOfObjects(ligandConc1, ligandConc2, ligandConc3,
            ligandConc4, ligandConc5, ligandConc6,
            ligandConc7, ligandConc8, ligandConc9,
            ligandConc10, ligandConc11, ligandConc12,
            ligandConc13, ligandConc14, ligandConc15);
    }

    /**
     * Aggregates the receptor concentration {@link TextField} objects into a
     * {@link List}
     *
     * @return the receptor concentration {@link TextField} objects in a list
     */
    private List<TextField> compileReceptorConcTextFields() {
        return makeListOfObjects(receptorConc1, receptorConc2, receptorConc3,
            receptorConc4, receptorConc5, receptorConc6,
            receptorConc7, receptorConc8, receptorConc9,
            receptorConc10, receptorConc11, receptorConc12,
            receptorConc13, receptorConc14, receptorConc15);
    }

    /**
     * Aggregates the {@link TextField} objects which display the data file
     * (peak lists) name into a {@link List}
     *
     * @return a {@link List} of {@link TextField} objects where the name of the
     * data files will be displayed;
     */
    private List<TextField> compileDataFileTextField() {
        return makeListOfObjects(fileName1, fileName2, fileName3,
            fileName4, fileName5, fileName6,
            fileName7, fileName8, fileName9,
            fileName10, fileName11, fileName12,
            fileName13, fileName14, fileName15);
    }

    /**
     * Aggregates the {@link Button} objects that will be used for choosing the
     * data files
     *
     * @return a collection of {@link Button} instances that are used to select
     * data files
     */
    private List<Button> compileDataFileChooserButtons() {
        return makeListOfObjects(chooser1, chooser2, chooser3,
            chooser4, chooser5, chooser6,
            chooser7, chooser8, chooser9,
            chooser10, chooser11, chooser12,
            chooser13, chooser14, chooser15);
    }

    /**
     * Gets the enum which indicates which type of titration (which nuclei) was
     * performed
     *
     * @return an enum indicating which type of titration was performed
     */
    private TypesOfTitrations getTypeOfTitration() {
        return (TypesOfTitrations) typeOfTitrationToggleGroup.getSelectedToggle().getUserData();
    }

    /**
     * Gets information about the order of chemical shifts in the peak lists
     *
     * @return indicates whether the values should be reversed going forward
     */
    private boolean getResonanceReversal() {
        return (boolean) nucleiToggleGroup.getSelectedToggle().getUserData();
    }

    /**
     * Shows an exception dialog from ControlsFX with the message from the
     * parameter
     *
     * @param e the exception that was throw which contains the message for the
     * dialog box
     */
    private void showExceptionDialog(Exception e) {

        ExceptionDialog dialog = new ExceptionDialog(e);

        if (e.getMessage() == null) {
            dialog.setHeaderText("Exception was caught without a message. Might be a good idea to copy/paste "
                + "and save the stacktrace that can be accessed with the carrot in this box. Thank you.");
        }

        dialog.showAndWait();
    }

    /**
     *  Opens a {@link FileChooser} and returns the {@link File} to save to.
     * 
     * @param title the title to apply to the dialog window
     * @param defaultFileName the name of the file that will be written to
     *
     * @return the name and location where the data will be saved which is
     * chosen from {@link FileChooser}
     */
    private Optional<Path> useSaveChooser(String title, String defaultFileName) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle((title));
        chooser.setInitialFileName(defaultFileName);

        File file = chooser.showSaveDialog(null);

        return file == null ? Optional.empty() : Optional.of(file.toPath().toAbsolutePath());
    }

    /**
     * Opens a {@link FileChooser} and returns the {@link File} it opened.
     * 
     * @param title the title to apply to the dialog window
     *
     * @return the name and location chosen by the user in the {link
     * FileChooser}
     */
    private Optional<Path> useOpenChooser(String title, String extTitle, String extFilter) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle((title));
        chooser.getExtensionFilters().addAll(
            new ExtensionFilter(extTitle, extFilter),
            new ExtensionFilter("All Files", "*.*"));

        File file = chooser.showOpenDialog(null);
        
        return file == null ? Optional.empty() : Optional.of(file.toPath().toAbsolutePath());
        
    }

    /**
     * Populates the {@link TextField} objects in the GUI with the receptor or ligand concentrations
     * (that probably came from a {@link FastExchangeGUISave} object
     * 
     * @param savedLigandConcs the total ligand concentrations
     * @param savedReceptorConcs the total receptor concentrations
     */
    private void fillProteinConcTextFields(final double[] savedLigandConcs, 
                                           final double[] savedReceptorConcs) {
        
        for(int ctr = 0; ctr < savedLigandConcs.length; ctr++) {
            
            updateTextField(ligandConcTextFieldList.get(ctr), savedLigandConcs[ctr]);
            updateTextField(receptorConcTextFieldList.get(ctr), savedReceptorConcs[ctr]);    
        }
    }
    
    /**
     * Updates a the text in an {@link TextField} object with a protein concentrations
     * that probably came from a {@link FastExchangeGUISave} object
     * 
     * @param textField holds a ligand or receptor concentration
     * @param savedConc the concentration
     */
    private void updateTextField(TextField textField,
                                 double savedConc) {
        
        if (!textField.equals(ligandConc1))
            textField.setEditable(true);
        
        textField.setText(Double.toString(savedConc));
    }

    /**
     * Sets {@link Tooltips} for all the nodes in the GUI
     */
    private void setTooltips() {
        
        resultsOutputButton.setTooltip(
            new Tooltip("Press to select name and location where results will be saved"));
        
        resultsOutputTextField.setTooltip(
            new Tooltip("Name of file where results will be printed"));
        
        clearButton.setTooltip(
            new Tooltip("Clears the GUI and resets to default values"));
        
        saveButton.setTooltip(
            new Tooltip("Press to choose location to saved data. Note that only a complete dataset\n"
                + "with valid values can be saved"));
        
        loadButton.setTooltip(
            new Tooltip("Press to find and load a saved dataset into the GUI"));
        
        analyzeButton.setTooltip(
            new Tooltip("Press to analyze data. This includes validation and sorting.\n"
                + "Data and results will be saved to disk in the locations indicated"));
        
        Tooltip typeTooltip = new Tooltip("Select type of experiment performed.\n"
            + "This will affect validation");
        
        typeOfTitrLabel.setTooltip(typeTooltip);
        amideHSQCRadioButton.setTooltip(typeTooltip);
        methylHMQCRadioButton.setTooltip(typeTooltip);
        
        Tooltip orderTooltip = new Tooltip("Select the order in which the resonances appear\nin the "
            + "peak lists. ex: 123.34  7.8 would be \"Nitrogen Proton\"");
        
        orderLabel.setTooltip(orderTooltip);
        orderNucleiFirstRadioButton.setTooltip(orderTooltip);
        orderNucleiSecondRadioButton.setTooltip(orderTooltip);
        
        
        Tooltip multiplierTooltip = new Tooltip("Enter the value that will be used two scale the two nuclei.\n"
            + "Note how the default values scale by gyromagnetic ratio");
        
        scalingFactorLabel.setTooltip(multiplierTooltip);
        scalingFactorTextField.setTooltip(multiplierTooltip);
        
        
        ligandConcTextFieldList.stream()
                                     .forEach(field -> field.setTooltip(new Tooltip(
                                         "Enter ligand conc in uM. If this TextField isn't editable\n"
                                             + "then you need to choose a file on this line first")));
        
        receptorConcTextFieldList.stream()
                                       .forEach(field -> field.setTooltip(new Tooltip(
                                           "Enter receptor conc in uM. If this TextField isn't editable\n"
                                               + "then you need to choose a file on this line first")));
            
  
        compileDataFileChooserButtons().stream()
                                       .forEach(field -> field.setTooltip(new Tooltip(
                                           "Press to choose a peak list file")));
        
        compileDataFileTextField().stream()
                                  .forEach(field -> field.setTooltip(new Tooltip(
                                      "Name of peak list file")));
    }
} // end class FastExchangeGUIController
 

// The exception dialog was from ControlsFX
/**
 * Copyright (c) 2013, 2014, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
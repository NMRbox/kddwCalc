/* created by Alex Ri on 170704
*
* After the user presses "Analyze" or "Execute" in the GUI, the user input needs to be 
* gathered and sent to the factory to parse. This class is sent to the 
* Template method in the abstractFactory class 
* 
* The object is created using a simple static factory method (createRawData) so I could
* add a couple of small validations. 1) each ArrayList must have same number of elements
* and 2) each text file associated with each path must have the same number of lines. 
*
* multipler is how to scale the two nuclei (carbon/proton different than carbon/nitrogen)
*
* resonanceReverseal is related to how the points are listed in the text files (the order),
* i.e. is it proton then carbon, or carbon then proton.
*/

package edu.uconn.kddwcalc.gui;

import edu.uconn.kddwcalc.sorting.ArraysInvalidException;
import edu.uconn.kddwcalc.sorting.DataArrayValidator;
import edu.uconn.kddwcalc.data.TypesOfTitrations;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.TextField;

/**
 * A class that contains raw data and the location of raw data from the user.
 * 
 * As of 171008, this can be saved and then loaded to populate GUI data fields
 *  or it can be sent to {@link edu.uconn.kddwcalc.fitting.LeastSquaresFitter#fit} to get 
 * {@link edu.uconn.kddwcalc.fitting.Results} object back
 * 
 * @author Alex R.
 * 
 * @since 1.8
 */
public class RawData implements Serializable {
    
    private final List<File> dataFiles;
    private final double[] ligandConcs;
    private final double[] receptorConcs;
    private final double multiplier;
    private final boolean resonanceReversal;
    private final TypesOfTitrations type;
    private final File dataOutputFile;
    private final File resultsFile;

    
    /**
     * Private seven-argument {@link RawData} constructor. Should only reach this point after quite a bit of validation has been
     * done.
     * 
     * @param dataFiles Location on disk of peak list data from user.
     * @param ligandConcs Total ligand concentrations
     * @param receptorConcs Total receptor concentration
     * @param multiplier A number to scale two different nuclei
     * @param resonanceReversal A flag that helps keep track of resonance ordering in data files
     * @param type The type of titration selected (i.e. 1H-15N HSQC)
     * @param dataOutputFile name and location where sorted peak lists will be output
     * @param resultsFile name and location where results will be output
     */
    private RawData(List<File> dataFiles, 
                    double[] ligandConcs,
                    double[] receptorConcs, 
                    double multiplier, 
                    boolean resonanceReversal,
                    TypesOfTitrations type,
                    File dataOutputFile,
                    File resultsFile) {
        this.dataFiles = dataFiles;
        this.ligandConcs = ligandConcs;
        this.receptorConcs = receptorConcs;
        this.multiplier = multiplier;
        this.resonanceReversal = resonanceReversal;
        this.type = type;
        this.dataOutputFile = dataOutputFile;
        this.resultsFile = resultsFile;
    }
    
    /**
     * A static simple factory that contains validation for creation of a {@link RawData} object. 
     * 
     * The dataFiles, ligandConcs, and receptorConcs lists/array must have the same length. This method
     * filters the ones with null values or null strings and then compares the arrays lengths. The arrays or list 
     * are stored in their truncated form.
     * 
     * Similarly, the text files scanned in from dataFiles must all have the same number of lines. This
     * is tested
     * 
     * @param dataFiles Location on disk of peak list data from user
     * @param ligandConcs Total ligand concentrations
     * @param receptorConcs Total receptor concentration
     * @param multiplier A number to scale two different nuclei
     * @param resonanceReversal A flag that helps keep track of resonance ordering in data files
     * @param type which nuclei were observed in experiment. Affects validation
     * @param dataOutputFile name and location to write sorted peak lists
     * @param resultsFile name and location to write the results text file
     * 
     * @return A {@link RawData} with unsorted peak lists and other info from user
     * 
     * @throws IOException if an I/O error occurs opening dataFiles
     * @throws IllegalArgumentException if the lists are not the same length
     * @throws IllegalArgumentException if the underlying text files in dataFiles have a different number of lines
     * @throws ArraysInvalidException if the lists have different lengths
     */
    public final static RawData createRawData(List<File> dataFiles, 
                                              List<TextField> ligandConcs,
                                              List<TextField> receptorConcs, 
                                              String  multiplier, 
                                              boolean resonanceReversal,
                                              TypesOfTitrations type,
                                              File dataOutputFile,
                                              File resultsFile) 
                                              throws IOException, ArraysInvalidException {
        
        /*
        * The three lists have the concentrations and location of peak lists, they
        * must be the same length. 
        */
        List<File> tempFileList = dataFiles.stream()
                                           .filter(file -> file != null)
                                           .collect(Collectors.toList());
        
        double[] tempLigandArray = ligandConcs.stream()
                                              .filter(field -> !(field.getText().equalsIgnoreCase("")))
                                              .mapToDouble(field -> Double.valueOf(field.getText()))
                                              .toArray();
        
        double[] tempReceptorArray = receptorConcs.stream()
                                                  .filter(field -> !(field.getText().equalsIgnoreCase("")))
                                                  .mapToDouble(field -> Double.valueOf(field.getText()))
                                                  .toArray();
        
        
        // tests to make sure the lists above are the same length. must because theres one for each exp point
        if (!DataArrayValidator.isListLengthsAllEqual(tempFileList, tempLigandArray, tempReceptorArray))
            throw new ArraysInvalidException("List had different lengths in RawData.createRawData. Might be an"
                + " issue with duplication in peak lists or missing concentratoion value");
        
        /*
        * This block of code tests each peak list to ensure it has the same number of lines. Each line is a residue,
        * so each file must have the same number of lines.
        */
        long[] numLines = new long[tempFileList.size()];
        
        // now count the number of lines in each file
        try {
            for (int ctr = 0; ctr < tempFileList.size(); ctr++)
            {
                numLines[ctr] = Files.lines(tempFileList.get(ctr).toPath()).count();
            }
        }
        catch(IOException e) {

        // if every number in that array isnt the same    
         if (Arrays.stream(numLines).distinct().count() != 1)
             throw new IllegalArgumentException("Data files dont all have same number of lines in"
                     + "RawData.createRawData");
         
         throw new IllegalArgumentException("Error when opening file in class RawData", e);
        
        }
        
        if (type == null || dataOutputFile == null || resultsFile == null)
            throw new NullPointerException("null found when in RawData.createRawData");
        
        
        // if this statement is reached, all the validation was passed 
        return new RawData(tempFileList, 
                           tempLigandArray,
                           tempReceptorArray, 
                           Double.valueOf(multiplier), 
                           resonanceReversal,
                           type,
                           dataOutputFile, 
                           resultsFile); 
    }
    
    
    
    
    
    
    
    /**
     * Gets the {@link java.nio.file.Path Path} for the location of the text files with the NMR chemical shift data.
     * 
     * @return a <code>List{@literal <}Path{@literal >}</code> object with the location of the peak lists
     */
    public final List<File> getDataFiles() {
        return dataFiles;
    }
    
    /**
     * Gets the ligand concentrations
     * 
     * @return the ligand concentrations
     */
    public final double[] getLigandConcs() {
        return ligandConcs;
    }
    
    /**
     * Gets the receptor concentrations. The receptor is the labeled (observed) species
     * 
     * @return the receptor concentrations
     */
    public final double[] getReceptorConcs() {
        return receptorConcs;
    }
    
    /**
     * Get the multiplier, which is the value used to scale the two different nuclei. This value
     * was provided by the user
     * 
     * @return the multiplier value
     */
    public final Double getMultiplier() {
        return multiplier;
    }
    
    /**
     * Gets the resonance reversal flag. This is a way to keep track of the order that the 
     * nuclei are listed in the peak lists. For instance, CCPNmr Analysis exports peak lists in the order
     * [nitrogen, proton], so if the peak lists are in this order, this value should be false. If the order
     * in the text file is [proton, nitrogen], this flag should be true. A better way to manage this
     * probably exists but its done now. 
     * 
     * @return the resonance reversal flag
     */
    public final boolean getResonanceReversal() {
        return resonanceReversal;
    }
    
    /**
     * Gets the information about which nuclei were observed in experiment
     * 
     * @return enum with information about which type was performed
     */
    public final TypesOfTitrations getType() {
        return type;
    }
    
    /**
     * Gets the name/location where sorted peak lists will be written
     * 
     * @return the object containing name/location info
     */
    public final File getDataOutputFile() {
        return dataOutputFile;
    }
    
    /**
     * Gets the name/location where results will be written
     * 
     * @return the object containing name/location info
     */
    public final File getResultsFile() {
        return resultsFile;
    }
    
    /**
     * Takes the <code>List{@literal <}File{@literal >}</code> and turns it into
     * a <code>List{@literal <}Path{@literal >}</code> . 
     *
     * @return {@link List} objects with locations of NMR chemical shift peak
     * list.
     */
    public final List<Path> getDataPaths() {
        return new ArrayList<>(dataFiles.stream()
                                        .map(File::toPath)
                                        .collect(Collectors.toList()));
    }
}

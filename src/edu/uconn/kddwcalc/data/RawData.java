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

package edu.uconn.kddwcalc.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that contains raw data and the location of raw data from the user.
 * 
 * @author Alex R.
 * 
 * @since 1.8
 */
public final class RawData {
    private final List<Path> dataFiles;
    private final List<Double> ligandConcs;
    private final List<Double> receptorConcs;
    private final double multiplier;
    private final boolean resonanceReversal;
    
    /**
     * Four argument {@link RawData} constructor. Should only reach this point after quite a bit of validation has been
     * done.
     * 
     * @param dataFiles Location on disk of peak list data from user
     * @param ligandConcs Total ligand concentrations
     * @param receptorConcs Total receptor concentration
     * @param multiplier A number to scale two different nuclei
     * @param resonanceReversal A flag that helps keep track of resonance ordering in data files
     */
    private RawData(List<Path> dataFiles, 
                    List<Double> ligandConcs,
                    List<Double> receptorConcs, 
                    double multiplier, 
                    boolean resonanceReversal) {
        this.dataFiles = dataFiles;
        this.ligandConcs = ligandConcs;
        this.receptorConcs = receptorConcs;
        this.multiplier = multiplier;
        this.resonanceReversal = resonanceReversal;
    }
    
    /**
     * A static simple factory that contains validation for creation of a {@link RawData} object. 
     * The dataFiles, ligandConcs, and receptorConcs lists/array must have the same length. Similarly,
     * the text files scanned in from dataFiles must all have the same number of lines.
     * 
     * @param dataFiles Location on disk of peak list data from user
     * @param ligandConcs Total ligand concentrations
     * @param receptorConcs Total receptor concentration
     * @param multiplier A number to scale two different nuclei
     * @param resonanceReversal A flag that helps keep track of resonance ordering in data files
     * 
     * @return A {@link RawData} with unsorted peak lists and other info from user
     * 
     * @throws IOException if an I/O error occurs opening dataFiles
     * @throws IllegalArgumentException if the lists are not the same length
     * @throws IllegalArgumentException if the underlying text files in dataFiles have a different number of lines
     */
    public final static RawData createRawData(List<Path> dataFiles, 
                                              List<Double> ligandConcs,
                                              List<Double> receptorConcs, 
                                              double multiplier, 
                                              boolean resonanceReversal) 
                                              throws IOException {
        // bit of code to make sure user entered and equal number of dataFiles, ligand conccentrations...
        // TODO insert the exception to check list lengths here
        
        
        
        

        // validation to ensure equal number of lines in each data file
        
        // this array holds the number of lines in each file
        long[] numLines = new long[dataFiles.size()];
        
        // now count the number of lines in each file
        try {
            for (int ctr = 0; ctr < dataFiles.size(); ctr++)
            {
                numLines[ctr] = Files.lines(dataFiles.get(ctr)).count();
            }
        }
        catch(IOException e) {
            System.err.println("Error when opening file in class RawData");

        
        // if every number in that array isnt the same    
         if (Arrays.stream(numLines).distinct().count() != 1)
             throw new IllegalArgumentException("Data files dont all have same number of lines");
        
        }
        
        // if this statement is reached, all the validation was passed 
        return new RawData(dataFiles, ligandConcs,receptorConcs, multiplier, resonanceReversal);
          
        
    }

    /**
     * Gets the {@link java.nio.file.Path Path} for the location of the text files with the NMR chemical shift data.
     * 
     * @return a <code>List{@literal <}Path{@literal >}</code> object with the location of the peak lists
     */
    public final List<Path> getDataFiles() {
        return dataFiles;
    }
    
    /**
     * Gets the ligand concentrations
     * 
     * @return the ligand concentrations
     */
    public final List<Double> getLigandConcs() {
        return ligandConcs;
    }
    
    /**
     * Gets the receptor concentrations. The receptor is the labeled (observed) species
     * 
     * @return the receptor concentrations
     */
    public final List<Double> getReceptorConcs() {
        return receptorConcs;
    }
    
    /**
     * Get the multiplier, which is the value used to scale the two different nuclei. This value
     * was provided by the user
     * 
     * @return the multiplier value
     */
    public final double getMultiplier() {
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
}

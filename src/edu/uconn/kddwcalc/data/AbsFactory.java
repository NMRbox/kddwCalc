/* created by Alex Rizzo on 170701
*
*
// 
// important note:
// for the three ArrayList objects, each index matches the same
// indicies for the other two. for instance, index[0] has the 
// receptor and ligand concentrations and the chemical shifts for
// the first titration point. these are the data from teh first line of the
// GUI that requests the data
*
* edited by Alex Ri on 170704 and 170706 
*
* edited by Alex R on 170712 to remove duplicate code for creating point and resonances 
*     from the subclass by making abstract methods in this class and delegating specifics
*     to overriden methods in subclasses
*
       essentially the factories take the data from the user and break it up 
    //     into individual titrations by residue. Titration class represents a single 
    //     residue and contains all the information needed to calculate kd and dw
*
*  class RawData has: 
*     private final ArrayList<Path> dataFiles;
*     private final ArrayList<Double> ligandConcs;
*     private final ArrayList<Double> receptorConcs;
*     private final double multiplier;
*     private final boolean resonanceReversal;

*/

package edu.uconn.kddwcalc.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract superclass in an abstract factory pattern hierarchy. In the template method 
 * <code>analyzeDataFile</code> the unsorted data comes from the user and is sorted into 
 * a <code>List{@literal <}Titration{@literal >}</code>. A <code>Titration</code> is composed of a 
 * a <code>List{@literal <}TitrationPoint{@literal >}</code>. A <code>TitrationPoint</code> is composed
 * of two <code>Resonance</code>. 
 * 
 * Where appropriate for creation of implementation specifics, abstract
 * methods are called which are overridden in the subclasses.
 * 
 * @author Alex R.
 * 
 * @see TitrationPoint
 * @see Titration
 * @see FactoryMaker
 * @see RawData
 * 
 * @since 1.8
 */
public abstract class AbsFactory {
    
    /**
     * A <code>RawData</code> instance is analyzed to return a <code>TitrationSeries</code>. 
     * 
     * @param dataObject contains the unsorted peak lists and protein concentrations
     * 
     * @return a <code>TitrationSeries</code> containing protein concentrations and chemical shifts (all
     * data required for fitting)
     */
    public final TitrationSeries analyzeDataFiles(RawData dataObject) {
        final TitrationSeries dataSet = new TitrationSeries();
        
        // open files by getting a List<Scanner> from the List<Path>
        final List<Scanner> scanners = makeScannersFromPaths(dataObject.getDataFiles());
        
        // iterate through the files and concentrations and group them
        //    as long as there is more data in the first file.
        // each iteration of the while loop creates a full titration curve
        //    for a single residue
        while(scanners.get(0).hasNext()) {
       
            final Titration titration = new Titration(dataObject.getMultiplier());
            
            // each iteration of for loop adds a single point to the titration.
            // the control variable is the number of data files, which is the 
            //   number of individial titraiton points collected
            for(int ctr = 0; ctr < dataObject.getDataFiles().size(); ctr++) {
                TitrationPoint point = makeTitrationPoint(scanners.get(ctr), 
                    dataObject.getLigandConcs().get(ctr), dataObject.getReceptorConcs().get(ctr),
                    dataObject.getResonanceReversal());
                
                titration.addPoint(point);
            }
            
            dataSet.addTitration(titration);   
        }
        
        closeFiles(scanners);

        return dataSet;
    }    
    
    /**
     * Takes the <code>Path</code> objects from the user and converts them to <code>Scanner</code> objects
     * 
     * @param paths the location of the text files with peak lists
     * 
     * @return <code>Scanner</code> instances to begin reading peak list data
     */
    private List<Scanner> makeScannersFromPaths(List<Path> paths) {
        final List<Scanner> scanners = new ArrayList<>();
        
        try {
            for (Path pth : paths) {
                scanners.add(new Scanner(pth));
            }
        }
        catch(IOException e) {
            System.err.println("Exception when change List<Path> to List<Scanner> in AbsFactory");
        }
        
        return scanners;
    }
    
    /**
     * 
     * @param scanner instance to read chemical shift data from peak lists
     * @param ligandConc a ligand concentration
     * @param receptorConc a double concentration
     * @param resonanceReversal the resonance reversal flag (related to order of nuclei in peak lists)
     * 
     * @return instance with all variables initialized
     */
    public TitrationPoint makeTitrationPoint(Scanner scanner, 
                                             double ligandConc, 
                                             double receptorConc, 
                                             boolean resonanceReversal) {
        
        final Resonance[] twoCoordinates = makeTwoResonances(scanner, resonanceReversal);

        final TitrationPoint point = makeTitrationPointSubclass(ligandConc, 
                                                                receptorConc, 
                                                                twoCoordinates[0], 
                                                                twoCoordinates[1]);
        return point;
    }
    
    /**
     * A method that reads two <code>Resonance</code> objects from a data file. These two should
     * be from the same line. 
     * 
     * Note: this method ultimately handles the issue of how the nuclei are ordered in the peak lists.
     * As of 170823, first <code>Resonance</code> is either a nitrogen or carbon and second 
     * <code>Resonance</code> is the matching proton.
     * 
     * Note: call the methods that are overridden in subclass to get correct <code>Resoonance</code> subclass
     * 
     * @param scanner object used to read chemical shift data from peak lists
     * @param resonanceReversal the resonance reversal flag
     * 
     * @see Resonance
     * @see TitrationPoint
     * 
     * @return two resonance objects that are appropriately ordered for when they get scaled 
     * by <code>multiplier</code> and used to calculate chemical shift perturbations
     */
    public Resonance[] makeTwoResonances(Scanner scanner, 
                                         boolean resonanceReversal) {
       
       final Resonance[] twoResonances = new Resonance[2];
       
       if(!resonanceReversal) {
           twoResonances[0] = getFirstResonanceSubclass(scanner);
           twoResonances[1] = getSecondResonanceSubclass(scanner);
       }
       else if (resonanceReversal) {
           twoResonances[1] = getSecondResonanceSubclass(scanner);
           twoResonances[0] = getFirstResonanceSubclass(scanner); 
       }
       
       return twoResonances;   
    }

    /**
     * A method to read in a chemical shift. Note that the actual creation of the <code>Resonance</code>
     * is delegated to subclass to make concrete <code>AmideNitrogen</code> or <code>MethylCarbon</code> 
     * <code>Resonance</code>.
     * 
     * @param scanner instance to read chemical shift data from peak list
     * 
     * @see Resonance
     * @see AmideNitrogen
     * @see MethylCarbon

     * @return a concrete subclass of <code>Resonance</code> based on overridden method call
     */
    public abstract Resonance getFirstResonanceSubclass(Scanner scanner);
    
    /**
     * An abstract method to read in a chemical shift. Note that the actual creation of the <code>Resonance</code>
     * is delegated to subclass to make concrete <code>AmideProton</code> or <code>MethylProton</code> 
     * <code>Resonance</code> objects.
     * 
     * @param scanner instance to read chemical shift data from peak list
     * 
     * @see Resonance
     * @see AmideProton
     * @see MethylProton
     * 
     * @return a concrete subclass of <code>Resonance</code> based on overridden method call
     */
    public abstract Resonance getSecondResonanceSubclass(Scanner scanner);
    
    // delegates creation of methyl or amide titration point to subclass
    /**
     * An abstract method to create a concrete subclass of <code>TitrationPoint</code>. Note how creation of the 
     * concrete subclasses is done with overridden methods in subclasses of <code>AbsFactory</code>
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param firstCoordinate an NMR chemical shift
     * @param secondCoordinate an NMR chemical shift
     * 
     * @return an instance of <code>TitrationPoint</code> with all variables initialized
     */
    public abstract TitrationPoint makeTitrationPointSubclass(double ligandConc,  
                                                              double receptorConc, 
                                                              Resonance firstCoordinate, 
                                                              Resonance secondCoordinate);

    /**
     * Closes a <code>Scanner</code> object
     * 
     * @param scanners the object to close
     */
    private void closeFiles(List<Scanner> scanners) {
        scanners.stream()
                .forEach(Scanner::close);
    }
    
}

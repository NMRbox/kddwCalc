package edu.uconn.kddwcalc.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract superclass in an abstract factory pattern hierarchy. In the template method 
 * {@link #analyzeDataFiles analyzeDataFiles}, the unsorted data comes from the user and is sorted into 
 * a <code>List{@literal <}Titration{@literal >}</code>. A {@link Titration} is composed of a 
 * a <code>List{@literal <}TitrationPoint{@literal >}</code>. A {@link TitrationPoint} is composed
 * of two {@link Resonance} objects and two protein concentrations (receptor and ligand). 
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
     * A {@link RawData} instance is sorted by residue to return a {@link TitrationSeries}. 
     * 
     * @param dataObject contains the unsorted peak lists and protein concentrations
     * 
     * @return a {@link TitrationSeries} containing protein concentrations and chemical shifts (all
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
     * Takes the {@link java.nio.file.Path} objects from the user and converts them to {@link Scanner} objects
     * 
     * @param paths the location of the text files with peak lists
     * 
     * @return {@link Scanner} instances to begin reading peak list data
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
     * A method that reads two {@link Resonance} objects from a data file. These two should
     * be from the same line and thus the same residue. 
     * 
     * Note: this method ultimately handles the issue of how the nuclei are ordered in the peak lists.
     * As of 170823, first {@link Resonance} is either a nitrogen or carbon and second 
     * {@link Resonance} is the matching proton.
     * 
     * Note: call the methods that are overridden in subclass to get correct {@link Resonance} subclass
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
     * A method to read in a chemical shift. Note that the actual creation of the {@link Resonance}
     * is delegated to subclass to make concrete {@link AmideNitrogen} or {@link MethylCarbon} 
     * {@link Resonance}.
     * 
     * @param scanner instance to read chemical shift data from peak list
     * 
     * @see Resonance
     * @see AmideNitrogen
     * @see MethylCarbon

     * @return a concrete subclass of {@link Resonance} based on overridden method call
     */
    public abstract Resonance getFirstResonanceSubclass(Scanner scanner);
    
    /**
     * An abstract method to read in a chemical shift. Note that the actual creation of the {@link Resonance}
     * is delegated to subclass to make concrete {@link AmideProton} or {@link MethylProton} 
     * {@link Resonance} objects.
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
    
    /**
     * An abstract method to create a concrete subclass of {@link TitrationPoint}. Note how creation of the 
     * concrete subclasses is done with overridden methods in subclasses of {@link AbsFactory}
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
     * Closes a {@link Scanner} object
     * 
     * @param scanners the object to close
     */
    private void closeFiles(List<Scanner> scanners) {
        scanners.stream()
                .forEach(Scanner::close);
    }
}

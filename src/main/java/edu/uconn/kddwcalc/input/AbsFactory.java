package edu.uconn.kddwcalc.input;

import edu.uconn.kddwcalc.data.Resonance;
import edu.uconn.kddwcalc.data.Titration;
import edu.uconn.kddwcalc.data.TitrationPoint;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.gui.RawData;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract superclass in an abstract factory pattern hierarchy. In the template method 
 * {@link #sortDataFiles sortDataFiles}, the unsorted data comes from the user and is sorted into 
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
     * 
     * @throws IOException if an exception occurs when opening peak list data files
     */
    public final TitrationSeries sortDataFiles(RawData dataObject) throws IOException {
        
        List<Scanner> scanners = null; 
        
        try {
            // open files by getting a List<Scanner> from the List<Path>
            scanners = makeScannersFromPaths(dataObject.getDataPaths());
            
            final List<Titration> listOfTitration = new ArrayList<>();
            
            // iterate through the files and concentrations and group them
            //    as long as there is more data in the first file.
            // each iteration of the while loop creates a full titration curve
            //    for a single residue
            
            int number = 1;
            
            while(scanners.get(0).hasNext()) {

                List<TitrationPoint> listOfPoints = new ArrayList<>();
                
                // each iteration of for loop adds a single point to the titration.
                // the control variable is the number of data files, which is the 
                //   number of individial titraiton points collected
                for(int ctr = 0; ctr < dataObject.getDataFiles().size(); ctr++) {
                    TitrationPoint point = makeTitrationPoint(scanners.get(ctr), 
                        dataObject.getLigandConcs()[ctr], 
                        dataObject.getReceptorConcs()[ctr],
                        dataObject.getResonanceReversal());

                    listOfPoints.add(point);
                }   
                
                
                listOfTitration.add(Titration.makeTitration(listOfPoints, 
                                                            dataObject.getScalingFactor(),
                                                            String.valueOf(number)));
                
                number++;
            } 
            
            return TitrationSeries.makeTitrationSeries(listOfTitration);
            
        }
        catch (IOException e) {
            throw new IOException(e);
        }
        
        finally {
            closeFiles(scanners);
        }
    }    
    
    /**
     * Takes the {@link java.nio.file.Path} objects from the user and converts them to {@link Scanner} objects
     * 
     * @param paths the location of the text files with peak lists
     * 
     * @return {@link Scanner} instances to begin reading peak list data
     */
    private List<Scanner> makeScannersFromPaths(List<Path> paths) throws IOException {
        
        // dont try to change this to use streams and method references, the
        // checked IOException is an issue
        
        final List<Scanner> scanners = new ArrayList<>();
        
        try {
            for (Path pth : paths) {
                scanners.add(new Scanner(pth));
            }
        }
        catch(IOException e) {
            throw new IOException("Exception when change List<Path> to List<Scanner> in AbsFactory", e);
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
       
        try {
            if(!resonanceReversal) {
                twoResonances[0] = getFirstResonanceSubclass(scanner);
                twoResonances[1] = getSecondResonanceSubclass(scanner);
            }
            else if (resonanceReversal) {
                twoResonances[1] = getSecondResonanceSubclass(scanner);
                twoResonances[0] = getFirstResonanceSubclass(scanner); 
            }
        }
        catch(InputMismatchException e) {
            throw new InputMismatchException("Exception in method makeTwoResonances that suggests an issue "
                + "with the peak list formats.");
        }
       return twoResonances;   
    }

    /**
     * A method to read in a chemical shift. Note that the actual creation of the {@link Resonance}
     * is delegated to subclass to make concrete {@link edu.uconn.kddwcalc.data.AmideNitrogen} or 
     * {@link edu.uconn.kddwcalc.data.MethylCarbon} 
     * 
     * @param scanner instance to read chemical shift data from peak list
     * 
     * @see Resonance
     * @see edu.uconn.kddwcalc.data.AmideNitrogen
     * @see edu.uconn.kddwcalc.data.MethylCarbon

     * @return a concrete subclass of {@link Resonance} based on overridden method call
     */
    public abstract Resonance getFirstResonanceSubclass(Scanner scanner);
    
    /**
     * An abstract method to read in a chemical shift. Note that the actual creation of the {@link Resonance}
     * is delegated to subclass to make concrete {@link edu.uconn.kddwcalc.data.AmideProton} or 
     * {@link edu.uconn.kddwcalc.data.MethylProton}
     * 
     * @param scanner instance to read chemical shift data from peak list
     * 
     * @see Resonance
     * @see edu.uconn.kddwcalc.data.AmideProton
     * @see edu.uconn.kddwcalc.data.MethylProton
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
     * @param scanners the objects to close
     */
    private void closeFiles(List<Scanner> scanners) {
        scanners.stream()
                .forEach(Scanner::close);
    }
}

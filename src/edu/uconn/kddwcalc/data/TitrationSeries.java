package edu.uconn.kddwcalc.data;

import edu.uconn.kddwcalc.fitting.Calculatable;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The {@link TitrationSeries} class holds a complete fast exchange NMR data to fit Kd, dw
 * and other parameters.
 * 
 * @author Alex R.
 * 
 * @see Titration
 * @see TitrationPoint
 * @see edu.uconn.kddwcalc.analyze.AbsFactory
 * @see edu.uconn.kddwcalc.fitting.LeastSquaresFitter
 * 
 * @since 1.8
 */
public class TitrationSeries implements Calculatable {   
    
    private final List<Titration> listOfTitrations;
    
    private static final String IDENTIFIER = "CumulativeFit";
    private static final String SORT_PEAKLIST_FILE_NAME =
        "sortedPeakLists.txt";
    private static final String IDENTIFIER_POINT_LIST = 
        "identifierNumberList.txt";
    
    
    private TitrationSeries(List<Titration> titrationSeries) {
        
        this.listOfTitrations = titrationSeries;
    }
    
    /**
     * Prints a text 
     * 
     * @param resultsOverallDirectory where the sorted peak lists will be written 
     * 
     * @throws FileNotFoundException if problems occur when opening sortedData.txt
     */
    public void printTitrationSeries(Path resultsOverallDirectory) 
            throws FileNotFoundException  {
        
        Path path = Paths.get(resultsOverallDirectory.toAbsolutePath().toString(),
                              SORT_PEAKLIST_FILE_NAME);
        
        try (Formatter output = new Formatter(path.toFile())) {
            listOfTitrations.stream()
                            .forEach(titr -> {
                                titr.printTitration(output);
                               output.format("%n");
                            });
        }
        catch(FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening sortedData.txt");
        }
        
        Path identifierPath = Paths.get(resultsOverallDirectory.toAbsolutePath().toString(),
            IDENTIFIER_POINT_LIST);
        
        try (Formatter output = new Formatter(identifierPath.toFile())) {
            
            output.format("Here are the numbers that identify each residue and the "
                + "corresponding point in the spectrum for free receptor. This "
                + "can be used to match the numbers with their assigned residues%n%n");
            
            listOfTitrations.stream()
                            .forEach(titr -> {
                                double[] freePoint = titr.getFreeReceptorPoint();
                                output.format("%4s:  %8.3f%8.3f%n", 
                                    titr.getIdentifier(),
                                    freePoint[0],
                                    freePoint[1]);
                            });
        }
        catch(FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening identiferNumberList.txt");
        }
        
    }

    /**
     * Calculates and returns an array with the cumulative chemical shifts. This array must be the 
     * same length as the arrays with concentrations of receptor and ligand
     * 
     * @return array containing the cumulative chemical shifts 
     */
    @Override
    public double[] getObservables() {

        final double[] aggCSPs = 
                new double[listOfTitrations.get(0).getObservables().length];
        
        Arrays.fill(aggCSPs, 0.0);
        
        // mutates a nonlocal variable (aggCSPs) 
        listOfTitrations.stream()  // now have Stream<Titration>
                        .map(Titration::getObservables) // now have Stream<double[]>
                        .forEach(cspsOneResi -> {
                            for(int ctr = 0; ctr < cspsOneResi.length; ctr++) {
                                aggCSPs[ctr] = cspsOneResi[ctr] + aggCSPs[ctr];
                            }

                        });    
        
        return aggCSPs;
        
    } // end method getObservables()
    
    /**
     * Gets the array of receptor concentrations
     * 
     * @return the receptor concentrations
     */
    @Override
    public double[] getReceptorConcs() {
        return listOfTitrations.get(0).getReceptorConcs();
    }
    
    /**
     * Gets the array of ligand concentrations
     * 
     * @return the array of ligand concentrations
     */
    @Override
    public double[] getLigandConcs() {
        return listOfTitrations.get(0).getLigandConcs();
                
    }
    
    /**
     * Gets the instance variable
     *
     * @return the instance variable
     */    
    public List<Titration> getListOfTitrations() {
        return listOfTitrations;
    } 
    
    /**
     * A simple factory to create an object of class <code>TitrationSeries</code>.
     * 
     * @param titrationSeries the List<Titration> that was passed int
     * 
     * @return an initialized object of class <code>TitrationSeries</code>.
     */
    public static TitrationSeries makeTitrationSeries(List<Titration> titrationSeries) {
        
        if(titrationSeries.stream().anyMatch(titr -> titr == null))
            throw new IllegalArgumentException("was a null titration in makeTitrationSeries");
        
        return new TitrationSeries(titrationSeries);
    }
    
    /**
     * Gets a unique identifier for this dataset
     * 
     * @return a {@link String} object with the identifier 
     */
    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }  
} // end class TitrationSeries

package edu.uconn.kddwcalc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
    
    private TitrationSeries(List<Titration> titrationSeries) {
        
        this.listOfTitrations = titrationSeries;
    }
    
    /**
     * Prints a text 
     * 
     * @param file where the sorted peak lists will be written 
     * 
     * @throws FileNotFoundException if problems occur when opening sortedData.txt
     */
    public void printTitrationSeries(File file) throws FileNotFoundException {
        try (Formatter output = new Formatter(file)) {
            listOfTitrations.stream()
                       .forEach(titr -> {
                           titr.printTitration(output);
                           output.format("%n");
                       });
        }
        catch(FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening sortedData.txt");
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

        List<List<Double>> aggCSPintermediate = 
            listOfTitrations.stream()  // now have Stream<Titration>
                           .map(Titration::getCSPsFrom2DPoints) // now have Stream<List<Double>>
                           .collect(Collectors.toList());        
        
        List<Double> aggCSPs = new ArrayList<>();
        
        // intialializes with values of 0.0 and sets the lenght of cumulative CSPs
        for(int ctr = 0; ctr < listOfTitrations.get(0).getCSPsFrom2DPoints().size(); ctr++) {
            aggCSPs.add(0.0);
        }
        
        for(List<Double> cspsForOneResidue : aggCSPintermediate) {
            for(int ctr = 0; ctr < cspsForOneResidue.size(); ctr++) {
                Double temp = Double.sum(cspsForOneResidue.get(ctr), aggCSPs.get(ctr));
                
                aggCSPs.set(ctr, temp);
            }
        }
        
        
        // target array in least squaures solver must be double[] not List<Double>
        double[] aggShiftsArray = new double[aggCSPs.size()];
        
        for(int ctr = 0; ctr < aggShiftsArray.length; ctr++) {
            aggShiftsArray[ctr] = aggCSPs.get(ctr);
        }
        
        return aggShiftsArray;   
        
    } // end method GetCumulativeShifts()
    
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

    public static TitrationSeries makeTitrationSeries(List<Titration> titrationSeries) {
        return new TitrationSeries(titrationSeries);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
    
    
} // end class TitrationSeries

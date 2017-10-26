package edu.uconn.kddwcalc.fitting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * A class that holds the final results of fitting fast exchange NMR titration data. This class also contains
 * a method to write the data to disk.
 * 
 * @author Alex R.
 * 
 * @see edu.uconn.kddwcalc.fitting.LeastSquaresFitter
 * 
 * @since 1.8
 */
public class Results {
    double kd;
    double percentBound;
    double[] boundCSPArray;
    double[][] presentationFit;
    
    /**
     * Initializes all the instance variables after being called from a simple static factory method.
     * 
     * @param kd the binding affinity for the receptor and ligand
     * @param percentBound the percent bound at the highest ligand:receptor ratio (i.e. the final titration point)
     * @param boundCSPArray contains per-residue dw between free and bound with the kd fixed to the value
     * from the cumulative fitting in {@link AggResults}
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * from {@link AggResults}
     */
    private Results(double kd, 
                    double percentBound, 
                    double[] boundCSPArray,
                    double[][] presentationFit) {
        this.kd = kd;
        this.percentBound = percentBound;
        this.boundCSPArray = boundCSPArray;
        this.presentationFit = presentationFit;
    }
    
    /**
     * A simple static factory to create a {@link Results} object with validation
     * 
     * @param kd the binding affinity for the receptor and ligand
     * @param percentBound the percent bound at the highest ligand:receptor ratio (i.e. the final titration point)
     * @param boundCSPArray contains per-residue dw between free and bound with the kd fixed to the value
     * from the cumulative fitting
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * 
     * @return a {@link Results} instance with all instance variables initialized
     */
    public static Results makeResultsObject(double kd, 
                                            double percentBound, 
                                            double[] boundCSPArray,
                                            double[][] presentationFit) {
        if (kd < 0.0 || percentBound < 0.0)
            throw new IllegalArgumentException("negative kd or %bound in Results");
        
        Arrays.stream(boundCSPArray)
              .forEach(csp -> { 
                  if (csp < 0.0) 
                     throw new IllegalArgumentException("negative csp in Results"); 
              });
        
        return new Results(kd, percentBound, boundCSPArray, presentationFit);
    }
    
    /**
     * Gets the affinity constant
     * 
     * @return the affinity constant as a <code>double</code>
     * 
     * @see AggResults
     * @see LeastSquaresFitter#fit
     * @see LeastSquaresFitter#fitCumulativeData 
     */
    private double getKd() {
        return kd;
    }
    
    /**
     * Gets the percent bound at the highest ligand ratio (last titration point)
     * 
     * @return the percent bound (0-1) 
     * 
     * @see AggResults
     * @see LeastSquaresFitter#fit
     * @see LeastSquaresFitter#fitCumulativeData 
     */
    private double getPercentBound() {
        return percentBound;
    }
    
    /**
     * Gets the per-residue chemical shift perturbations (dw) using the Kd from {@link AggResults}
     * 
     * @return an array containing the per-residue chemical shift perturbations (dw)
     * 
     * @see LeastSquaresFitter#fit
     * @see LeastSquaresFitter#fitDwForAResidue
     */
    private double[] getBoundCSPArray() {
        return boundCSPArray;
    }
    
    /**
     * A method to write a text file with the results to disk.
     * 
     * @param file where the results will be written
     * 
     * @throws FileNotFoundException if can't find the place to write finalResults.txt
     */
    public void writeResultsToDisk(File file) throws FileNotFoundException {

        try (Formatter output = new Formatter(file)) {
            
            writeResults(output);
        }
        catch(FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening the file to write results");
        }  
    }
    
    /**
     * Overloaded version for testing with {@link edu.uconn.kddwcalc.data.ResonanceTest}
     * 
     * @throws FileNotFoundException if issue arises when writing final results
     */
    public void writeResultsToDisk() throws FileNotFoundException {

        try (Formatter output = new Formatter("finalResults.txt")) {
            
            writeResults(output);
        }
        catch(FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening the file to write results");
        }  
    }
    
    /**
     * A method to write the text file with the results to disk
     * 
     * @param output the {@link java.util.Formatter Formatter} used to write the results to disk
     */
    private void writeResults(Formatter output) {
        output.format("kd = %.2f%n", getKd());
        output.format("percent bound = %.2f%n", getPercentBound());
            
        output.format("%ndw for fully bound%n");
            
        Arrays.stream(getBoundCSPArray())
              .forEach(csp -> output.format("%.6f%n", csp));
            
        output.format("%n%17s%17s%17s%n","ligand ratio", "model point", "exp point");
            
        for(int ctr = 0; ctr < presentationFit.length; ctr++) {
            for(int ctr2 = 0; ctr2 < presentationFit[ctr].length; ctr2++) {
                output.format("%17f", presentationFit[ctr][ctr2]);
            }
            output.format("%n");
        }  
    } // end writeResults
} // end class Results

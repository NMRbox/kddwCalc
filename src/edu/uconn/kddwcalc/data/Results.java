package edu.uconn.kddwcalc.data;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.NoSuchElementException;

/**
 * A class that holds the final results of fitting fast exchange NMR titration data. This class also contains
 * a method to write the data to disk.
 * 
 * @author Alex R.
 * 
 * @see AggResults
 * @see LeastSquaresFitter
 * @see LeastSquaresFitter#fit 
 * @see LeastSquaresFitter#fitCumulativeData 
 * @see LeastSquaresFitter#fitDwForAResidue 
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
     * from the cumulative fitting in <code>AggResults</code>
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * from <code>AggResults</code>
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
     * A simple static factory to create a <code>Results</code> object with validation
     * 
     * @param kd the binding affinity for the receptor and ligand
     * @param percentBound the percent bound at the highest ligand:receptor ratio (i.e. the final titration point)
     * @param boundCSPArray contains per-residue dw between free and bound with the kd fixed to the value
     * from the cumulative fitting in <code>AggResults</code>
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * from <code>AggResults</code>
     * 
     * @return a <code>Results</code> instance with all instance variables initialized
     */
    public static Results makeResultsObject(double kd, 
                                            double percentBound, 
                                            double[] boundCSPArray,
                                            double[][] presentationFit) {
        if (kd < 0.0 || percentBound < 0.0)
            throw new IllegalArgumentException("negative kd or %bound in Results");
        
        for(double csp : boundCSPArray)
        {
            if (csp < 0.0)
                throw new IllegalArgumentException("negative csp in Results");
        }
        
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
    public double getKd() {
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
    public double getPercentBound() {
        return percentBound;
    }
    
    /**
     * Gets the per-residue chemical shift perturbations (dw) using the Kd from <code>AggResults</code>
     * 
     * @return an array containing the per-residue chemical shift perturbations (dw)
     * 
     * @see LeastSquaresFitter#fit
     * @see LeastSquaresFitter#fitDwForAResidue
     */
    public double[] getBoundCSPArray() {
        return boundCSPArray;
    }
    
    /**
     * A method to write a text file with the results to disk.
     */
    public void writeResultsToDisk() {
        Formatter output = openFile();
        
        writeResults(output);
        
        closeFile(output);
    }
    
    /**
     * A method to open a file (ie create a new <code>Formatter</code>
     * 
     * @return a <code>Formatter</code> instance where the result will be written to disk
     */
    private Formatter openFile() {
        Formatter output = null;
        
        try {
            return new Formatter("finalResults.txt");
        }
        catch(SecurityException | FileNotFoundException e) {
            System.err.println(e);
        }
        
        if (output == null)
            throw new IllegalArgumentException("output Formatter is null before returning");
                
        return output; 
    }
    
    /**
     * A method to write the text file with the results to disk
     * 
     * @param output the <code>Formatter</code> used to write the results to disk
     */
    private void writeResults(Formatter output) {
        try {
            output.format("kd = %.2f%n", getKd());
            output.format("percent bound = %.2f%n", getPercentBound());
            
            output.format("%ndw for fully bound%n");
            
            Arrays.stream(getBoundCSPArray())
                  .forEach(csp -> output.format("%.6f%n", csp));
            
            output.format("%nModel points [ligand radio, percent bound]%n");
            
            for(int ctr = 0; ctr < presentationFit.length; ctr++)
            {
                for(int ctr2 = 0; ctr2 < presentationFit[ctr].length; ctr2++)
                {
                    output.format("%f\t", presentationFit[ctr][ctr2]);
                }
                output.format("%n");
            }
            
        }
        catch(FormatterClosedException | NoSuchElementException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Closes an open <code>Formatter</code>
     * 
     * @param output the <code>Formatter</code> to close
     */
    private void closeFile(Formatter output) {
        if (output != null)
            output.close();
    }
}

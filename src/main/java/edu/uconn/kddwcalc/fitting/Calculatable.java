/**
 * Interface for objects that can be sent to 
 * {@link edu.uconn.kddwcalc.fitting.LeastSquaresFitter#fit} for
 * analysis.
 * 
 */
package edu.uconn.kddwcalc.fitting;

import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 *
 * @author Alex R.
 */
public interface Calculatable {
    
    /**
     * 
     * @return array containing the ligand concentrations
     */
    public double[] getLigandConcs();
    
    /**
     * 
     * @return array containing the receptor concentrations
     */
    public double[] getReceptorConcs();
    
    /**
     * 
     * 
     * @return array containing the observables (experimental values)
     */
    public double[] getObservables();
    
    /**
     * 
     * @return an identifier for this result (e.g. residue number)
     * 
     */
    public String getIdentifier();
    
    
}

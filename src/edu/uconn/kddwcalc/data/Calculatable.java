/**
 * Interface for objects that can be sent to 
 * {@link edu.uconn.kddwcalc.fitting.LeastSquaresFitter#fit} for
 * analysis.
 * 
 */
package edu.uconn.kddwcalc.data;

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
    
}

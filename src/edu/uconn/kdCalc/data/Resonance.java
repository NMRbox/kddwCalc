package edu.uconn.kdCalc.data;

/**
 * An abstract class that holds an NMR chemical shift as a <code>double</code>. 
 * 
 * @author Alex R.
 * 
 * @see AbsFactory
 * @see TitrationPoint
 * 
 * @since 1.8
 */
public abstract class Resonance {
    private final double chemShift;
    
    /**
     * Initializes an instance of <code>Resonance</code> with an NMR chemical shift value
     * 
     * @param chemShift an NMR chemical shift value
     */
    protected Resonance(double chemShift) {
        this.chemShift = chemShift;
    }
    
    /**
     * Gets the chemical shift
     * 
     * @return the NMR chemical shift
     */
    public double getChemShift() {
        return chemShift;
    }
    
    /**
     * A static method that tests whether an NMR chemical shift is within the expected range
     * based on the type of nucleus. The bounds are generous and are primarily intended to catch
     * times where incorrect nuclei have been specified
     * 
     * @param chemShift the NMR chemical shift
     * @param max the maximum possible of <code>chemShift</code> value allowed
     * @param min the minimum possible of <code>chemShift</code> value allowed
     * 
     * @return whether of not the chemical shift is within the range specified in subclass 
     */
    public static boolean isWithinLegitRange(double chemShift, int max, int min) {
       return (chemShift < max && chemShift > min);
    }
}

package edu.uconn.kdCalc.data;

/**
 * A subclass of <code>Resonance</code> for a methyl proton nucleus. This class also performs validation 
 * of the chemical shift to ensure it is within an expected range using a simple static factory.
 * 
 * @author Alex R.
 * 
 * @see AbsFactory
 * @see MethylCarbonProtonFactory
 * @see MethylCarbonProtonTitrationPoint
 * @see MethylCarbon
 * 
 * @since 1.8
 */
public class MethylProton extends Resonance {
    private static final int METHYL_PROTON_MAX_SHIFT = 3;
    private static final int METHYL_PROTON_MIN_SHIFT = -3;

    /**
     * Initializes an instance of the class with an NMR chemical shift value
     * 
     * @param chemShift an NMR chemical shift for a methyl proton nucleus
     */
    private MethylProton(double chemShift) {
        super(chemShift);
    }

    /**
     * A static simple factory to validate and create a instance of class <code>MethylProton</code>.
     * 
     * @param chemShift an NMR chemical shift for a methyl proton nucleus
     * 
     * @return an instance of <code>MethylProton</code> that has been initialized with a chemical shift
     */
    public static MethylProton validateAndCreate(double chemShift) {    
        if (isWithinLegitRange(chemShift, METHYL_PROTON_MAX_SHIFT, METHYL_PROTON_MIN_SHIFT))
            return new MethylProton(chemShift);
        
        else throw new IllegalArgumentException();
    } 
}

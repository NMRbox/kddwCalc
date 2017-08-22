package edu.uconn.kddwcalc.data;

/**
 * A subclass of <code>Resonance</code> for a methyl carbon nucleus. This class also performs validation 
 * of the chemical shift to ensure it is within an expected range using a simple static factory.
 * 
 * @author Alex R.
 * 
 * @see AbsFactory
 * @see MethylCarbonProtonFactory
 * @see MethylCarbonProtonTitrationPoint
 * @see MethylProton
 * 
 * @since 1.8
 */
public class MethylCarbon extends Resonance {
    private static final int METHYL_CARBON_MAX_SHIFT = 50;
    private static final int METHYL_CARBON_MIN_SHIFT = -20;

    /**
     * Initializes an instance of the class with an NMR chemical shift value
     * 
     * @param chemShift an NMR chemical shift for a methyl carbon nucleus
     */
    private MethylCarbon(double chemShift) {
        super(chemShift);
    }

    /**
     * A static simple factory to validate and create a instance of class <code>MethylCarbon</code>.
     * 
     * @param chemShift an NMR chemical shift for a methyl carbon nucleus
     * 
     * @return an instance of <code>MethylCarbon</code> that has been initialized with a chemical shift
     */
    public static MethylCarbon validateAndCreate(double chemShift) {
        if (isWithinLegitRange(chemShift, METHYL_CARBON_MAX_SHIFT, METHYL_CARBON_MIN_SHIFT))
            return new MethylCarbon(chemShift);
        
        else throw new IllegalArgumentException();
    }
}  // end class MethylCarbon

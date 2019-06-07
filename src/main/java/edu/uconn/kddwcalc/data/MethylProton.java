package edu.uconn.kddwcalc.data;

/**
 * A subclass of {@link Resonance} for a methyl proton nucleus. This class also performs validation 
 * of the chemical shift to ensure it is within an expected range using a simple static factory.
 * 
 * @author Alex R.
 * 
 * @see edu.uconn.kddwcalc.analyze.AbsFactory
 * @see edu.uconn.kddwcalc.analyze.MethylCarbonProtonFactory
 * @see MethylCarbonProtonTitrationPoint
 * @see MethylCarbon
 * 
 * @since 1.8
 */
public class MethylProton extends Resonance {
    private static final int METHYL_PROTON_MAX_SHIFT = 4;
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
     * A static simple factory to validate and create a instance of class {@link MethylProton}.
     * 
     * @param chemShift an NMR chemical shift for a methyl proton nucleus
     * 
     * @return an instance of {@link MethylProton} that has been initialized with a chemical shift
     */
    public static MethylProton validateAndCreate(double chemShift) {    
        if (isWithinLegitRange(chemShift, METHYL_PROTON_MAX_SHIFT, METHYL_PROTON_MIN_SHIFT))
            return new MethylProton(chemShift);
        
        else throw new IllegalArgumentException("Chemical shifts out of range in "
            + "MethylProton.validateAndCreate. Consider switching titration type or order of nuclei.");
    } 
}

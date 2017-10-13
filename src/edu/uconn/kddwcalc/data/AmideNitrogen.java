package edu.uconn.kddwcalc.data;

/**
 * A subclass of {@link Resonance} for an amide nitrogen nucleus. This class also performs validation 
 * of the chemical shift to ensure it is within an expected range using a simple static factory.
 * 
 * @author Alex R
 * 
 * @see edu.uconn.kddwcalc.sorting.AbsFactory
 * @see edu.uconn.kddwcalc.sorting.AmideNitrogenProtonFactory
 * @see AmideNitrogenProtonTitrationPoint
 * @see AmideProton
 * 
 * @since 1.8
 */
public class AmideNitrogen extends Resonance {
    private static final int AMIDE_NITROGEN_MAX_SHIFT = 150;
    private static final int AMIDE_NITROGEN_MIN_SHIFT = 80;

    /**
     * Initializes an instance of the class with an NMR chemical shift value
     * 
     * @param chemShift an NMR chemical shift for an amide nitrogen nucleus
     */
    private AmideNitrogen(double chemShift) {
        super(chemShift);
    }

    /**
     * A static simple factory to validate and create a instance of class <code>AmideNitrogen</code>.
     * 
     * @param chemShift an NMR chemical shift for an amide nitrogen nucleus
     * 
     * @return an instance of {@link AmideNitrogen} that has been initialized with a chemical shift
     */
    public static AmideNitrogen validateAndCreate(double chemShift) {
        if (isWithinLegitRange(chemShift, AMIDE_NITROGEN_MAX_SHIFT, AMIDE_NITROGEN_MIN_SHIFT))
            return new AmideNitrogen(chemShift);
        
        else throw new IllegalArgumentException("Chemical shifts out of range in "
            + "AmideNitrogen.validateAndCreate. Consider switching titration type or order of nuclei.");
    }
}

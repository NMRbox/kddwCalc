// created by Alex Rizzo on 170625

package edu.uconn.kdCalcdata;

public class AmideNitrogen extends Resonance
{
    private static final int AMIDE_NITROGEN_MAX_SHIFT = 150;
    private static final int AMIDE_NITROGEN_MIN_SHIFT = 80;

    // one-argument constructor. 
    // validation has already been performed in validateAndCreate method
    private AmideNitrogen(double chemShift) 
    {
        super(chemShift);
    }

    public static AmideNitrogen validateAndCreate(double chemShift)
    {
        if (isWithinLegitRange(chemShift, AMIDE_NITROGEN_MAX_SHIFT, AMIDE_NITROGEN_MIN_SHIFT))
            return new AmideNitrogen(chemShift);
        
        else throw new IllegalArgumentException("The chemical shift values in the data file are outside of the expected range "
            + "for the nuclei specified in the GUI. The bounds are generous, so most likely the "
            + "wrong nuclei were selected or they were reversed compared to the data file.");
    }
}

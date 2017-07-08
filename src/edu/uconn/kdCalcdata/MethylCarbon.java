// created by Alex Rizzo on 170625

package edu.uconn.kdCalcdata;

public class MethylCarbon extends Resonance
{
    private static final int METHYL_CARBON_MAX_SHIFT = 50;
    private static final int METHYL_CARBON_MIN_SHIFT = -20;

    // one-argument constructor. 
    // validation has already been performed in validateAndCreate method
    private MethylCarbon(double chemShift) 
    {
        super(chemShift);
    }

    public static MethylCarbon validateAndCreate(double chemShift)
    {
        if (isWithinLegitRange(chemShift, METHYL_CARBON_MAX_SHIFT, METHYL_CARBON_MIN_SHIFT))
            return new MethylCarbon(chemShift);
        
        else throw new IllegalArgumentException();
    }
}  // end class MethylCarbon

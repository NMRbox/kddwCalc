// created by Alex Rizzo on 170625

package edu.uconn.kdCalcdata;

public class MethylProton extends Resonance
{
    private static final int METHYL_PROTON_MAX_SHIFT = 3;
    private static final int METHYL_PROTON_MIN_SHIFT = -3;

    // one-argument constructor. 
    // validation has already been performed in validateAndCreate method
    private MethylProton(double chemShift) 
    {
        super(chemShift);
    }

    public static MethylProton validateAndCreate(double chemShift)
    {    
        if (isWithinLegitRange(chemShift, METHYL_PROTON_MAX_SHIFT, METHYL_PROTON_MIN_SHIFT))
            return new MethylProton(chemShift);
        
        else throw new ResonanceOutOfRangeException();
    } 
}

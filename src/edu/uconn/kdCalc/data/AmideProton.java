// created by Alex Rizzo on 170625

package edu.uconn.kdCalc.data;

public class AmideProton extends Resonance
{
    private static final int AMIDE_PROTON_MAX_SHIFT = 17;
    private static final int AMIDE_PROTON_MIN_SHIFT = 4;

    // one-argument constructor. 
    // validation has already been performed in validateAndCreate method
    private AmideProton(double chemShift) 
    {
        super(chemShift);
    }

    public static AmideProton validateAndCreate(double chemShift)
    {
        if (isWithinLegitRange(chemShift, AMIDE_PROTON_MAX_SHIFT, AMIDE_PROTON_MIN_SHIFT))
            return new AmideProton(chemShift);
        
        else throw new IllegalArgumentException();
    }  
}

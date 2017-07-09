// created by Alex Rizzo on 170625

package edu.uconn.kdCalc.data;


public abstract class Resonance 
{
    private final double chemShift;
    
    // one-argument constructor
    public Resonance(double chemShift)
    {
        this.chemShift = chemShift;
    }
    
    // GETTER
    public double getResonance()
    {
        return chemShift;
    }
    
    // tests whether the chemicalShift is within range
    // this eliminates duplication of this one line of code in each
    // subclass validateAndCreate method
    public static boolean isWithinLegitRange(double chemShift, int max, int min)
    {
       return (chemShift < max && chemShift > min);
    }
}

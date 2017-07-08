// created by AR on 170626

package edu.uconn.kdCalcdata;



public abstract class TitrationPoint
{   
    // concentrations of the two proteins.
    // the receptor is the labeled species.
    // units are micromol (uM)
    private final double ligandConc;
    private final double receptorConc;
    
    private final Resonance resonance1;
    private final Resonance resonance2;
    
    public TitrationPoint(double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
        this.ligandConc = ligandConc;
        this.receptorConc = receptorConc;
        
        this.resonance1 = resonance1;
        this.resonance2 = resonance2;
    }
    
    // GETTERS
    public double getLigandConc()
    {
        return ligandConc;
    }
    
    public double getReceptorConc()
    {
        return receptorConc;
    }
    
    public Resonance getResonance1()
    {
        return resonance1;
    }
    
    public Resonance getResonance2()
    {
        return resonance2;
    }
    
    public static boolean isValidConc (double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
        if (ligandConc < 0 || receptorConc < 0)
            throw new IllegalArgumentException("Concentrations are less than zero, cant be");
        
        if (resonance1 == null || resonance2 == null)
            throw new NullPointerException("During instantitaion of a TitrationPoint, the resoonances had"
                + "a null reference (resonances need to be instantiated before TitrationPoint)");
        
        return true;    
    }
    
    
}

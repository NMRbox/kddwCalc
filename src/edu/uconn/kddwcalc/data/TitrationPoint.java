package edu.uconn.kddwcalc.data;

/**
 * A class holding the data fom a single residue at a single concentration.
 * 
 * Units of receptor and ligand concentration are uM (micromolar in all cases)
 * 
 * @author Rizzo
 * 
 * @see Resonance
 * @see Titration
 * @see TitrationSeries
 */
public abstract class TitrationPoint {   
    private final double ligandConc;  
    private final double receptorConc;   // the labeled protein
    
    private final Resonance resonance1;
    private final Resonance resonance2;
    
    
    public TitrationPoint(double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2) {
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
    
    public static boolean isValidData (double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
        if (ligandConc < 0 || receptorConc < 0)
            throw new IllegalArgumentException("Concentrations are less than zero, cant be");
        
        if (resonance1 == null || resonance2 == null)
            throw new NullPointerException("During instantitaion of a TitrationPoint, the resoonances had"
                + "a null reference (resonances need to be instantiated before TitrationPoint)");
        
        return true;    
    }
    
    @Override 
    public String toString()
    {
        return String.format("%10.3f %16.3f %13.5f %15.5f",
            ligandConc, receptorConc, resonance1.getChemShift(), resonance2.getChemShift());
    }
    
}

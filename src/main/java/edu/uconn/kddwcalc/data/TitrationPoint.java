package edu.uconn.kddwcalc.data;

/**
 * A class representing the data from a single residue at a single concentration.
 * 
 * Units of receptor and ligand concentration are uM (micromolar in all cases)
 * 
 * @author Alex R.
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
    
    /**
     * Initializes four instance variables; should only be called after extensive validation.
     * 
     * @param ligandConc the total ligand concentration
     * @param receptorConc the total receptor concentration
     * @param resonance1 the first {@link Resonance}
     * @param resonance2 the second {@link Resonance}
     */
    public TitrationPoint(double ligandConc, 
                          double receptorConc,
                          Resonance resonance1, 
                          Resonance resonance2) {
        
        this.ligandConc = ligandConc;
        this.receptorConc = receptorConc;
        
        this.resonance1 = resonance1;
        this.resonance2 = resonance2;
    }
    
    /**
     * Gets the ligand concentration
     * 
     * @return the ligand concentration
     */
    public double getLigandConc() {
        return ligandConc;
    }
    
    /**
     * Gets the receptor concentration
     * 
     * @return the receptor concentration 
     */
    public double getReceptorConc() {
        return receptorConc;
    }
    
    /**
     * Gets the first resonance. As of 170824 (AR) this is amide nitrogen or methyl carbon
     * 
     * @return the first resonance object
     */
    public Resonance getResonance1() {
        return resonance1;
    }
    
    /**
     * Gets the second resonance. As of 170824 (AR) this is amide proton or methyl proton
     * 
     * @return the second resonance object 
     */
    public Resonance getResonance2() {
        return resonance2;
    }
    
    /**
     * Performs certain validations of the data
     * 
     * @param ligandConc the total ligand concentration
     * @param receptorConc the total receptor concentration
     * @param resonance1 the first {@link Resonance}
     * @param resonance2 the second {@link Resonance}
     * 
     * @return <code>true</code> if validation is passed, <code>false</code> if values don't pass 
     */
    public static boolean isValidData (double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2) {
        
        if (ligandConc < 0 || receptorConc < 0)
            throw new IllegalArgumentException("Concentrations are less than zero, cant be");
        
        if (resonance1 == null || resonance2 == null)
            throw new NullPointerException("During instantitaion of a TitrationPoint, the resonances had"
                + "a null reference (resonances need to be instantiated before TitrationPoint)");
        
        return true;    
    }
    
    /**
     * Gets a string with the data for this {@link TitrationPoint}
     * 
     * @return a {@link String} with the ligand and receptor concentration and the two chemical shifts 
     */
    @Override 
    public String toString() {
        return String.format("%10.3f %16.3f %13.5f %15.5f",
            ligandConc, receptorConc, resonance1.getChemShift(), resonance2.getChemShift());
    }  
}

package edu.uconn.kddwcalc.data;

/**
 * A class representing an experiment titration point in a 1H-15N HSQC. This point contains the data
 * for a single residue at a single concentration.
 * 
 * Note that <code>resonance1</code> must be the nitrogen dimension and <code>resonance2</code> must
 * be the proton dimension
 * 
 * @author Alex R.
 * 
 * @see Titration
 * @see TitrationSeries
 * @see Resonance
 * 
 * @since 1.8
 */
public class AmideNitrogenProtonTitrationPoint extends TitrationPoint {
    /**
     * Initializes an instance of the class by calling the four-argument constructor from 
     * superclass {@link TitrationPoint}
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param resonance1 the chemical shift from the nitrogen dimension
     * @param resonance2 the chemical shift from the proton dimensions
     */
    private AmideNitrogenProtonTitrationPoint(double ligandConc, 
                                              double receptorConc,
                                              Resonance resonance1, 
                                              Resonance resonance2) {
        super(ligandConc, receptorConc, resonance1, resonance2);
    }
    
    /**
     * A static simple factory that performs validation of the parameters before calling the private
     * constructor
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param resonance1 the chemical shift from the nitrogen dimension
     * @param resonance2 the chemical shift from the proton dimensions
     * 
     * @see TitrationPoint#isValidData 
     * 
     * @return an instance of class {@link AmideNitrogenProtonTitrationPoint} with all instance
     * variables initialized
     * 
     * @throws IllegalArgumentException if <code>resonance1</code> is not nitrogen 
     * or <code>resonance2</code> is not proton
     * 
     * @throws IllegalArgumentException if {@link TitrationPoint#isValidData} returns false
     */
    public static AmideNitrogenProtonTitrationPoint validateAndCreate(double ligandConc, 
                                                                      double receptorConc,
                                                                      Resonance resonance1, 
                                                                      Resonance resonance2) {
        
        // if its not a amide nitrogen, throw exception
        if(!(resonance1 instanceof AmideNitrogen))
            throw new IllegalArgumentException("when instantiating a AmideNitrogenTitration point, resonance 1"
                + " was not a AmideNitrogen");
        // if its not a amide proton like its supposed to be, throw exception
        if (!(resonance2 instanceof AmideProton))
            throw new IllegalArgumentException("when instantiating a AmideNitrogenTitration point, resonance 2"
                + " was not a AmideProton");
        
       
        
        // after testing the validity (positive values for concentration, and referecnes arent null for resonances)
        //     if data is valid, fall through to return
        if (!(TitrationPoint.isValidData(ligandConc, receptorConc, resonance1, resonance2)))
            throw new IllegalArgumentException("when instantiating a AmideNitrogenTitration point, the isValid method"
                + " from TitrationPoint returned false somehow");
        
        return new AmideNitrogenProtonTitrationPoint(ligandConc, receptorConc, resonance1, resonance2);   
    }
    
    
        
}

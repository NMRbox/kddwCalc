package edu.uconn.kddwcalc.data;

/**
 * A class representing an experiment titration point in a 1H-13C Methyl HMQC. This point contains the data
 * for a single residue at a single concentration.
 * 
 * Note that <code>resonance1</code> must be the proton dimension and <code>resonance2</code> must
 * be the carbon dimension
 * 
 * @author Alex R.
 * 
 * @see Titration
 * @see TitrationSeries
 * @see Resonance
 * 
 * @since 1.8
 */
public class MethylCarbonProtonTitrationPoint extends TitrationPoint {
    
    /**
     * Initializes an instance of the class by calling the four-argument constructor from 
     * superclass {@link TitrationPoint}
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param resonance1 the chemical shift from the proton dimension
     * @param resonance2 the chemical shift from the carbon dimension
     */
    private MethylCarbonProtonTitrationPoint(double ligandConc, 
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
     * @return an instance of class {@link MethylCarbonProtonTitrationPoint} with all instance
     * variables initialized
     * 
     * @throws IllegalArgumentException if <code>resonance1</code> is not proton 
     * or  <code>resonance2</code> is not carbon
     * 
     * @throws IllegalArgumentException if {@link TitrationPoint#isValidData} returns false
     */
    public static MethylCarbonProtonTitrationPoint validateAndCreate(double ligandConc, 
                                                                     double receptorConc,
                                                                     Resonance resonance1, 
                                                                     Resonance resonance2) {
        // if its not a methylcarbon, throw exception
        if(!(resonance1 instanceof MethylCarbon))
            throw new IllegalArgumentException("when instantiating a MethylCarbonTitration point, resonance 1"
                + " was not a MethylCarbon");
        
        // if its not a methyl proton like its supposed to be, throw exception
        if (!(resonance2 instanceof MethylProton))
            throw new IllegalArgumentException("when instantiating a MethylCarbonTitration point, resonance 2"
                + " was not a MethylProton");
        
        
        // after testing the validity (positive values for concentration, and referecnes arent null for resonances)
        //     if data is valid, fall through to return
        if (!(TitrationPoint.isValidData(ligandConc, receptorConc, resonance1, resonance2)))
            throw new IllegalArgumentException("when instantiating a MethylCarbonTitration point, the isValid method"
                + " from TitrationPoint returned false somehow");
        
        return new MethylCarbonProtonTitrationPoint(ligandConc, receptorConc, resonance1, resonance2);   
    }    
}

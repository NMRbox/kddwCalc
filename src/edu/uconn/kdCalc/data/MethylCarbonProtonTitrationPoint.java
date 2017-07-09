// created by AR on 170626
//
// resonance1 must be a MethylProton
// resonance2 must be a MethylCarbon


package edu.uconn.kdCalc.data;

public class MethylCarbonProtonTitrationPoint extends TitrationPoint
{
    // four-argument constructor
    private MethylCarbonProtonTitrationPoint(double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
        super(ligandConc, receptorConc, resonance1, resonance2);
    }
    
    public static MethylCarbonProtonTitrationPoint validateAndCreate(double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
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
        if (!(TitrationPoint.isValidConc(ligandConc, receptorConc, resonance1, resonance2)))
            throw new IllegalArgumentException("when instantiating a MethylCarbonTitration point, the isValid method"
                + " from TitrationPoint returned false somehow");
        
        return new MethylCarbonProtonTitrationPoint(ligandConc, receptorConc, resonance1, resonance2);   
    }
        
}

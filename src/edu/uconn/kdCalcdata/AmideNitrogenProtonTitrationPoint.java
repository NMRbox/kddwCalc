// created by AR on 170626
//
// resonance1 must be a AmideProton
// resonance2 must be a AmideNitrogen


package edu.uconn.kdCalcdata;

public class AmideNitrogenProtonTitrationPoint extends TitrationPoint
{
    // four-argument constructor
    private AmideNitrogenProtonTitrationPoint(double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
        super(ligandConc, receptorConc, resonance1, resonance2);
    }
    
    public static AmideNitrogenProtonTitrationPoint validateAndCreate(double ligandConc, double receptorConc,
        Resonance resonance1, Resonance resonance2)
    {
        
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
        if (!(TitrationPoint.isValidConc(ligandConc, receptorConc, resonance1, resonance2)))
            throw new IllegalArgumentException("when instantiating a AmideNitrogenTitration point, the isValid method"
                + " from TitrationPoint returned false somehow");
        
        return new AmideNitrogenProtonTitrationPoint(ligandConc, receptorConc, resonance1, resonance2);   
    }
        
}

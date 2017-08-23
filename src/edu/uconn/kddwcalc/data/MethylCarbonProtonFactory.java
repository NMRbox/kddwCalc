package edu.uconn.kddwcalc.data;

import java.util.Scanner;

/**
 * A concrete factory class used to create the read in 1H-13C methyl HMQC NMR titration data. Note that the first resonance 
 * is a <code>MethylCarbon</code> and the second resonance is a <code>MethylProton</code>.
 * 
 * @author Alex R.
 * 
 * @see Resonance
 * @see TitrationPoint
 * @see MethylCarbonProtonTitrationPoint
 * @see Titration
 * @see TitrationSeries
 * @see MethylCarbon
 * @see MethylProton
 * 
 * @since 1.8
 */
public class MethylCarbonProtonFactory extends AbsFactory {
    
    /**
     * A method to read a <code>MethylCarbon</code> chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a <code>MethylCarbon</code> subclass of <code>Resonance</code>
     */
    @Override
    public Resonance getFirstResonanceSubclass(Scanner scanner) {
        return MethylCarbon.validateAndCreate(scanner.nextDouble());
    }
    
     /**
     * A method to read a <code>MethylProton</code> chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a <code>MethylProton</code> subclass of <code>Resonance</code>
     */
    @Override
    public Resonance getSecondResonanceSubclass(Scanner scanner) {
        return MethylProton.validateAndCreate(scanner.nextDouble());
    }
    
    /**
     * A method to create a concrete <code>MethylCarbonTitrationPoint</code> subclass of
     * <code>TitrationPoint</code>
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param firstCoordinate the first <code>Resonance</code>, here is a <code>MethylCarbon</code>
     * @param secondCoordinate the first <code>Resonance</code>, here is a <code>MethylProton</code>
     * 
     * @return a fully initialized <code>MethylCarbonProtonTitrationPoint</code> concrete subclass of
     * <code>Resonance</code>
     */
    @Override
    public TitrationPoint makeTitrationPointSubclass(double ligandConc, 
                                                     double receptorConc, 
                                                     Resonance firstCoordinate, 
                                                     Resonance secondCoordinate) {
        
        return MethylCarbonProtonTitrationPoint.validateAndCreate(ligandConc, 
                                                                  receptorConc, 
                                                                  firstCoordinate, 
                                                                  secondCoordinate);
    }
}

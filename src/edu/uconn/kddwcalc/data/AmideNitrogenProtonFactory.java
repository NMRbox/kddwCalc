package edu.uconn.kddwcalc.data;

import java.util.Scanner;

/**
 * A concrete factory class used to create the read in 1H-15N HSQC NMR titration data. Note that the first resonance 
 * is a <code>AmideNitrogen</code> and the second resonance is a <code>AmideProton</code>
 * 
 * @author Alex R.
 * 
 * @see Resonance
 * @see TitrationPoint
 * @see AmideNitrogenProtonTitrationPoint
 * @see Titration
 * @see TitrationSeries
 * @see AmideProton
 * @see AmideNitrogen
 * 
 * @since 1.8
 */
public class AmideNitrogenProtonFactory extends AbsFactory {
    
    /**
     * A method to read a <code>AmideNitrogen</code> chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a <code>AmideNitrogen</code> subclass of <code>Resonance</code>
     */
    @Override
    public Resonance getFirstResonanceSubclass(Scanner scanner) {
        return AmideNitrogen.validateAndCreate(scanner.nextDouble());
    }
    
    /**
     * A method to read a <code>AmideProton</code> chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a <code>AmideProton</code> subclass of <code>Resonance</code>
     */
    @Override
    public Resonance getSecondResonanceSubclass(Scanner scanner) {
        return AmideProton.validateAndCreate(scanner.nextDouble());
    }
    
    /**
     * A method to create a concrete <code>AmideNitrogenProtonTitrationPoint</code> subclass of
     * <code>TitrationPoint</code>
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param firstCoordinate the first <code>Resonance</code>, here is a <code>AmideNitrogen</code>
     * @param secondCoordinate the first <code>Resonance</code>, here is a <code>AmideProton</code>
     * 
     * @return a fully initialized <code>AmideNitrogenProtonTitrationPoint</code> concrete subclass of
     * <code>Resonance</code>
     */
    @Override
    public TitrationPoint makeTitrationPointSubclass(double ligandConc, 
                                                     double receptorConc, 
                                                     Resonance firstCoordinate, 
                                                     Resonance secondCoordinate) {
        
        return AmideNitrogenProtonTitrationPoint.validateAndCreate(ligandConc, 
                                                                   receptorConc, 
                                                                   firstCoordinate, 
                                                                   secondCoordinate);
    }
}

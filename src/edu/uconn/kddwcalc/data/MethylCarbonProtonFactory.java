package edu.uconn.kddwcalc.data;

import java.util.Scanner;

/**
 * A concrete factory class used to create the read in 1H-13C methyl HMQC NMR titration data. Note that the first resonance 
 * is a {@link MethylCarbon} and the second resonance is a {@link MethylProton}.
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
     * A method to read a {@link MethylCarbon} chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a {@link MethylCarbon} subclass of {@link Resonance}
     */
    @Override
    public Resonance getFirstResonanceSubclass(Scanner scanner) {
        return MethylCarbon.validateAndCreate(scanner.nextDouble());
    }
    
     /**
     * A method to read a {@link MethylProton} chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a {@link MethylProton} subclass of {@link Resonance}
     */
    @Override
    public Resonance getSecondResonanceSubclass(Scanner scanner) {
        return MethylProton.validateAndCreate(scanner.nextDouble());
    }
    
    /**
     * A method to create a concrete {@link MethylCarbonProtonTitrationPoint} subclass of
     * {@link TitrationPoint}
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param firstCoordinate the first {@link Resonance}, here is a {@link MethylCarbon}
     * @param secondCoordinate the first {@link Resonance}, here is a {@link MethylProton}
     * 
     * @return a fully initialized {@link MethylCarbonProtonTitrationPoint} concrete subclass of
     * {@link Resonance}
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

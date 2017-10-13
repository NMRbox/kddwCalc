package edu.uconn.kddwcalc.sorting;

import edu.uconn.kddwcalc.data.AmideNitrogen;
import edu.uconn.kddwcalc.data.AmideNitrogenProtonTitrationPoint;
import edu.uconn.kddwcalc.data.AmideProton;
import edu.uconn.kddwcalc.data.Resonance;
import edu.uconn.kddwcalc.data.TitrationPoint;
import edu.uconn.kddwcalc.sorting.AbsFactory;
import java.util.Scanner;

/**
 * A concrete factory class used to create the read in 1H-15N HSQC NMR titration data. Note that the first resonance 
 * is a {@link AmideNitrogen} and the second resonance is a {@link AmideProton}
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
     * A method to read a {@link AmideNitrogen} chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a {@link AmideNitrogen} subclass of {@link Resonance}
     */
    @Override
    public Resonance getFirstResonanceSubclass(Scanner scanner) {
        return AmideNitrogen.validateAndCreate(scanner.nextDouble());
    }
    
    /**
     * A method to read a {@link AmideProton} chemical shift from a peak list
     * 
     * @param scanner the object to read in the data from the peak list file
     * 
     * @return a {@link AmideProton} subclass of {@link Resonance}
     */
    @Override
    public Resonance getSecondResonanceSubclass(Scanner scanner) {
        return AmideProton.validateAndCreate(scanner.nextDouble());
    }
    
    /**
     * A method to create a concrete {@link AmideNitrogenProtonTitrationPoint} subclass of
     * {@link TitrationPoint}
     * 
     * @param ligandConc the ligand concentration
     * @param receptorConc the receptor concentration
     * @param firstCoordinate the first {@link Resonance}, here is a {@link AmideNitrogen}
     * @param secondCoordinate the first {@link Resonance}, here is a {@link AmideProton}
     * 
     * @return a fully initialized {@link AmideNitrogenProtonTitrationPoint} concrete subclass of
     * {@link Resonance}
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

package edu.uconn.kddwcalc.analyze;

import edu.uconn.kddwcalc.data.TypesOfTitrations;

/**
 * A class containing a single simple static factory to instantiate a subclass of {@link AbsFactory}.
 * The switch statement using a {link String} that came from
 * {link InputGUIController#typeOfTitration}. These string must match. I probably should have used enums
 * but its done now.
 * 
 * @author Alex R.
 * 
 * @see AbsFactory
 * @see edu.uconn.kddwcalc.data.TitrationSeries
 * @see edu.uconn.kddwcalc.gui.FastExchangeGUIController
 * 
 * 
 * @since 1.8
 */
public class FactoryMaker {
    
    /**
     * Construction of {@link FactoryMaker} object is unnecessary; call the static method {@link #createFactory}. 
     */
    private FactoryMaker() {
        }

    /**
     * Creates a concrete implementation of {@link AbsFactory}.
     * 
     * @param type the type of titration (chosen by user in GUI)
     * 
     * @return a concrete subclass of {@link AbsFactory}
     * 
     * @see TypesOfTitrations
     * @see AbsFactory
     * 
     * @throws NullPointerException if the variable {@link AbsFactory} is not initialized in the
     * switch statement.
     */
    public static AbsFactory createFactory(TypesOfTitrations type) {
        
        AbsFactory absFactory = null;
        
        switch (type) {
            
            case AMIDEHSQC: 
                absFactory = new AmideNitrogenProtonFactory();
                break;
                            
            case METHYLHMQC: 
                absFactory = new MethylCarbonProtonFactory();
                break;
            
            default: 
                throw new NullPointerException("In class FactoryMaker, the variable absFactory"
                + " was not instantiated before the return statement. Why didn't the switch "
                + "make a reference to a concrete factory class when switching enums?");                  
        }

        return absFactory;
    }
}  // end class FactoryMaker

package edu.uconn.kddwcalc.data;

/**
 * A class containing a single simple static factory to instantiate a subclass of <code>AbsFactory</code>.
 * The switch statement using a <code>String</code> that came from
 * <code>InputGUIController.typeOfTitration</code>. These string must match. I probably should have used enums
 * but its done now.
 * 
 * @author Alex R.
 * 
 * @see AbsFactory
 * @see TitrationSeries
 * @see edu.uconn.kddwcalc.gui.InputGUIController
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
     * Creates a concrete implementation of <code>AbsFactory</code>.
     * 
     * @param type the type of titration (chosen by user in GUI)
     * 
     * @return a concrete subclass of {@link AbsFactory}
     * 
     * @throws IllegalArgumentException if the variable <code>AbsFactory</code> is not initialized in the
     * switch statement.
     */
    public static AbsFactory createFactory(String type) {
        AbsFactory absFactory = null;
   
        
        if (type.equalsIgnoreCase("1H-15N HSQC"))
            absFactory = new AmideNitrogenProtonFactory();
        
        else if (type.equalsIgnoreCase("1H-13C methyl HMQC"))
            absFactory = new AmideNitrogenProtonFactory();
        
        
        if (absFactory == null)
            throw new NullPointerException("In class FactoryMaker, the variable absFactory"
                + " was still null before return statement. Why didn't the if statement "
                + "make a reference to a concrete factory class?");
        
        return absFactory;
    }
}  // end class FactoryMaker

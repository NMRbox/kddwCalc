/* created by Alex Ri on 170704
*
* Somewhere, the program has to make a decision on what type of spectrum is being analyzed (here)
*
* This class takes the info from teh GUI and uses a switch statement to instantiate a 
* subclass of the abstract factory.
*
* method createFactory:
*  the String paramater is the text from the comboBox is the GUI where user choose spectrum type.
*/

package edu.uconn.kddwcalc.data;

public class FactoryMaker {
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

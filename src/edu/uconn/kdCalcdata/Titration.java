// created 170626 by Alex R.

// This class represents the data for a full titration curve of a single residue (i.e. peak).
// Thus, it contains a Collection of TitrationPoints

package edu.uconn.kdCalcdata;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Titration implements Comparator
{
    
    private final List<TitrationPoint> titration = new ArrayList<>();
    
    private final double multiplier;
    
    // one argument constructor
    public Titration(double multiplier)
    {
        this.multiplier = multiplier;
    }
    
    public final void addPoint(TitrationPoint pnt)
    {
        titration.add(pnt);
    }
    
    // TODO code me
    /*public ResultsObject calculateResults()
    {
        ResultsObject result = null;
        
        return result;
    }
*/
    @Override
    public int compare(Object o1, Object o2) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
    @Override
    public String toString()
    {
        return String.format("%nTitration:%nLigandConc  ReceptorConc  Resonance1  Resonance 2"
            + titration.stream()
                       .map(TitrationPoint::toString)
                       .reduce("", (x, y) -> String.format("%s%n%s", x, y)));
    }
    
    */
    
    public void printTitration()
    {
        System.out.printf("Titration with multiplier of %.2f%n"
            + "LigandConc  ReceptorConc  Resonance1  Resonance 2%n", multiplier);
        
        titration.stream()
                 .forEach(System.out::println);
  
    }
    
}

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
    
    // one-argument constructor.
    // the first file (top of gui) will be searched for residues number which will
    // be used to create the set of titrations.  
    
    public final void addPoint(TitrationPoint pnt)
    {
        titration.add(pnt);
    }
    
    // TODO code me
    public ResultsObject calculateResults(double multiplier)
    {
        ResultsObject result;
        
        return result;
    }

    @Override
    public int compare(Object o1, Object o2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

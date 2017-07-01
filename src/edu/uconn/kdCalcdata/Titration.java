// created 170626 by Alex R.

package edu.uconn.kdCalcdata;

import java.util.ArrayList;


public class Titration implements Comparator
{
    private final int residueNumber;
    
    private final ArrayList<TitrationPoint> titration = new ArrayList<>();
    
    // one-argument constructor.
    // the first file (top of gui) will be searched for residues number which will
    // be used to create the set of titrations.  
    public Titration(int residueNumber)
    {
        this.residueNumber = residueNumber;
    }
    
    // TODO code me
    public ResultsObject calculateResults(double multiplier)
    {
        ResultsObject result;
        
        return result;
    }
    
    
    
}

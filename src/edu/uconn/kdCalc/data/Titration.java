// created 170626 by Alex R.

// This class represents the data for a full titration curve of a single residue (i.e. peak).
// Thus, it contains a Collection of TitrationPoints

/*

edited by Alex R. on 170722 
added methods getCSPsFrom2DPoints, getReceptorConcList, and getLigandConcList.
they might not be necessary in the end.

*/



package edu.uconn.kdCalc.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Titration
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
    
    public void printTitration()
    {
        System.out.printf("Titration with multiplier of %.2f%nCSPs: %s%n"
            + "LigandConc     ReceptorConc    Resonance1      Resonance2%n", multiplier, getCSPsFrom2DPoints().toString());
        
        titration.stream() // results in a Stream<TitrationPoint>
                 .forEach(System.out::println);
  
    }
    
    public List<Double> getReceptorConcList()
    {
        return titration.stream()
                        .map(TitrationPoint::getReceptorConc)
                        .collect(Collectors.toList());
    }
    
    public List<Double> getLigandConcList()
    {
        return titration.stream()
                        .map(TitrationPoint::getLigandConc)
                        .collect(Collectors.toList());
    }
    
    
    // this method takes the 2D points and calculates distance between them using
    //
    // sqrt((x1-x2)^2 + (y1-y2)^2)
    // 
    // the multiplier scales the values to proton ppm values (as of 170724).
    //     note how getResonance1() and getResonance2() are used below.
    //     this is a dependcy between this class and class Resonance
    
    public List<Double> getCSPsFrom2DPoints()
    {
        final List<Double> csps = new ArrayList<>();
        
        for(int ctr = 0; ctr < titration.size(); ctr++)
        {
            csps.add(Math.sqrt(Math.pow(titration.get(ctr).getResonance2().getChemShift() 
                - titration.get(0).getResonance2().getChemShift(), 2.0)
                + Math.pow(multiplier * (titration.get(ctr).getResonance1().getChemShift() 
                - titration.get(0).getResonance1().getChemShift()), 2.0)));
        }

        return csps;
    }

}

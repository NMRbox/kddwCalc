

package edu.uconn.kdCalcdata;

import java.util.ArrayList;
import java.util.List;



public class TitrationSeries 
{   
    private final List<Titration> titrationSeries = new ArrayList<>();
    
    public void addTitration(Titration titration)
    {
        titrationSeries.add(titration);
    }
}

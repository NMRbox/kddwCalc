

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
    
    /*
    @Override
    public String toString()
    {
        return titrationSeries.stream() 
                              .map(Titration::toString)
                              .reduce("", (x, y) -> String.format("%s%n%s", x, y));
    }
    */
    
    public void printTitrationSeries()
    {
        titrationSeries.stream()
                       .forEach(titr ->
                       {
                           titr.printTitration();
                           System.out.println();
                       });
    }
}

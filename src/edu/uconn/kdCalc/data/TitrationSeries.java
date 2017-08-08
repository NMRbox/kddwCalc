

package edu.uconn.kdCalc.data;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;



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
    
    public double[] getCumulativeShifts()
    {

        List<List<Double>> cumulativeCSPintermediate = titrationSeries.stream()  // now have Stream<Titration>
                                            .map(Titration::getCSPsFrom2DPoints) // now have Stream<List<Double>>
                                            .collect(Collectors.toList());        
        
        List<Double> cumulativeCSPs = new ArrayList<>();
        
        // intialializes with values of 0.0 and sets the lenght of cumulative CSPs
        for(int ctr = 0; ctr < titrationSeries.get(0).getCSPsFrom2DPoints().size(); ctr++)
        {
            cumulativeCSPs.add(0.0);
        }
        
        for(List<Double> cspsForOneResidue : cumulativeCSPintermediate)
        {
            for(int ctr = 0; ctr < cspsForOneResidue.size(); ctr++)
            {
                Double temp = Double.sum(cspsForOneResidue.get(ctr), cumulativeCSPs.get(ctr));
                
                cumulativeCSPs.set(ctr, temp);
            }
        }
        
        
        // target array in least squaures solver must be double[] not List<Double>
        double[] cumShiftsArray = new double[cumulativeCSPs.size()];
        
        for(int ctr = 0; ctr < cumShiftsArray.length; ctr++)
        {
            cumShiftsArray[ctr] = cumulativeCSPs.get(ctr);
        }
        
        return cumShiftsArray;   
        
    } // end method GetCumulativeShifts()
    
    public double[] getReceptorConcArray()
    {
        return titrationSeries.get(0).getReceptorConcArray();
    }
    
    public double[] getLigandConcArray()
    {
        return titrationSeries.get(0).getLigandConcArray();
                
    }
    
    // writes a text file 
    public void writeToFileForExcel()
    {
        Formatter output = openFileToWrite();
        
        writeFile(output);
        
        closeFile(output);
        
    }
    
    private Formatter openFileToWrite()
    {
        Formatter output = null;
        
        try
        {
            output = new Formatter("results.txt");
        }
        catch (SecurityException e)
        {
            System.err.println("Write permission denied");
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error opening file");
        }
 
        return output;
    }
    
    private void writeFile(Formatter output)
    {
        double[] ligandConcArray = getLigandConcArray();
        double[] receptorConcArray = getReceptorConcArray();
        double[] cumShifts = getCumulativeShifts();
        
        output.format("P0\tL0\tCSP%n");
        
        for(int ctr = 0; ctr < getLigandConcArray().length; ctr++)
        {
            output.format("%6.3f\t%6.3f\t%6.5f%n", receptorConcArray[ctr], ligandConcArray[ctr],
                    cumShifts[ctr]);
        }
        
    }
    
    private void closeFile(Formatter output)
    {
        if(output != null)
            output.close();
    }
            
    public List<Titration> getTitrationSeries()
    {
        return titrationSeries;
    }

    double[] getCSPbyResidueArray(double kd) 
    {
        return titrationSeries.stream() // now have Stream<Titration>
                               .mapToDouble((Titration titr) -> 
                               {    return LeastSquaresFitter.fitDwForAResidue(getLigandConcArray(), 
                                                                               getReceptorConcArray(), 
                                                                               titr.getCSPsByResidueArray(), 
                                                                               kd);
                               })
                               .toArray(); 
    }
        
    
} // end class TitrationSeries

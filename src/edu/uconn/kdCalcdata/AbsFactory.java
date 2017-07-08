/* created by Alex Rizzo on 170701
*
*
// 
// important note:
// for the three ArrayList objects, each index matches the same
// indicies for the other two. for instance, index[0] has the 
// receptor and ligand concentrations and the chemical shifts for
// the first titration point. these are the data from teh first line of the
// GUI that requests the data
*
* edited by Alex Ri on 170704 and 170706 to use a Template method to create the dataset.
*
*
*  class RawData has: 
*     private final ArrayList<Path> dataFiles;
*     private final ArrayList<Double> ligandConcs;
*     private final ArrayList<Double> receptorConcs;
*     private final double multiplier;
*     private final boolean resonanceReversal;

*/

package edu.uconn.kdCalcdata;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AbsFactory 
{
    // no-argument default constructor is only construtor
    
    // method that data an object of class RawData (comes from user) and parses files.
    // 
    // this is meant to use the Template method pattern, where creation of each resonance is
    // overriden in a subclass
    public final TitrationSeries analyzeDataFiles(RawData dataObject)
    {
        final TitrationSeries dataSet = new TitrationSeries();
        
        // open files by getting a List<Scanner> from the List<Path>
        List<Scanner> scanners = makeScannersFromPaths(dataObject.getDataFiles());
        
        while(scanners.get(0).hasNext())
        {
            Titration titration = new Titration(dataObject.getMultiplier());
            
            for(int ctr = 0; ctr < dataObject.getDataFiles().size(); ctr++)
            {
                TitrationPoint point = makeTitrationPoint(scanners.get(ctr), 
                    dataObject.getLigandConcs().get(ctr), dataObject.getReceptorConcs().get(ctr),
                    dataObject.getResonanceReversal());
                
                titration.addPoint(point);
            }
            
            dataSet.addTitration(titration);   
        }
        
        closeFiles(scanners);

        return dataSet;
    }
    
    // override in subclasses to create correct Resonance
    public abstract Resonance[] makeResonances(Scanner scanner, boolean resonanceReversal);
    
    public abstract TitrationPoint makeTitrationPoint(Scanner scanner, double ligandConc,
        double receptorConc, boolean resonanceReversal);
    
    
    private List<Scanner> makeScannersFromPaths(List<Path> paths)
    {
        final List<Scanner> scanners = new ArrayList<>();
        
        try
        {
            for (Path pth : paths)
            {
                scanners.add(new Scanner(pth));
            }
        }
        catch(IOException e)
        {
            System.err.println("Exception when change List<Path> to List<Scanner> in AbsFactory");
        }
        
        return scanners;
    }
    
    
    private void closeFiles(List<Scanner> scanners)
    {
        scanners.stream()
                .forEach(Scanner::close);
    }
    
}

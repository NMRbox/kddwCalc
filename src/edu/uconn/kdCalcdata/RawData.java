/* created by Alex Ri on 170704
*
* After the user presses "Analyze" or "Execute" in the GUI, the user input needs to be 
* gathered and sent to the factory to parse. This class is sent to the 
* Template method in the abstractFactory class 
* 
* The object is created using a simple static factory method (createRawData) so I could
* add a couple of small validations. 1) each ArrayList must have same number of elements
* and 2) each text file associated with each path must have the same number of lines. 
*
* multipler is how to scale the two nuclei (carbon/proton different than carbon/nitrogen)
*
* resonanceReverseal is related to how the points are listed in the text files (the order),
* i.e. is it proton then carbon, or carbon then proton.
*/

package edu.uconn.kdCalcdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RawData 
{
    private final List<Path> dataFiles;
    private final List<Double> ligandConcs;
    private final List<Double> receptorConcs;
    private final double multiplier;
    private final boolean resonanceReversal;
    
    private RawData(List<Path> dataFiles, List<Double> ligandConcs,
        List<Double> receptorConcs, double multiplier, boolean resonanceReversal)
    {
        this.dataFiles = dataFiles;
        this.ligandConcs = ligandConcs;
        this.receptorConcs = receptorConcs;
        this.multiplier = multiplier;
        this.resonanceReversal = resonanceReversal;
    }
    
    public final static RawData createRawData(List<Path> dataFiles, List<Double> ligandConcs,
        List<Double> receptorConcs, double multiplier, boolean resonanceReversal) throws IOException
    {
        
        // bit of code to make sure user entered and equal number of dataFiles, ligand conccentrations...
        if (dataFiles.size() != ligandConcs.size()
            || ligandConcs.size() != receptorConcs.size()
            || dataFiles.size() != receptorConcs.size())
        {
            throw new IllegalArgumentException("The size of ArrayLists for dataFiles, ligandConcs"
                + " and receptorConcs do not have the same length. They must.");
        }
        
        

      // validation to ensure equal number of lines in each data file
        
        // this array holds the number of lines in each file
        long[] numLines = new long[dataFiles.size()];
        
        // now count the number of lines in each file
        try
        {
            for (int ctr = 0; ctr < dataFiles.size(); ctr++)
            {
                numLines[ctr] = Files.lines(dataFiles.get(ctr)).count();
            }
        }
        catch(IOException e)
        {
            System.err.println("Error when opening file in class RawData");

        
        // if every number in that array isnt the same    
         if (Arrays.stream(numLines).distinct().count() != 1)
             throw new IllegalArgumentException("Data files dont all have same number of lines");
        
        }
        
        // if this statement is reached, all the validation was passed 
        return new RawData(dataFiles, ligandConcs,receptorConcs, multiplier, resonanceReversal);
          
        
    }

    
    // GETTERS
    public final List<Path> getDataFiles()
    {
        return dataFiles;
    }
    
    public final List<Double> getLigandConcs()
    {
        return ligandConcs;
    }
    
    public final List<Double> getReceptorConcs()
    {
        return receptorConcs;
    }
    
    public final double getMultiplier()
    {
        return multiplier;
    }
    
    public final boolean getResonanceReversal()
    {
        return resonanceReversal;
    }
    
}

/* created by Alex Ri on 170704
*
* After the user presses "Analyze" or "Execute" in the GUI, the user input needs to be 
* gathered and sent to the factory to parse and then analyze. This class is sent to the 
* Template method in the abstractFactory class 
*/

package edu.uconn.kdCalcdata;

public class RawData 
{
    private final ArrayList<Path> dataFiles;
    private final ArrayList<Double> ligandConcs;
    private final ArrayList<Double> receptorConcs;
    private final double multipler;
    private final boolean resonanceReversal;
    
    private RawData()
    {
        
    }
    
    public final static RawData createRawData()
    {
        
    }
}

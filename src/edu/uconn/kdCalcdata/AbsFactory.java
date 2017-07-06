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

public class AbsFactory 
{
    // no-argument default constructor is only construtor
    
    // method that data an object of class RawData (comes from user) and parses files.
    // 
    // this is meant to use the Template method pattern, where creation of each resonance is
    // overriden in a subclass
    public final TitrationSeries analyzeDataFiles(RawData dataObject)
    {
        final TitrationSeries dataSet = new TitrationSeries();
        
        
        
        
        
        
        
        return dataSet;
    }
}

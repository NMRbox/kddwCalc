package edu.uconn.kdCalc.data;

/**
 * 
 * @author Alex Rizzo
 * 
 */
public class CumResults 
{
    double kd; 
    double percentBound;
    double[][] presentationFit;
        
    
    private CumResults(double kd, 
                       double percentBound,
                       double[][] presentationFit)
    {
        this.kd = kd;
        this.percentBound = percentBound;
        this.presentationFit = presentationFit;
    }
    
    public static CumResults makeCumResults(double kd, 
                                            double percentBound,
                                            double[][] presentationFit)               
    {
        if (kd < 0 || percentBound < 0)
            throw new IllegalArgumentException("kd < 0 or percentBound < 0 (CumResults)");
        
        // TODO code validation for presentationFit a double[][]. must all be positive
        
        return new CumResults(kd, percentBound, presentationFit); 
    }
    
    public double getKd()
    {
        return kd;
    }
    
    public double getPercentBound()
    {
        return percentBound;
    }
} // end class CumResults

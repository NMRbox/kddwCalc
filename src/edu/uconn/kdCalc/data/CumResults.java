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
    //double rms;
    //double iterations;
    //double evaluations;
        
    
    public CumResults(double kd, double percentBound)
    {
        this.kd = kd;
        this.percentBound = percentBound;
    }
    
    public static CumResults makeCumResults(double kd, double percentBound)
    {
        if (kd < 0 || percentBound < 0)
            throw new IllegalArgumentException("kd < 0 or percentBound < 0 (CumResults)");
        
        return new CumResults(kd, percentBound); 
    }
    
    public double getKd()
    {
        return kd;
    }
    
    public double getPercentBound()
    {
        return percentBound;
    }
}

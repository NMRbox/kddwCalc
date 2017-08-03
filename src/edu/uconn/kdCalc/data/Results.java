package edu.uconn.kdCalc.data;

import java.util.Arrays;

class Results 
{
    double kd;
    double percentBound;
    double[] boundCSPArray;
    
    private Results(double kd, 
                    double percentBound, 
                    double[] boundCSPArray)
    {
        this.kd = kd;
        this.percentBound = percentBound;
        this.boundCSPArray = boundCSPArray;
    }
    
    public static Results makeResultsObject(double kd, 
                                            double percentBound, 
                                            double[] boundCSPArray)
    {
        if (kd < 0.0 || percentBound < 0.0)
            throw new IllegalArgumentException("negative kd or %bound in Results");
        
        for(double csp : boundCSPArray)
        {
            if (csp < 0.0)
                throw new IllegalArgumentException("negative csp in Results");
        }
        
        return new Results(kd, percentBound, boundCSPArray);
    }
    
    public double getKd()
    {
        return kd;
    }
    
    public double getPercentBound()
    {
        return percentBound;
    }
    
    public double[] getBoundCSPArray()
    {
        return boundCSPArray;
    }
    
    public void writeResultsToDisk()
    {
        System.out.println(getKd());
        System.out.println(getPercentBound());
        
        System.out.println();
        
        Arrays.stream(getBoundCSPArray())
                        .forEach(System.out::println);
    }
}

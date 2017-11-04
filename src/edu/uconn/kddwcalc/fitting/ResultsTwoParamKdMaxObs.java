package edu.uconn.kddwcalc.fitting;

public class ResultsTwoParamKdMaxObs {
    
    private final double kd; 
    private final double maxObservable;
    private final double[] expObservables;
    private final double[][] presentationFit;

    /**
     * Initializes an instance of AggResults with the results of least squares fitting of the cumulative CSP data
     * 
     * @param kd the affinity constant
     * @param percentBound the percentage of receptor bound at the highest point
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * 
     * @see #makeCumResults()
     */
    private ResultsTwoParamKdMaxObs(double kd, 
                                    double maxObservable,
                                    double[] expObservables,
                                    double[][] presentationFit) {
        this.kd = kd;
        this.maxObservable = maxObservable;
        this.expObservables = expObservables;
        this.presentationFit = presentationFit;

    }

    /**
     * Static simple factory that performs validations of the data before call 
     * the constructor
     * 
     * @param kd the affinity constant
     * @param maxObservable the percentage of receptor bound at the highest point
     *  @param obsAtFinalExpPoint
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * 
     * @see LeastSquaresFitter#fitTwoParamKdAndMaxObs
     * 
     * @return an instance of <code>AggResults</code>, (most likely for further processing)
     */
    public static ResultsTwoParamKdMaxObs makeTwoParamResults(double kd, 
                                                              double maxObservable,
                                                              double[] expObservables,
                                                              double[][] presentationFit) {
        if (kd < 0 || maxObservable  < 0)
            throw new IllegalArgumentException("kd < 0 or percentBound < 0 (CumResults)");

        // TODO code validation for presentationFit a double[][]. must all be positive

        return new ResultsTwoParamKdMaxObs(kd, 
                                           maxObservable, 
                                           expObservables,
                                           presentationFit); 
    }

    /**
     * Gets the Kd (affinity constant) for the interaction
     * 
     * @return a <code>double</code> value representing the Kd
     */
    public double getKd() {
        return kd;
    }

    /**
     * Gets the percent of receptor bound at the highest ligand ratio (final titration point)
     * 
     * @return a <code>double</code> value representing the percent bound (0-1)
     */
    public double getMaxObservable() {
        return maxObservable;
    }

    /**
     * A way to view the data that would be used in a publication figure
     * 
     * @return a two-column <code>double</code> matrix with ligand ratio on x-axis 
     * and percent bound on the y-axis
     */
    public double[][] getPresentationFit() {
        return presentationFit;
    }

    @Override
    public String toString() {
        
        StringBuilder string = new StringBuilder();
        
        string.append(String.format("kd = %.2f uM%n", kd));
        
        string.append(String.format("percent bound at final point = %.1f%n%n", 
                expObservables[expObservables.length - 1] / maxObservable * 100));
            
        string.append(String.format("Presentation fit:%n"));    
        string.append(String.format("%17s%17s%17s%n","ligand ratio", 
                                                     "model point", 
                                                     "exp point"));
            
        for(int ctr = 0; ctr < presentationFit.length; ctr++) {
            for(int ctr2 = 0; ctr2 < presentationFit[ctr].length; ctr2++) {
                string.append(String.format("%17f", presentationFit[ctr][ctr2]));
            }
            string.append(String.format("%n"));
        }  
        
        return string.toString();
    }
}


package edu.uconn.kddwcalc.fitting;

/**
 * A class which holds results from a two parameter fit of binding data. The two
 * parameters are (1) Kd and (2) the maximum observable at fully bound.
 * 
 * @author Alex R.
 */
public class ResultsTwoParamKdMaxObs {
    
    private final double kd; 
    private final double maxObservable;
    private final double[] expObservables;
    private final double[][] presentationFit;

    /**
     * Initializes an instance of AggResults with the results of least squares fitting of the cumulative CSP data
     * 
     * @param kd the affinity constant (a parameter from fitting)
     * @param maxObservable the maximum observable at fully bound (parameter from fitting)
     * @param expObservables experimental data expressed as (Point - Point0)
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
     * @param expObservables an array of experimental data as (point - point0)
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
     * Gets the Kd (affinity constant) for the interaction. This is one of the
     * parameters from fitting.
     * 
     * @return a <code>double</code> value representing the Kd
     */
    public double getKd() {
        return kd;
    }

    /**
     * Gets the maximum observable point if the fully bound state was reached. This
     * is a parameter from the fitting (e.g. dw or delta-omega)
     * 
     * @return a <code>double</code> value representing the percent bound (0-1)
     */
    public double getMaxObservable() {
        return maxObservable;
    }

    /**
     * A way to view the data that would be used in a publication figure
     * 
     * @return a three-column <code>double</code> matrix with ligand ratio on x-axis 
     * and percent bound on the y-axis.
     * 
     * Includes experimental and fitted points to plot how well the model
     * fits the data.
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
        string.append(String.format("%15s%15s%15s%n","ligand ratio", 
                                                     "model point", 
                                                     "exp point"));
            
        for(int ctr = 0; ctr < presentationFit.length; ctr++) {
            for(int ctr2 = 0; ctr2 < presentationFit[ctr].length; ctr2++) {
                string.append(String.format("%15f", presentationFit[ctr][ctr2]));
            }
            string.append(String.format("%n"));
        }  
        
        return string.toString();
    }
}


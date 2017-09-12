package edu.uconn.kddwcalc.data;

import java.util.List;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;


/**
 * This class performs nonlinear least squares fitting of NMR fast exchange data
 * using the Apache Commons Mathematics Library. 
 * 
 * Clients of this class will pass in a TitrationSeries
 * object which contains all the data necessary for fitting and receive a Results object back which contains
 * all the results needed.
 * 
 * Note that {@link #fit} is the only public interface in this class
 * 
 * @author Alex R.
 *
 * @see TitrationSeries
 * @see Results
 */
public class LeastSquaresFitter {
    
    /**
     * A static method to get a {@link Results} object from a 
     * {@link TitrationSeries}
     * 
     * @param series contains all the data needed for fitting
     * 
     * @see Results
     * @see AggResults
     * 
     * @throws ArraysInvalidException if arrays have different lengths or duplicate data
     * 
     * @return a {@link Results} object containing Kd and other values from the fitting
     */
    public static Results fit(TitrationSeries series) throws ArraysInvalidException {
        final double[] ligandConcArray = series.getLigandConcArray();
        final double[] receptorConcArray = series.getReceptorConcArray();
        
        final double[] aggExpCSPs = series.getCumulativeShifts();
        
        if (!DataArrayValidator.isValid(ligandConcArray, receptorConcArray, aggExpCSPs))
            throw new ArraysInvalidException("in LeastSquaresFitter.fit, either duplicate data or arrays are"
                + " a different length");
        
        AggResults aggResultsObject = fitCumulativeData(ligandConcArray, receptorConcArray, aggExpCSPs);
        
        double kd = aggResultsObject.getKd();
        double percentBound = aggResultsObject.getPercentBound();
        double[][] presentationFit = aggResultsObject.getPresentationFit();
        
        double[] boundCSPArrayByResidue =
                     series.getTitrationSeries().stream() // now have Stream<Titration>
                               .mapToDouble((Titration titr) ->  {
                                   return LeastSquaresFitter.fitDwForAResidue(ligandConcArray, 
                                                                              receptorConcArray, 
                                                                              titr.getCSPsByResidueArray(), 
                                                                              kd);
                               })
                               .toArray(); 
        
        
        return Results.makeResultsObject(kd, percentBound, boundCSPArrayByResidue, presentationFit);
    }   
    
    /**
     * A static method to fit both Kd (affinity) and dw (delta-omega) using
     * the cumulative chemical shifts. 
     * 
     * This method contains within it an Override of the method:
     * <code> public Pair<RealVector, RealMatrix>> value (final RealVector paramPoint) </code>
     * 
     * In this case, RealVector paramPoint is a two element vector (array) where the first
     * element is the kd and the second element is dw
     * 
     * @param ligandConcArray contains total ligand concentrations (L0)
     * @param receptorConcArray contains total receptor (labeled species) concentrations (P0)
     * @param aggExpCSPsArray contains the cumulative chemical shift perturbations
     * 
     * @see AggResults
     * @see #makeArrayOfPresentationFit
     * 
     * @throws ArraysInvalidException if arrays have different lengths or duplicate data
     * 
     * @return a {@link AggResults} object with Kd, percent bound, and presentation fitting
     * 
     */
    private static AggResults fitCumulativeData(double[] ligandConcArray,
                                                double[] receptorConcArray,
                                                double[] aggExpCSPsArray) 
                                                throws ArraysInvalidException {
        
        if (!DataArrayValidator.isValid(ligandConcArray, receptorConcArray, aggExpCSPsArray))
            throw new ArraysInvalidException("in LeastSquaresFitter.fitCumulativeData");

        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            double kd = paramPoint.getEntry(0);
            double dw = paramPoint.getEntry(1);
            
            RealVector value = new ArrayRealVector(ligandConcArray.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcArray.length, 2);
            
            for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
                double L0 = ligandConcArray[ctr];
                double P0 = receptorConcArray[ctr];
                
                value.setEntry(ctr, calcModel(P0, L0, kd, dw));
                
                jacobian.setEntry(ctr, 0, calcModelDerivativeKd(P0, L0, kd, dw));
                jacobian.setEntry(ctr, 1, calcModelDerivativeDw(P0, L0, kd));
            }
            
            return new Pair<>(value, jacobian);
        };
        
        // iniial guess is 10 uM with a cumulative shift of 5. i might try to dw 
        //  change based on number of residues. later edit: maybe not. seems to be robust over a range of kd
        final double[] startingGuess = {10, 5};
        
        LeastSquaresOptimizer.Optimum optimum = buildAndGetOptimum(startingGuess, function, aggExpCSPsArray);
        
        double kd =  optimum.getPoint().getEntry(0);   
        
        double dwMax =  optimum.getPoint().getEntry(1);  // at fully bound
        double dwAtHighestPoint = aggExpCSPsArray[aggExpCSPsArray.length - 1];
        
        double[][] presentationFit = makeArrayOfPresentationFit(ligandConcArray, 
                                                                receptorConcArray, 
                                                                kd, 
                                                                dwMax, 
                                                                aggExpCSPsArray);
        
        
        
                
        return AggResults.makeCumResults(kd, dwAtHighestPoint / dwMax, presentationFit);
    
    }
    
    /**
     * A static helper method that uses the Kd from the cumulative fit to determine total dw
     * between free and bound states for a residue using least squares.
     * 
     * This method contains within it a lambda expression that overrides the method:
     * <code> public Pair<RealVector, RealMatrix>> value (final RealVector paramPoint) </code>
     * 
     * In this case, RealVector paramPoint is a one element vector (array) where the only
     * element is dw (kd is fixed to cumulative value)
     * 
     * @param ligandConcArray Total ligand concentrations
     * @param receptorConcArray Total protein concentrations (labeled species)
     * @param cspArray Contains chemical shift perturbations of an individual residues
     * @param kdFromCumData The affinity constant (Kd)
     * 
     * @throws ArraysInvalidException if the protein concentrations are equal or there are 
     * duplicates in cspArray.
     * 
     * @return Value of the total CSP between free and fully bound for a single residue
     */
    private static double fitDwForAResidue(double[] ligandConcArray,
                                           double[] receptorConcArray,
                                           double[] cspArray,
                                           double kdFromCumData) {
        
        
        if (!DataArrayValidator.isValid(ligandConcArray, receptorConcArray))
            throw new IllegalArgumentException("in LeastSquaresFitter.fitDwForAResidue");
        
        // only checking for duplicates technically, single element in array created by varargs
        if (!DataArrayValidator.isValid(cspArray))
            throw new IllegalArgumentException("in LeastSquaresFitter.fitDwForAResidue, for aggExpCSPs");
        
        
        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            final double dw = paramPoint.getEntry(0);
            
            RealVector value = new ArrayRealVector(ligandConcArray.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcArray.length, 2);
            
            for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
                double L0 = ligandConcArray[ctr];
                double P0 = receptorConcArray[ctr];
                
                value.setEntry(ctr, calcModel(P0, L0, kdFromCumData, dw));
                
                jacobian.setEntry(ctr, 0, calcModelDerivativeDw(P0, L0, kdFromCumData));
            }
            
            return new Pair<>(value, jacobian);
        };
        
        // iniial guess is 10 uM with a cumulative shift of 0.1 ppm proton
        final double[] startingGuess = {10, 0.1};
        
        LeastSquaresOptimizer.Optimum optimum = buildAndGetOptimum(startingGuess, function, cspArray);
        
        return optimum.getPoint().getEntry(0);
    }
    
    
    /**
     * A static helper method that uses {@link LeastSquaresBuilder} to create a {@link LeastSquaresProblem}
     * which is then evaluated using LM minimization. Note that this is used for both the two-parameter (kd, dw)
     * cumulative fit and the one-parameter per-residue (dw) fit.
     * 
     * @param startingGuess Initial values for parameters. 
     * @param function The function to minimize
     * @param target The experimental chemical shifts that function tries to optimize toward
     * 
     * @see #fitCumulativeData
     * @see #fitDwForAResidue
     * 
     * @return A {@link LeastSquaresOptimizer.Optimum} object containing results
     */
    private static LeastSquaresOptimizer.Optimum buildAndGetOptimum(double[] startingGuess, 
                                                                    MultivariateJacobianFunction function, 
                                                                    double[] target) {
        LeastSquaresProblem problem = new LeastSquaresBuilder().
                                start(startingGuess).
                                model(function).
                                target(target).
                                lazyEvaluation(false).
                                maxEvaluations(10000).
                                maxIterations(10000).
                                build();
        
        return new LevenbergMarquardtOptimizer().optimize(problem);
    }
    
    /**
     * A static helper method that creates a two-column array of data that are [x,y] values
     * for plotting the data. X-axis is ligand ratio (ligand / receptor) and y-axis is percent bound
     * which is from modelCSP / dwMax
     * 
     * @param ligandConcArray
     * @param receptorConcArray
     * @param kd
     * @param dwMax
     * @param cumCSPsArray
     * 
     * @see #calcModel
     * 
     * @return A 2 column array containing 
     */
    private static double[][] makeArrayOfPresentationFit(double[] ligandConcArray, 
                                                         double[] receptorConcArray, 
                                                         double kd, 
                                                         double dwMax,
                                                         double[] aggExpCSPsArray) 
                                                         throws ArraysInvalidException {    
        
        if (!DataArrayValidator.isValid(ligandConcArray, receptorConcArray, aggExpCSPsArray))
            throw new ArraysInvalidException("in LeastSquaresFitter.makeArrayOfPresentationFit");
        
        double [][] presentationFit = new double[ligandConcArray.length][3];
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
            presentationFit[ctr][0] = ligandConcArray[ctr] / receptorConcArray[ctr];
        }
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
            presentationFit[ctr][1] = 
                calcModel(receptorConcArray[ctr], ligandConcArray[ctr], kd, dwMax) / dwMax;
        }
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
            presentationFit[ctr][2] = aggExpCSPsArray[ctr] / dwMax;
        }
        
        return presentationFit;
        
        // TODO this method needs to be a 3 column array that also gives exprimental values
    }
    
    /**
     * A method that evaluates the model (ie the equation to be fit).
     * 
     * @param P0 The total receptor concentration (labeled species)
     * @param L0 The total ligand concentration
     * @param kd The affinity
     * @param dw Delta-omega between free and bound states
     * 
     * @see #calcModelDerivativeDw 
     * @see #calcModelDerivativeKd
     * 
     * @return Value of the model for the given parameters. Ostensibly, the return value
     * is a chemical shift perturbation (be it cumulative or for a single residue only)
     */
    private static double calcModel(double P0, 
                                    double L0, 
                                    double kd, 
                                    double dw) {
            // test of this equation worked using known values. unlikely issue is here
            return dw / (2 * P0) * ((kd + L0 + P0) - Math.sqrt(Math.pow(kd + L0 + P0, 2) - 4 * P0 * L0));
        }
    
    /**
     * A method that evaluates the first derivative with respect to dw (dM/d(dw))
     * of the equation in method <code>calcModel</code>. This is required for the
     * Jacobian matrix in LM minimization
     * 
     * @param P0 the total receptor concentration (labeled species)
     * @param L0 the total ligand concentration
     * @param kd the affinity
     * 
     * @see #calcModel
     * @see #calcModelDerivativeKd
     * 
     * @return value of the model derivative for the given parameters
     */
    private static double calcModelDerivativeDw(double P0, 
                                                double L0, 
                                                double kd) {
        return 1 / (2 * P0) * ((kd + L0 + P0) - Math.sqrt(Math.pow(kd + L0 + P0, 2) - 4 * P0 * L0));
    }
    
    
    /**
     * A method that evaluates the first derivative with respect to Kd (dM/d(kd)) 
     * of the equation in method {@link #calcModel}. This is required for the
     * Jacobian matrix in LV minimization
     * 
     * @param P0 the total receptor concentration (labeled species)
     * @param L0 the total ligand concentration
     * @param kd the affinity
     * @param dw delta-omega between free and bound states
     * 
     * @see #calcModel
     * @see #calcModelDerivativeDw
     * 
     * @return Value of the model derivative for the given parameters
     */
    private static double calcModelDerivativeKd(double P0, 
                                                double L0, 
                                                double kd, 
                                                double dw) {
        return dw / (2 * P0) * (1 - ((kd + L0 + P0) / Math.sqrt((Math.pow(kd + L0 + P0, 2) - 4 * L0 * P0))));
    }        
} // end class LeastSquaresFitter

    


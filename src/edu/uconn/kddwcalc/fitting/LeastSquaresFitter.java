package edu.uconn.kddwcalc.fitting;

import edu.uconn.kddwcalc.analyze.ArraysInvalidException;
import edu.uconn.kddwcalc.analyze.DataArrayValidator;
import edu.uconn.kddwcalc.data.Calculatable;
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
 * This class performs nonlinear least squares fitting of data for which the
 * experimental observable indicates the percent bound when compared with the
 * free and fully bound signals. 
 * 
 * Clients of this class will pass in a Calculatable object
 * object which contains all the data necessary for fitting and receive a 
 * Results object back
 * 
 * 
 * @author Alex R.
 *
 * @see Calculatable
 */
public class LeastSquaresFitter {
    
    /**
     * A static method to fit both Kd (affinity) and the maximum observable 
     * point at fully bound. 
     * 
     * This method contains within it an Override of the method:
     * <code> public Pair@ltRealVector, RealMatrix@gt value (final RealVector paramPoint) </code>
     * 
     * In this case, RealVector paramPoint is a two element vector (array) where the first
     * element is the kd and the second element is the maximum observable at fully bound
     * 
     * @param dataset a {@link edu.uconn.kddwcalc.data.Calculatable} object to fit
     * 
     * @see ResultsKdAndMaxObs
     * @see #makeArrayOfPresentationFit
     * 
     * @return a {@link ResultsKdAndMaxObs} object with Kd, percent bound, and presentation fitting
     * 
     */
    public static ResultsKdAndMaxObs fitTwoParamKdAndMaxObs(Calculatable dataset)  {
        
        final double[] ligandConcs = dataset.getLigandConcs();
        final double[] receptorConcs = dataset.getReceptorConcs();
        final double[] observables = dataset.getObservables();

        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            double kd = paramPoint.getEntry(0);
            double dw = paramPoint.getEntry(1);
            
            RealVector value = new ArrayRealVector(ligandConcs.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcs.length, 2);
            
            for(int ctr = 0; ctr < ligandConcs.length; ctr++) {
                double L0 = ligandConcs[ctr];
                double P0 = receptorConcs[ctr];
                
                value.setEntry(ctr, calcModel(P0, L0, kd, dw));
                
                jacobian.setEntry(ctr, 0, calcModelDerivativeKd(P0, L0, kd, dw));
                jacobian.setEntry(ctr, 1, calcModelDerivativeMaxObs(P0, L0, kd));
            }
            
            return new Pair<>(value, jacobian);
        };
        
        // starting guess of 10 uM Kd and overall delta omega of 5. seems to be
        // robust against errors in starting Kd
        final double[] STARTING_GUESS = {10, 5};
        
        LeastSquaresOptimizer.Optimum optimum = 
                buildAndGetOptimum(STARTING_GUESS, function, observables);
        
        double kd =  optimum.getPoint().getEntry(0);   
        
        double maxObservable =  optimum.getPoint().getEntry(1);  // at fully bound
        
        double[][] presentationFit = makeArrayOfPresentationFit(ligandConcs, 
                                                                receptorConcs, 
                                                                kd, 
                                                                maxObservable, 
                                                                observables);

        return ResultsKdAndMaxObs.makeTwoParamResults(kd, 
                                                      maxObservable, 
                                                      observables,
                                                      presentationFit);
    
    }
    
    /**
     * A static helper method that uses the Kd (probably from two parameter fit) 
     * to determine total the maximum difference in observable between free and bound states
     * 
     * This method contains within it a lambda expression that overrides the method:
     * <code> public Pair@ltRealVector, RealMatrix@gt value (final RealVector paramPoint) </code>
     * 
     * In this case, RealVector paramPoint is a one element vector (array) where the only
     * element is the maximum observable (kd is fixed)
     * 
     * @param dataset data to fit maximum observable only. kd is set from global fit
     * @param kdFromTwoParamFit The affinity constant (Kd)
     * 
     * @return value of the total difference in the observable between free
     *  and fully bound
     */
    public static ResultsKdAndMaxObs fitOneParamMaxObs(Calculatable dataset,
                                                       double kdFromTwoParamFit) {
        
        final double[] ligandConcArray = dataset.getLigandConcs();
        final double[] receptorConcArray = dataset.getReceptorConcs();
        final double[] observables = dataset.getObservables();
        
        if (!DataArrayValidator.isValid(ligandConcArray, receptorConcArray))
            throw new IllegalArgumentException("in LeastSquaresFitter.fitOneParamMaxObs");
        
        // only checking for duplicates technically, single element in array created by varargs
        if (!DataArrayValidator.isValid(observables))
            throw new IllegalArgumentException("in LeastSquaresFitter.fitOneParamMaxObs, "
                + "an issue with observable values (experimental data)");
        
        
        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            final double observable = paramPoint.getEntry(0);
            
            RealVector value = new ArrayRealVector(ligandConcArray.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcArray.length, 2);
            
            
            // TODO , use Stream<Titration>. makes no sense to sort data
            // and them pull out as individual arrays here.
            for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
                double L0 = ligandConcArray[ctr];
                double P0 = receptorConcArray[ctr];
                
                value.setEntry(ctr, calcModel(P0, L0, kdFromTwoParamFit, observable));
                
                jacobian.setEntry(ctr, 0, calcModelDerivativeMaxObs(P0, L0, kdFromTwoParamFit));
            }
            
            return new Pair<>(value, jacobian);
        };
        
        // iniial guess is 10 uM with a cumulative shift of 0.1 ppm proton
        final double[] startingGuess = {10, 0.1};
        
        LeastSquaresOptimizer.Optimum optimum = buildAndGetOptimum(startingGuess, function, observables);
        
        double maxObservable = optimum.getPoint().getEntry(0); // one parameter fit result
        
        double[][] presentationFit = 
            makeArrayOfPresentationFit(ligandConcArray, 
                                       receptorConcArray, 
                                       kdFromTwoParamFit, 
                                       maxObservable, 
                                       observables);
        
        return ResultsKdAndMaxObs.makeTwoParamResults(kdFromTwoParamFit, 
                                                      maxObservable, 
                                                      observables, 
                                                      presentationFit);
            
    }
    
    
    /**
     * A static helper method that uses {@link LeastSquaresBuilder} to create a {@link LeastSquaresProblem}
     * which is then evaluated using LM minimization. Note that this is used for both the two-parameter (kd, observable)
 cumulative fit and the one-parameter per-residue (observable) fit.
     * 
     * @param startingGuess Initial values for parameters. 
     * @param function The function to minimize
     * @param target The experimental chemical shifts that function tries to optimize toward
     * 
     * @see #fitTwoParamKdAndMaxObs
     * @see #fitOneParamMaxObs
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
     * A static helper method that creates a three-column array of data that are [x,y] values
     * for plotting the data. X-axis is ligand ratio (ligand / receptor) and y-axis is percent bound
     *which is from expObservable / maxObservable
     * 
     * @param ligandConcArray
     * @param receptorConcArray
     * @param kd
     * @param maxObservable
     * @param observables
     * 
     * @see #calcModel
     * 
     * @return A 3 column array containing 
     */
    private static double[][] makeArrayOfPresentationFit(double[] ligandConcArray, 
                                                         double[] receptorConcArray, 
                                                         double kd, 
                                                         double maxObservable,
                                                         double[] observables) {    
        
        double [][] presentationFit = new double[ligandConcArray.length][3];
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
            presentationFit[ctr][0] = ligandConcArray[ctr] / receptorConcArray[ctr];
        }
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
            presentationFit[ctr][1] = 
                calcModel(receptorConcArray[ctr], ligandConcArray[ctr], kd, maxObservable) / maxObservable;
        }
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++) {
            presentationFit[ctr][2] = observables[ctr] / maxObservable;
        }
        
        return presentationFit;
    }
    
    /**
     * A method that evaluates the model (ie the equation to be fit).
     * 
     * @param P0 The total receptor concentration (labeled species)
     * @param L0 The total ligand concentration
     * @param kd The affinity
     * @param maxObservable difference in observable between free and bound states
     * 
     * @see #calcModelDerivativeMaxObs 
     * @see #calcModelDerivativeKd
     * 
     * @return Value of the model for the given parameters. Ostensibly, the return value
     * is a chemical shift perturbation (be it cumulative or for a single residue only)
     */
    private static double calcModel(double P0, 
                                    double L0, 
                                    double kd, 
                                    double maxObservable) {
            // test of this equation worked using known values. 
            //unlikely issue is here
            return maxObservable / (2 * P0) * ((kd + L0 + P0) - 
                Math.sqrt(Math.pow(kd + L0 + P0, 2) - 4 * P0 * L0));
        }
    
    /**
     * A method that evaluates the first derivative with respect to observable (dM/d(observable))
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
    private static double calcModelDerivativeMaxObs(double P0, 
                                                    double L0, 
                                                    double kd) {
        return 1 / (2 * P0) * ((kd + L0 + P0) - 
            Math.sqrt(Math.pow(kd + L0 + P0, 2) - 4 * P0 * L0));
    }
    
    
    /**
     * A method that evaluates the first derivative with respect to Kd (dM/d(kd)) 
     * of the equation in method {@link #calcModel}. This is required for the
     * Jacobian matrix in LV minimization
     * 
     * @param P0 the total receptor concentration (labeled species)
     * @param L0 the total ligand concentration
     * @param kd the affinity
     * @param maxObservable difference in observable bet
     * 
     * @see #calcModel
     * @see #calcModelDerivativeMaxObs
     * 
     * @return Value of the model derivative for the given parameters
     */
    private static double calcModelDerivativeKd(double P0, 
                                                double L0, 
                                                double kd, 
                                                double maxObservable) {
        return maxObservable / 
            (2 * P0) * (1 - ((kd + L0 + P0) / 
            Math.sqrt((Math.pow(kd + L0 + P0, 2) - 4 * L0 * P0))));
    }        
} // end class LeastSquaresFitter







    


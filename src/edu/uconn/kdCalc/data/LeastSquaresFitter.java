package edu.uconn.kdCalc.data;

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



public class LeastSquaresFitter 
{
    public static Results fit(TitrationSeries series)
    {
        final double[] ligandConcArray = series.getLigandConcArray();
        final double[] receptorConcArray = series.getReceptorConcArray();
        
        final double[] cumCSPs = series.getCumulativeShifts();
        
        CumResults cumResultsObject = fitCumulativeData(ligandConcArray, receptorConcArray, cumCSPs);
        
        double kd = cumResultsObject.getKd();
        double percentBound = cumResultsObject.getPercentBound();
        double[][] presentationFit = cumResultsObject.getPresentationFit();
        
        double[] boundCSPArrayByResidue =
                     series.getTitrationSeries().stream() // now have Stream<Titration>
                               .mapToDouble((Titration titr) -> 
                               {    return LeastSquaresFitter.fitDwForAResidue(ligandConcArray, 
                                                                               receptorConcArray, 
                                                                               titr.getCSPsByResidueArray(), 
                                                                               kd);
                               })
                               .toArray(); 
        
        
        return Results.makeResultsObject(kd, percentBound, boundCSPArrayByResidue, presentationFit);
    }
        
        
        
    
    
    public static CumResults fitCumulativeData(double[] ligandConcArray,
                                               double[] receptorConcArray,
                                               double[] cumCSPsArray)
    {
        
        // TODO add code to make sure arrays are all same size
        
        
        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            double kd = paramPoint.getEntry(0);
            double dw = paramPoint.getEntry(1);
            
            RealVector value = new ArrayRealVector(ligandConcArray.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcArray.length, 2);
            
            for(int ctr = 0; ctr < ligandConcArray.length; ctr++)
            {
                double L0 = ligandConcArray[ctr];
                double P0 = receptorConcArray[ctr];
                
                value.setEntry(ctr, calcModel(P0, L0, kd, dw));
                
                jacobian.setEntry(ctr, 0, calcModelDerivativeKd(P0, L0, kd, dw));
                jacobian.setEntry(ctr, 1, calcModelDerivativeDw(P0, L0, kd));
            }
            
            return new Pair<>(value, jacobian);
        };
        
        // iniial guess is 10 uM with a cumulative shift of 5. i might try to dw 
        //  change based on number of residues. maybe not
        final double[] startingGuess = {10, 5};
        
        LeastSquaresOptimizer.Optimum optimum = buildAndGetOptimum(startingGuess, function, cumCSPsArray);
        
        double kd =  optimum.getPoint().getEntry(0);   
        
        double dwMax =  optimum.getPoint().getEntry(1);  // at fully bound
        double dwAtHighestPoint = cumCSPsArray[cumCSPsArray.length - 1];
        
        double[][] presentationFit = makeArrayOfPresentationFit(ligandConcArray, 
                                                                receptorConcArray, 
                                                                kd, 
                                                                dwMax, 
                                                                cumCSPsArray);
        
        
        
                
        return CumResults.makeCumResults(kd, dwAtHighestPoint / dwMax, presentationFit);
    
    }
    
    public static double fitDwForAResidue(double[] ligandConcArray,
                                          double[] receptorConcArray,
                                          double[] cspArray,
                                          double kdFromCumData)
    {
        // TODO add code to make sure arrays are all same size
        
        
        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            final double dw = paramPoint.getEntry(0);
            
            RealVector value = new ArrayRealVector(ligandConcArray.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcArray.length, 2);
            
            for(int ctr = 0; ctr < ligandConcArray.length; ctr++)
            {
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
    
    
    private static LeastSquaresOptimizer.Optimum buildAndGetOptimum(double[] startingGuess, 
                                                                    MultivariateJacobianFunction function, 
                                                                    double[] target)
    {
        LeastSquaresProblem problem = new LeastSquaresBuilder().
                                start(startingGuess).
                                model(function).
                                target(target).
                                lazyEvaluation(false).
                                maxEvaluations(10000).
                                maxIterations(10000).
                                build();
        
        return  new LevenbergMarquardtOptimizer().optimize(problem);
    }
    
    private static double[][] makeArrayOfPresentationFit(double[] ligandConcArray, 
                                                         double[] receptorConcArray, 
                                                         double kd, 
                                                         double dwMax,
                                                         double[] cumCSPsArray)
    {    
        double [][] presentationFit = new double[ligandConcArray.length][2];
        
        for(int ctr = 0; ctr < ligandConcArray.length; ctr++)
        {
            presentationFit[ctr][0] = ligandConcArray[ctr] / receptorConcArray[ctr];
        }
        
        for(int ctr = 0; ctr < cumCSPsArray.length; ctr++)

        {
            presentationFit[ctr][1] = 
                calcModel(receptorConcArray[ctr], ligandConcArray[ctr], kd, dwMax) / dwMax;
        }
        return presentationFit;
    }
    
    private static double calcModel(double P0, 
                                    double L0, 
                                    double kd, 
                                    double dw)
        {
            // test of this equation worked using known values. unlikely issue is here
            return dw / (2 * P0) * ((kd + L0 + P0) - Math.sqrt(Math.pow(kd + L0 + P0, 2) - 4 * P0 * L0));
        }
    
    private static double calcModelDerivativeDw(double P0, 
                                                double L0, 
                                                double kd) 
    {
        return 1 / (2 * P0) * ((kd + L0 + P0) - Math.sqrt(Math.pow(kd + L0 + P0, 2) - 4 * P0 * L0));
    }
    
    private static double calcModelDerivativeKd(double P0, 
                                                double L0, 
                                                double kd, 
                                                double dw)
    {
        return dw / (2 * P0) * (1 - ((kd + L0 + P0) / Math.sqrt((Math.pow(kd + L0 + P0, 2) - 4 * L0 * P0))));
    }        
} // end class LeastSquaresFitter

    


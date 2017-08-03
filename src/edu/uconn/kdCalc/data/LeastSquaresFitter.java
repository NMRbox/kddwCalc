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
        List<Double> ligandConcList = series.getLigandConcList();
        List<Double> receptorConcList = series.getReceptorConcList();
        
        double[] cumCSPs = series.getCumulativeShifts();
        
        CumResults cumResultsObject = fitCumulativeData(ligandConcList, receptorConcList, cumCSPs);
        
        double kd = cumResultsObject.getKd();
        double percentBound = cumResultsObject.getPercentBound();
        
        double[] boundCSPArrayByResidue =
                     series.getTitrationSeries().parallelStream() // now have Stream<Titration>
                               .mapToDouble((Titration titr) -> 
                               {    return LeastSquaresFitter.fitDwForAResidue(ligandConcList, 
                                                                               receptorConcList, 
                                                                               titr.getCSPsByResidueArray(), 
                                                                               kd);
                               })
                               .toArray(); 
        
        
        return Results.makeResultsObject(kd, percentBound, boundCSPArrayByResidue);
    }
        
        
        
    
    
    public static CumResults fitCumulativeData(List<Double> ligandConcList,
                                               List<Double> receptorConcList,
                                               double[] cumCSPsArray)
    {
        
        // TODO add code to make sure arrays are all same size
        
        
        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            double kd = paramPoint.getEntry(0);
            double dw = paramPoint.getEntry(1);
            
            RealVector value = new ArrayRealVector(ligandConcList.size());
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcList.size(), 2);
            
            for(int ctr = 0; ctr < ligandConcList.size(); ctr++)
            {
                double L0 = ligandConcList.get(ctr);
                double P0 = receptorConcList.get(ctr);
                
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
        
        return CumResults.makeCumResults(optimum.getPoint().getEntry(0), 
                                         cumCSPsArray[cumCSPsArray.length - 1] / optimum.getPoint().getEntry(1));
    
    }
    
    public static double fitDwForAResidue(List<Double> ligandConcList,
                                          List<Double> receptorConcList,
                                          double[] cspArray,
                                          double kdFromCumData)
    {
        // TODO add code to make sure arrays are all same size
        
        
        MultivariateJacobianFunction function = (final RealVector paramPoint) -> {
            final double dw = paramPoint.getEntry(0);
            
            RealVector value = new ArrayRealVector(ligandConcList.size());
            RealMatrix jacobian = new Array2DRowRealMatrix(ligandConcList.size(), 2);
            
            for(int ctr = 0; ctr < ligandConcList.size(); ctr++)
            {
                double L0 = ligandConcList.get(ctr);
                double P0 = receptorConcList.get(ctr);
                
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
}

    


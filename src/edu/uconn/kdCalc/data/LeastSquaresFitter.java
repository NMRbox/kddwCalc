package edu.uconn.kdCalc.data;

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
            
            return new Pair<RealVector, RealMatrix>(value, jacobian);
        };
        
        LeastSquaresProblem problem = new LeastSquaresBuilder().
                                start(new double[] {10, 5}).
                                model(function).
                                target(cumCSPsArray).
                                lazyEvaluation(false).
                                maxEvaluations(10000).
                                maxIterations(10000).
                                build();
        
        LeastSquaresOptimizer.Optimum optimum = new LevenbergMarquardtOptimizer().optimize(problem);
        
        return CumResults.makeCumResults(optimum.getPoint().getEntry(0), 
                                         cumCSPsArray[cumCSPsArray.length - 1] / optimum.getPoint().getEntry(1));
    
    }
    
    private static double calcModel(double P0, double L0, double kd, double dw)
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
}

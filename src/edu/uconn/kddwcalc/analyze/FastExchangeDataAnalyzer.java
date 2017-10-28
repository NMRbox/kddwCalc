package edu.uconn.kddwcalc.analyze;

import edu.uconn.kddwcalc.fitting.Results;
import edu.uconn.kddwcalc.fitting.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.data.Titration;
import edu.uconn.kddwcalc.fitting.ResultsTwoParamKdMaxObs;
import edu.uconn.kddwcalc.gui.RawData;
import java.io.IOException;

/**
 *
 * @author Rizzo
 */
public class FastExchangeDataAnalyzer {
    
    /**
     * Takes the user data in a {@link RawData} object, analyzes it and outputs the results to disk
     * 
     * @param rawDataInstance contains data from the user input
     * @throws IOException if unable to read/write files
     * @throws ArraysInvalidException if data is duplicated or arrays have different lengths (they cant)
     */
    public static void analyze(RawData rawDataInstance) 
        throws IOException, ArraysInvalidException {
        
        // sorts data from user and creates a TitrationSeries object
        TitrationSeries series = FactoryMaker.createFactory(rawDataInstance.getType()) // returns AbsFactory subclass
                                             .analyzeDataFiles(rawDataInstance); // returns TitrationSeries

        // prints sorted data to disk
        series.printTitrationSeries(rawDataInstance.getDataOutputFile());
        
        ResultsTwoParamKdMaxObs twoParamResults = LeastSquaresFitter.fitTwoParamKdAndMaxObs(series);
        

        double[] boundCSPArrayByResidue =
                     series.getTitrationSeries()
                           .stream() // now have Stream<Titration>
                           .mapToDouble((Titration titr) ->  {
                               return LeastSquaresFitter.fitOneParamMaxObs(series.getLigandConcs(), 
                                                                           series.getReceptorConcs(), 
                                                                           titr.getObservables(), 
                                                                           twoParamResults.getKd());
                           })
                           .toArray();
        
        Results finalResults = 
            Results.makeResultsObject(twoParamResults.getKd(), 
                                      series.getObservables()[series.getObservables().length - 1]
                                        / twoParamResults.getMaxObservable(), 
                                      boundCSPArrayByResidue, 
                                      twoParamResults.getPresentationFit());
        
        
        finalResults.writeResultsToDisk(rawDataInstance.getResultsFile());
        
        
                          
        
        
    }
    
    
    
}



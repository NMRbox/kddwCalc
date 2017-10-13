/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uconn.kddwcalc.sorting;

import edu.uconn.kddwcalc.sorting.FactoryMaker;
import edu.uconn.kddwcalc.fitting.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.gui.RawData;
import java.io.IOException;

/**
 *
 * @author Rizzo
 */
public class FastExchangeDataAnalyzer {
    
    private final RawData rawDataInstance;
    
    private FastExchangeDataAnalyzer(RawData rawDataInstance) {
        
        this.rawDataInstance = rawDataInstance;
    }
    
    /**
     * Takes the user data in a {@link RawData} object, analyzes it and outputs the results to disk
     * 
     * @param rawDataInstance contains data from the user input
     * @throws IOException if unable to read/write files
     * @throws ArraysInvalidException if data is duplicated or arrays have different lengths (they cant)
     */
    public static void analyze(RawData rawDataInstance) 
        throws IOException, ArraysInvalidException {
        
        TitrationSeries series = FactoryMaker.createFactory(rawDataInstance.getType()) // returns AbsFactory subclass
                                             .analyzeDataFiles(rawDataInstance); // returns TitrationSeries

        series.printTitrationSeries(rawDataInstance.getDataOutputFile());
        
        LeastSquaresFitter.fit(series) // returns Results object
                          .writeResultsToDisk(rawDataInstance.getResultsFile()); // calls methods of class Results
                          
        
        
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uconn.kddwcalc.gui;

import edu.uconn.kddwcalc.data.ArraysInvalidException;
import edu.uconn.kddwcalc.data.FactoryMaker;
import edu.uconn.kddwcalc.data.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.data.TypesOfTitrations;
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
    
    public static void analyze(RawData rawDataInstance) 
        throws IOException, ArraysInvalidException {
        
        TitrationSeries series = FactoryMaker.createFactory(rawDataInstance.getType()) // returns AbsFactory subclass
                                             .analyzeDataFiles(rawDataInstance); // returns TitrationSeries

        series.printTitrationSeries(rawDataInstance.getDataOutputFile());
        
        LeastSquaresFitter.fit(series) // returns Results object
                          .writeResultsToDisk(rawDataInstance.getResultsFile()); // calls methods of class Results
                          
        
        
    }
    
    
}

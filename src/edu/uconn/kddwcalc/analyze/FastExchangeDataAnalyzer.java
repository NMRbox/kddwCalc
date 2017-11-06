package edu.uconn.kddwcalc.analyze;

import edu.uconn.kddwcalc.fitting.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.fitting.ResultsKdAndMaxObs;
import edu.uconn.kddwcalc.gui.RawData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author Rizzo
 */
public class FastExchangeDataAnalyzer {
    
    private static final String TWO_PARAM_BY_RESIDUE_DIRECTORY = "resultsByResidueTwoParamFit";   
    private static final String ONE_PARAM_BY_RESIDUE_DIRECTORY = "resultsByResidueKdFixed";
    
    
    /**
     * Takes the user data in a {@link RawData} object, analyzes it and outputs the results to disk
     * 
     * @param rawDataInstance contains data from the user input
     * @throws IOException if unable to read/write files
     * @throws ArraysInvalidException if data is duplicated or arrays have different lengths (they cant)
     */
    public static void analyze(RawData rawDataInstance) 
        throws IOException, ArraysInvalidException {
        
        // figures out which Factory subclass to instantiate and then sorts
        //  
        TitrationSeries series = 
            FactoryMaker.createFactory(rawDataInstance.getType()) // returns AbsFactory
                        .sortDataFiles(rawDataInstance); // returns TitrationSeries

        // prints sorted data to disk
        series.printTitrationSeries(rawDataInstance.getDataOutputFile());
        
        // gets an object back containing Kd, max observable, and the presentation fit
        //    using the cumulative data
        ResultsKdAndMaxObs aggTwoParamResults = 
            LeastSquaresFitter.fitTwoParamKdAndMaxObs(series);
        
        // uses the Kd from the aggregate two parameter fit and 
        //     finds the max observable in a one param fit (Kd fixed)
        double kd = aggTwoParamResults.getKd();
        List<ResultsKdAndMaxObs> resultsByResidueFixedKd =
            getResultsByResidueFixedKd(kd, series);
        
        // does a two parameter fitting for each residue
        List<ResultsKdAndMaxObs> twoParamResultsByResidue =
            getTwoParamResultsByResidue(series);
        
        writeResultsToDisk(aggTwoParamResults,
                           resultsByResidueFixedKd, 
                           twoParamResultsByResidue,
                           rawDataInstance.getResultsFile());
        
    } // end method Analyze
    
    // 
    private static void writeResultsToDisk(ResultsKdAndMaxObs aggTwoParamResults,
                                           List<ResultsKdAndMaxObs> resultsByResidueFixedKd,
                                           List<ResultsKdAndMaxObs> twoParamResultsList,
                                           File resultsFile) throws FileNotFoundException, IOException {
        
        try {
            
            writeCumulativeResults(resultsFile,
                                   aggTwoParamResults,
                                   resultsByResidueFixedKd);
            
            writeTwoParamResultsByResidue(resultsFile,
                                          twoParamResultsList,
                                          TWO_PARAM_BY_RESIDUE_DIRECTORY);
            
            writeTwoParamResultsByResidue(resultsFile,
                                          resultsByResidueFixedKd,
                                          ONE_PARAM_BY_RESIDUE_DIRECTORY);
            
            writeKdsForEachResidueToSingleFile(resultsFile,
                                               twoParamResultsList);
            
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("Was an issue opening the file to write results");
        }
        
    }

    private static List<ResultsKdAndMaxObs> getTwoParamResultsByResidue(TitrationSeries series) {
        return series.getListOfTitrations()
                     .stream() // have Stream<Titration>
                     .map(LeastSquaresFitter::fitTwoParamKdAndMaxObs)
                     .collect(Collectors.toList());
    }
    
    /**
     */
    private static void writeCumulativeResults(File resultsFile,
                                               ResultsKdAndMaxObs aggTwoParamResults,
                                               List<ResultsKdAndMaxObs> resultsByResidueKdFixed) 
                                    throws FileNotFoundException, IOException {
        
        try (Formatter output = new Formatter(resultsFile)) {
            
            output.format("Results from cumulative fit:%n%n%s%n%n", 
            aggTwoParamResults.toString());
        
            output.format("dw for fully bound:%n");    
            
            resultsByResidueKdFixed.stream()
              .forEach(result -> output.format("%.6f%n", result.getMaxObservable()));
        }  
        
        // create a new path below where results file goes
        Path newPath = Paths.get(resultsFile.toPath().getParent().toString(), "finalFit.png");
        
        
        aggTwoParamResults.writeFitImageToDisk(newPath.toFile());
        
        
    } // end writeCumulativeResults
    /**
     * 
     * @param resultsFile
     * @param twoParamResultsList 
     */
    
    private static void writeTwoParamResultsByResidue(File resultsFile, 
                                                      List<ResultsKdAndMaxObs> twoParamResultsList,
                                                      String newDirectoryName) 
                                                throws IOException {
        
        // get the absolute path to where final results are written
        Path path = resultsFile.getAbsoluteFile().toPath();
        
        // create a new path below where results file goes
        Path newPath = Paths.get(path.getParent().toString(), newDirectoryName);
        
        // if this program was already run once, then the output data might
        // have already been written. this begins the process of deleting that data
        // by getting an array of the files inside
        if (Files.exists(newPath)) {
            Path[] oldFiles = Files.list(newPath).toArray(Path[]::new);
        
            // take array of old files and delete
            for (Path pth : oldFiles) {
                Files.delete(pth);
            } 
            // delete the directory ending in "individualResidueData" if program
            // was already executed
            Files.deleteIfExists(newPath);
        }
        
        
        
        // create directory where individual residue results will go
        Files.createDirectory(newPath);
        
        // iterate through the individual residue data and write the disk
        for (int ctr = 0; ctr < twoParamResultsList.size(); ctr++) {
            
            try (Formatter output = new Formatter(Paths.get(newPath.toFile().getAbsolutePath(), 
                                                  String.format("%s.txt", ctr)).toFile())) {
                
                output.format("%s", String.format(twoParamResultsList.get(ctr).toString())); 
            }
            
            twoParamResultsList.get(ctr)
                               .writeFitImageToDisk(Paths.get(newPath.toFile().getAbsolutePath(), 
                                                  String.format("%sFit.png", ctr)).toFile());
        }
    } // end method writeTwoParamResultsByResidue 

    
    private static void writeKdsForEachResidueToSingleFile(File resultsFile, 
                                                           List<ResultsKdAndMaxObs> twoParamResultsList) 
                                                        throws FileNotFoundException {
        
        // create a new path below where results file goes
        Path newPath = Paths.get(resultsFile.toPath().getParent().toString(), "KdsByResidue.txt");
        
        try (Formatter output = new Formatter(newPath.toFile())) {
            
            output.format(String.format("Kds for each residue. Note that these "
                + "are not weighted by the magnitude of the CSP, so some "
                + "values that seem way off may be subject to greater noise "
                + "relative to signal%n%n"));
            
            output.format(String.format("%10s%10s%n", "Kd (uM)", "dw" ));
            
            twoParamResultsList.stream()
                               .forEach(result -> {
                                   output.format(String.format("%10.2f%10.6f%n",
                                       result.getKd(), result.getMaxObservable()));
                               });
        }
   }

    private static List<ResultsKdAndMaxObs> getResultsByResidueFixedKd(double kd, TitrationSeries series) {
        
        return series.getListOfTitrations()
                     .stream()
                     .map(titr -> LeastSquaresFitter.fitOneParamMaxObs(titr, kd))
                     .collect(Collectors.toList());
                     
   }

   
} // end class FastExchangeDataAnalyzer

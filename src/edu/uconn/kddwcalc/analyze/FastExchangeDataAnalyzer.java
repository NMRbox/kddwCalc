package edu.uconn.kddwcalc.analyze;

import edu.uconn.kddwcalc.fitting.LeastSquaresFitter;
import edu.uconn.kddwcalc.data.TitrationSeries;
import edu.uconn.kddwcalc.fitting.ResultsKdAndMaxObs;
import edu.uconn.kddwcalc.gui.RawData;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Static method <code>Analyze</code> take the data, sorts it, creates the results,
 *  and then prints all results to disk.
 *  
 * 
 * @author Rizzo
 */
public class FastExchangeDataAnalyzer {
    
    private static final String TWO_PARAM_BY_RESIDUE_DIRECTORY = "resultsByResidueTwoParamFit";   
    private static final String ONE_PARAM_BY_RESIDUE_DIRECTORY = "resultsByResidueKdFixed";
    private static final String FINAL_CUMULATIVE_TXT_RESULTS = "finalResults.txt";
    private static final String FINAL_CUMULATIVE_FIT = "finalFit.png";
    private static final String LIST_TWO_PARAM_FIT_BY_RESIDUE = "listByResidueTwoParamFit.txt";
    private static final String IDENTIFIER_MATCHING_LIST = "residueListWithFreePeaks.txt";
    
    
    /**
     * Takes the user data in a {@link RawData} object, analyzes it and outputs the results to disk
     * 
     * @param rawDataInstance contains data from the user input
     * @throws IOException if unable to read/write files
     * @throws ArraysInvalidException if data is duplicated or arrays have different lengths (they cant)
     */
    public static void analyze(RawData rawDataInstance) 
        throws IOException, ArraysInvalidException {
        
        Path resultsOverallDirectory = 
            rawDataInstance.getResultsDirectoryFile().toPath().toAbsolutePath();
        if (Files.exists(resultsOverallDirectory)) {
            emptyResultsDirectory(resultsOverallDirectory);
        }
        Files.createDirectory(resultsOverallDirectory);
        
        // figures out which Factory subclass to instantiate and then sorts
        TitrationSeries series = 
            FactoryMaker.createFactory(rawDataInstance.getType()) // returns AbsFactory
                        .sortDataFiles(rawDataInstance); // returns TitrationSeries

        // prints sorted data to disk
        series.printTitrationSeries(resultsOverallDirectory);
        
        // gets an object back containing Kd, max observable, and the presentation fit
        //    using the cumulative data
        ResultsKdAndMaxObs aggTwoParamResults = 
            LeastSquaresFitter.fitTwoParamKdAndMaxObs(series);
        
        // uses the Kd from the aggregate two parameter fit and 
        //     finds the max observable in a one param fit (Kd fixed)
        double kd = aggTwoParamResults.getKd();
        List<ResultsKdAndMaxObs> resultsByResidueFixedKd =
            getResultsByResidueFixedKd(kd, series);
        
        // does a two parameter fitting for each residue and returns the 
        // results in a list 
        List<ResultsKdAndMaxObs> twoParamResultsByResidue =
            getTwoParamResultsByResidue(series);
        
        writeResultsToDisk(aggTwoParamResults,
                           resultsByResidueFixedKd, 
                           twoParamResultsByResidue,
                           resultsOverallDirectory);
        
    } // end method Analyze
    
    // writes all results to disk. 
    private static void writeResultsToDisk(ResultsKdAndMaxObs aggTwoParamResults,
                                           List<ResultsKdAndMaxObs> resultsByResidueFixedKd,
                                           List<ResultsKdAndMaxObs> twoParamResultsList,
                                           Path resultsOverallDirectory) 
                                        throws FileNotFoundException, IOException {
        
            writeCumulativeResults(resultsOverallDirectory,
                                   aggTwoParamResults,
                                   resultsByResidueFixedKd);
            
            writeKdsForEachResidueToSingleFile(resultsOverallDirectory,
                                               twoParamResultsList);
            
            writeTwoParamResultsByResidue(resultsOverallDirectory,
                                          twoParamResultsList,
                                          TWO_PARAM_BY_RESIDUE_DIRECTORY);
            
            writeTwoParamResultsByResidue(resultsOverallDirectory,
                                          resultsByResidueFixedKd,
                                          ONE_PARAM_BY_RESIDUE_DIRECTORY);  
    }

    private static List<ResultsKdAndMaxObs> getTwoParamResultsByResidue(TitrationSeries series) {
        return series.getListOfTitrations()
                     .stream() // have Stream<Titration>
                     .map(LeastSquaresFitter::fitTwoParamKdAndMaxObs)
                     .collect(Collectors.toList());
    }
    
    /**
     * 
     * @param resultsOverallDirectory
     * @param aggTwoParamResults
     * @param resultsByResidueKdFixed 
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static void writeCumulativeResults(Path resultsOverallDirectory,
                                               ResultsKdAndMaxObs aggTwoParamResults,
                                               List<ResultsKdAndMaxObs> resultsByResidueKdFixed) 
                                    throws FileNotFoundException, IOException {
        
        Path thisPath = Paths.get(resultsOverallDirectory.toAbsolutePath().toString(),
                                  FINAL_CUMULATIVE_TXT_RESULTS);
        
        
        try (Formatter output = new Formatter(thisPath.toFile())) {
            
            output.format("Results from cumulative fit:%n%n%s%n%n", 
            aggTwoParamResults.toString());
        
            output.format("dw for fully bound using global Kd:%n");    
            
            resultsByResidueKdFixed.stream()
              .forEach(result -> output.format("%4s:   %.6f%n", 
                  result.getIdentifier(), result.getMaxObservable()));
        }  
        
        aggTwoParamResults.writeFitImageInPassedPath(Paths.get(
            resultsOverallDirectory.toAbsolutePath().toString(), 
            FINAL_CUMULATIVE_FIT));
    } // end writeCumulativeResults
    
    /**
     * 
     * @param resultsOverallDirectory the directory enclosing newDirectoryName
     * @param twoParamResultsList the results to write out
     * @param newDirectoryName subdirectory name where results will be written into
     * 
     * @throws IOException 
     */
    private static void writeTwoParamResultsByResidue(Path resultsOverallDirectory, 
                                                      List<ResultsKdAndMaxObs> twoParamResultsList,
                                                      String newDirectoryName) 
                                                throws IOException {
        
        Path subDirectory = Paths.get(resultsOverallDirectory.toAbsolutePath().toString(),
                                      newDirectoryName);
        
        Files.createDirectory(subDirectory);
        
        twoParamResultsList.stream()
                           .forEach(result -> { 
                               try {
                                   result.writeFitAndTextToDisk(subDirectory);
                               }
                               catch (IOException e) {
                                   System.out.println("Issue writing fit image to disk");
                               }
                           });
        
    } // end method writeTwoParamResultsByResidue 

    
    /**
     * Writes the Kd and maximum observable at fully bound into a single file.
     * 
     * @param resultsOverallDirectory where to write the data
     * @param twoParamResultsList the results to write out
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private static void writeKdsForEachResidueToSingleFile(Path resultsOverallDirectory, 
                                                           List<ResultsKdAndMaxObs> twoParamResultsList) 
                                                        throws FileNotFoundException, IOException {
        
        // create a new path below where results file goes
        Path newPath = Paths.get(resultsOverallDirectory.toAbsolutePath().toString(), 
                                 LIST_TWO_PARAM_FIT_BY_RESIDUE);
        
        try (Formatter output = new Formatter(newPath.toFile())) {
            
            output.format(String.format("Kds for each residue. Note that these "
                + "are not weighted by the magnitude of the CSP, so some "
                + "values that seem way off may be subject to greater noise "
                + "relative to signal%n%n"));
            
            output.format(String.format("%4s%11s%10s%n","ID", "Kd (uM)", "dw" ));
            
            twoParamResultsList.stream()
                               .forEach(result -> {
                                   output.format(String.format("%4s:%10.2f%10.6f%n",
                                       result.getIdentifier(), 
                                       result.getKd(), 
                                       result.getMaxObservable()));
                               });
        }
   }

    /**
     * Takes the global Kd from the cumulative fit and fits each residue on an
     * individual basis with the maximum observable at fully bound as the only
     * parameter
     * 
     * @param kd the Kd from the global fit that will be used in one param fitting
     * @param series the data to fit
     * @return 
     */
    private static List<ResultsKdAndMaxObs> getResultsByResidueFixedKd(double kd, TitrationSeries series) {
        
        return series.getListOfTitrations()
                     .stream()
                     .map(titr -> LeastSquaresFitter.fitOneParamMaxObs(titr, kd))
                     .collect(Collectors.toList());
                     
   }

    /**
     * Deletes previous results if the user has chosen to write results into a
     * directory that already exists.
     * 
     * @param resultsOverallDirectory the directory containing previous results
     *                                that will be deleted
     * 
     * @throws IOException if an Exception occurs during deletion
     */
    private static void emptyResultsDirectory(Path resultsOverallDirectory) 
        throws IOException {
    
        Files.walkFileTree(resultsOverallDirectory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    // directory iteration failed
                    throw e;
                }
            }
        });
    }
} // end class FastExchangeDataAnalyzer

package edu.uconn.kddwcalc.fitting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Formatter;
import java.util.concurrent.ExecutorService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

/**
 * A class which holds results from a two parameter fit of binding data. The two
 * parameters are (1) Kd and (2) the maximum observable at fully bound.
 * 
 * @author Alex R.
 */
public class ResultsKdAndMaxObs {
    
    private final double kd; 
    private final double maxObservable;
    private final double[] expObservables;
    private final double[][] presentationFit;
    private final String identifier;

    /**
     * Initializes an instance of AggResults with the results of least squares fitting of the cumulative CSP data
     * 
     * @param kd the affinity constant (a parameter from fitting)
     * @param maxObservable the maximum observable at fully bound (parameter from fitting)
     * @param expObservables experimental data expressed as (Point - Point0)
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * 
     * @see #makeCumResults()
     */
    private ResultsKdAndMaxObs(double kd, 
                               double maxObservable,
                               double[] expObservables,
                               double[][] presentationFit,
                               String identifier) {
        this.kd = kd;
        this.maxObservable = maxObservable;
        this.expObservables = expObservables;
        this.presentationFit = presentationFit;
        this.identifier = identifier;

    }

    /**
     * Static simple factory that performs validations of the data before call 
     * the constructor
     * 
     * @param kd the affinity constant
     * @param maxObservable the percentage of receptor bound at the highest point
     * @param expObservables an array of experimental data as (point - point0)
     * @param presentationFit [x,y] coordinates as [ligand ratio, percent bound] for publication figure
     * @param identifier a number gives to each residue. its not the assigned residue number
     * 
     * @see LeastSquaresFitter#fitTwoParamKdAndMaxObs
     * 
     * @return an instance of <code>AggResults</code>, (most likely for further processing)
     */
    public static ResultsKdAndMaxObs makeTwoParamResults(double kd, 
                                                         double maxObservable,
                                                         double[] expObservables,
                                                         double[][] presentationFit,
                                                         String identifier) {
        if (kd < 0 || maxObservable  < 0)
            throw new IllegalArgumentException("kd < 0 or percentBound < 0 (CumResults)");

        // TODO code validation for presentationFit a double[][]. must all be positive

        return new ResultsKdAndMaxObs(kd, 
                                      maxObservable, 
                                      expObservables,
                                      presentationFit,
                                      identifier); 
    }

    /**
     * Gets the Kd (affinity constant) for the interaction. This is one of the
     * parameters from fitting.
     * 
     * @return a <code>double</code> value representing the Kd
     */
    public double getKd() {
        return kd;
    }

    /**
     * Gets the maximum observable point if the fully bound state was reached. This
     * is a parameter from the fitting (e.g. dw or delta-omega)
     * 
     * @return a <code>double</code> value representing the percent bound (0-1)
     */
    public double getMaxObservable() {
        return maxObservable;
    }

    /**
     * A way to view the data that would be used in a publication figure
     * 
     * @return a three-column <code>double</code> matrix with ligand ratio on x-axis 
     * and percent bound on the y-axis.
     * 
     * Includes experimental and fitted points to plot how well the model
     * fits the data.
     */
    public double[][] getPresentationFit() {
        return presentationFit;
    }

    @Override
    public String toString() {
        
        StringBuilder string = new StringBuilder();
        
        string.append(String.format("Titration: %s%n", getIdentifier()));
        
        string.append(String.format("kd = %.2f uM%n", getKd()));
        
        string.append(String.format("percent bound at final point = %.1f%n%n", 
                expObservables[expObservables.length - 1] / maxObservable * 100));
            
        string.append(String.format("Presentation fit:%n"));    
        string.append(String.format("%15s%15s%15s%n","ligand ratio", 
                                                     "model point", 
                                                     "exp point"));
            
        for(int ctr = 0; ctr < presentationFit.length; ctr++) {
            for(int ctr2 = 0; ctr2 < presentationFit[ctr].length; ctr2++) {
                string.append(String.format("%15f", presentationFit[ctr][ctr2]));
            }
            string.append(String.format("%n"));
        }  
        
        return string.toString();
    }
    
    public void writeFitImageInPassedPath(Path path) throws IOException {
        // System.out.println("works"); called ~152 times
        WritableImage image = getAsWritableImage();
        // System.out.println("still working"); called once
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", path.toFile());
    } 
    
    public void writeFitImageToDisk(Path path) throws IOException {
        
        Path filePath = Paths.get(path.toAbsolutePath().toString(),
                                  String.format("%s.png", getIdentifier()));
        
        writeFitImageInPassedPath(filePath);
    }
    
    public void writeTextResultsToDisk(Path path) throws FileNotFoundException {
        
        try (Formatter output = 
            new Formatter(Paths.get(path.toAbsolutePath().toString(), 
                                    String.format("%s.txt", getIdentifier()))
                          .toFile())){
                
            output.format("%s", String.format(toString())); 
        }
    }
    
    /**
     * 
     * @return the number associated with this residue. was added in AbsFactory
     * and probably does not represent the assigned residue number
     */
    public String getIdentifier() {
        return identifier;
    }

    private WritableImage getAsWritableImage() {
        try {    
            // for x-axis
            double maxLigandRatio = 
                getPresentationFit()[getPresentationFit().length - 1][0];

            // define axes, round so x-axis is a little bigger than maxLigandRadio
            final NumberAxis xAxis = 
                new NumberAxis(0, Math.ceil(maxLigandRatio), 0.5);
            final NumberAxis yAxis = 
                new NumberAxis(0, 1, 0.25);
            xAxis.setLabel("Ligand/Protein ratio");
            yAxis.setLabel("Percent bound");

            //creating the chart
            final LineChart<Number,Number> lineChart = 
                    new LineChart<>(xAxis,yAxis);

            // if this is true, then the scene is only partially printed
            lineChart.setAnimated(false);

            XYChart.Series expSeries = new XYChart.Series();
            XYChart.Series modelSeries = new XYChart.Series();


            Arrays.stream(presentationFit)
                  .forEach( (double[] line) -> {
                      expSeries.getData().add(new XYChart.Data(line[0], line[2]));
                      modelSeries.getData().add(new XYChart.Data(line[0], line[1]));
                  });//}); 


            Scene scene  = new Scene(lineChart, 660, 600);

            scene.getStylesheets().add(
                getClass().getResource("chartStyleSheet.css").toExternalForm());

            lineChart.getData().add(expSeries);
            lineChart.getData().add(modelSeries);

            return lineChart.snapshot(new SnapshotParameters(), null);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        throw new AssertionError();
    } // end method getAsWritableImage
    
    
    
    public void writeFitAndTextToDisk(Path path) throws IOException {
        writeFitImageToDisk(path);
        writeTextResultsToDisk(path);
    }
    
    
}


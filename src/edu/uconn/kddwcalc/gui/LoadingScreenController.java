package edu.uconn.kddwcalc.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * FXML controller class for the loading screen which is displayed on launch
 *
 * @author Alex Ri.
 */
public class LoadingScreenController implements Initializable {
    
    private static final int KEYFRAME_DURATION = 900;
    
    @FXML ProgressBar loadingProgressBar;
    @FXML Label loadingLabel;
    
    private final Timeline loadingTimeline = new Timeline();

    /**
     * Initializes the loading screen controller class. Runs a Timeline with a few {@link KeyFrame} objects
     * and an image
     *
     * @param url a url
     * @param rb resource
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        final Timeline loadingTimeline = new Timeline();
        
        loadingTimeline.setCycleCount(1);
        loadingTimeline.setAutoReverse(false);
        
        
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(KEYFRAME_DURATION * 1),
                                          new KeyValue(loadingLabel.textProperty(), "Loading packages..."),
                                          new KeyValue(loadingProgressBar.progressProperty(), 0.15, Interpolator.DISCRETE));
        
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(KEYFRAME_DURATION * 2),
                                          new KeyValue(loadingLabel.textProperty(), "Turning on modules..."),
                                          new KeyValue(loadingProgressBar.progressProperty(), 0.300, Interpolator.DISCRETE));
        
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(KEYFRAME_DURATION * 3),
                                          new KeyValue(loadingLabel.textProperty(), "Creating GUI..."),
                                          new KeyValue(loadingProgressBar.progressProperty(), 0.45, Interpolator.DISCRETE));
        
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(KEYFRAME_DURATION * 4),
                                          new KeyValue(loadingLabel.textProperty(), 
                                              "Loading Apache Commons Mathematics Library..."),
                                          new KeyValue(loadingProgressBar.progressProperty(), 0.60, Interpolator.DISCRETE));
        
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(KEYFRAME_DURATION * 6),
                                          new KeyValue(loadingLabel.textProperty(), "Starting..."),
                                          new KeyValue(loadingProgressBar.progressProperty(), 0.90, Interpolator.DISCRETE));
        
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(KEYFRAME_DURATION * 6.8),
                                          new KeyValue(loadingLabel.textProperty(), ""),
                                          new KeyValue(loadingProgressBar.progressProperty(), 1.0, Interpolator.DISCRETE));
        

       
        loadingTimeline.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3,
                                              keyFrame4, keyFrame5, keyFrame6);
        
        loadingTimeline.playFromStart();
    }   
    
    /**
     * Gets the {@link Timeline} for the loading screen
     * 
     * @return for the loading screen
     */
    public Timeline getLoadingTimeline() {
        return loadingTimeline;
    }
    
}

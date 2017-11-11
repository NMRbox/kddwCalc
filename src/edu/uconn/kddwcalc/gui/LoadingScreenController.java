package edu.uconn.kddwcalc.gui;


//  https://pixabay.com/en/schrecksee-allgäu-hochgebirgssee-2534484
//  website where image on loading screen was taken


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
        
        
        KeyFrame keyFrame1 = makeKeyFrame(1.0, "Loading packages...", 0.15);
        
        KeyFrame keyFrame2 = makeKeyFrame(2.0, "Initializing modules...", 0.3);
        
        KeyFrame keyFrame3 = makeKeyFrame(3.0, "Creating GUI...", 0.45);
        
        KeyFrame keyFrame4 = makeKeyFrame(4.0, "Loading Apache Commons Mathematics Library...", 0.6);
        
        KeyFrame keyFrame5 = makeKeyFrame(6.0, "Starting...", 0.90);
        
        KeyFrame keyFrame6 = makeKeyFrame(6.8, "", 1.0);
        
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
    
    private KeyFrame makeKeyFrame(double duration, String text, double loadingProgress) {
        return new KeyFrame(Duration.millis(KEYFRAME_DURATION * duration),
                               new KeyValue(loadingLabel.textProperty(), text),
                               new KeyValue(loadingProgressBar.progressProperty(), 
                                            loadingProgress, Interpolator.DISCRETE));
    }
    
}

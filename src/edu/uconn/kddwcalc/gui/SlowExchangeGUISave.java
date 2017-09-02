package edu.uconn.kddwcalc.gui;

import edu.uconn.kddwcalc.data.TypesOfTitrations;
import java.io.File;
import java.io.Serializable;
import java.util.List;



public class SlowExchangeGUISave implements Serializable {
    
    private final TypesOfTitrations typeOfTitr;
    private final boolean resonanceReversal;
    private final double multiplier;
    private final File outputDataFile;
    private final File outputResultsFile;
    private final List<File> fileList;
    private final List<Double> ligandConcs;
    private final List<Double>receptorConcs;
    
    private SlowExchangeGUISave(TypesOfTitrations typeOfTitr,
                                boolean resonanceReversal,
                                double multiplier,
                                File outputDataFile,
                                File outputResultsFile,
                                List<File> fileList,
                                List<Double> ligandConcs,
                                List<Double> receptorConcs) {
        
        this.typeOfTitr = typeOfTitr;
        this.resonanceReversal = resonanceReversal;
        this.multiplier = multiplier;
        this.outputDataFile = outputDataFile;
        this.outputResultsFile = outputResultsFile;
        this.fileList = fileList;
        this.ligandConcs = ligandConcs;
        this.receptorConcs = receptorConcs;
    }
    
    public static SlowExchangeGUISave createUnsortedDataObject(TypesOfTitrations typeOfTitr,
                                                               boolean resonanceReversal,
                                                               double multiplier,
                                                               File outputDataFile,
                                                               File outputResultsFile,
                                                               List<File> fileList,
                                                               List<Double> ligandConcs,
                                                               List<Double> receptorConcs) {
        // TODO add some validation code
        
        return new SlowExchangeGUISave(typeOfTitr,
                                       resonanceReversal,
                                       multiplier,
                                       outputDataFile,
                                       outputResultsFile,
                                       fileList,
                                       ligandConcs,
                                       receptorConcs);
    }

    /**
     * @return the typeOfTitr
     */
    public TypesOfTitrations getTypeOfTitr() {
        return typeOfTitr;
    }

    /**
     * @return the resonanceReversal
     */
    public boolean isResonanceReversal() {
        return resonanceReversal;
    }

    /**
     * @return the multiplier
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * @return the outputDataFile
     */
    public File getOutputDataFile() {
        return outputDataFile;
    }

    /**
     * @return the outputResultsFile
     */
    public File getOutputResultsFile() {
        return outputResultsFile;
    }

    /**
     * @return the fileList
     */
    public List<File> getFileList() {
        return fileList;
    }

    /**
     * @return the ligandConcs
     */
    public List<Double> getLigandConcs() {
        return ligandConcs;
    }

    /**
     * @return the receptorConcs
     */
    public List<Double> getReceptorConcs() {
        return receptorConcs;
    }
    
    
    
    
    
    
    
}

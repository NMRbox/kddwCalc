package edu.uconn.kddwcalc.gui;

import edu.uconn.kddwcalc.data.DataArrayValidator;
import edu.uconn.kddwcalc.data.TypesOfTitrations;
import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * A class representing unsorted slow exchange NMR titration data that can be serialized and
 * deserialized to populate the SlowExchangeGUI with data.
 * 
 * Note the types of data
 * 
 * @see SlowExchangeGUIController
 * 
 * @author Rizzo
 * 
 * @since 1.8
 */

public class SlowExchangeGUISave implements Serializable {
    
    private final TypesOfTitrations typeOfTitr;
    private final boolean resonanceReversal;
    private final double multiplier;
    private final File outputDataFile;
    private final File outputResultsFile;
    private final List<File> fileList;
    private final List<Double> ligandConcs;
    private final List<Double>receptorConcs;
    
    
    /**
     * Initializes all instance variables. Note that the constructor is private so the only
     * way to create an instance of this class is through static method <code>createUnsortedDataObject</code>
     * 
     * @param typeOfTitr type of titration performed (ex 1H-15N HSQC or 1H-13C methyl HMQC)
     * @param resonanceReversal related to the order of resonances in the peak list
     * @param multiplier how the resonances will be scaled
     * @param outputDataFile where the sorted but not analyzed peak lists will be written
     * @param outputResultsFile where the final results will be written
     * @param fileList contains the files with the peaks lists which contain the resonances
     * @param ligandConcs the total ligand concentration
     * @param receptorConcs the total receptor concentration
     */
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
    
    /**
     * Simple factory to create an instance of <code>SlowExchangeGUISave<code>
     * 
     * @param typeOfTitr type of titration performed (ex 1H-15N HSQC or 1H-13C methyl HMQC)
     * @param resonanceReversal related to the order of resonances in the peak list
     * @param multiplier how the resonances will be scaled
     * @param outputDataFile where the sorted but not analyzed peak lists will be written
     * @param outputResultsFile where the final results will be written
     * @param fileList contains the files with the peaks lists which contain the chemical shifts
     * @param ligandConcs the total ligand concentration
     * @param receptorConcs the total receptor concentration
     * 
     * @return an instance of class <code>SlowExchangeGUISave</code> with all instance
     * variables initialized
     */
    public static SlowExchangeGUISave createUnsortedDataObject(TypesOfTitrations typeOfTitr,
                                                               boolean resonanceReversal,
                                                               double multiplier,
                                                               File outputDataFile,
                                                               File outputResultsFile,
                                                               List<File> fileList,
                                                               List<Double> ligandConcs,
                                                               List<Double> receptorConcs) {
        
        DataArrayValidator.isListLengthsAllEqual(ligandConcs, receptorConcs);
        
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
     * Gets an <code>enum</code> with information about the type of titration
     * 
     * @return the typeOfTitr
     */
    public TypesOfTitrations getTypeOfTitr() {
        return typeOfTitr;
    }

    /**
     * Gets information about how to deal with the order of {@link edu.uconn.kddwcalc.data.Resonance} objects 
     * in the peak lists
     * 
     * @return the resonanceReversal
     */
    public boolean isResonanceReversal() {
        return resonanceReversal;
    }

    /**
     * Gets the multiplier which is used to scale the two nuclei
     * 
     * @return the multiplier
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Gets the name and location where the sorted but unanalyzed data will be written. This is basically
     * the peak lists sorted by residue
     * 
     * @return the outputDataFile
     */
    public File getOutputDataFile() {
        return outputDataFile;
    }

    /**
     * Gets the name and location where the final results will be written. 
     * 
     * @return the outputResultsFile
     */
    public File getOutputResultsFile() {
        return outputResultsFile;
    }

    /**
     * The names and locations of the chemical shift lists (peak lists). These are not sorted by residue,
     * they are sorted by experimental point and must be sorted by residue before written by 
     * <code>getOutputDataFile</code>
     * 
     * @return the fileList
     */
    public List<File> getFileList() {
        return fileList;
    }

    /**
     * Gets the total ligand concentrations
     * 
     * @return the ligandConcs
     */
    public List<Double> getLigandConcs() {
        return ligandConcs;
    }

    /**
     * Gets the total receptor concentrations
     * 
     * @return the receptorConcs
     */
    public List<Double> getReceptorConcs() {
        return receptorConcs;
    }
    
    
    
    
    
    
    
}

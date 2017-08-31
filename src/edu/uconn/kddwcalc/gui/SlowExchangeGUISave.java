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
    
    List<File> fileList;
    double[] ligandConcs;
    double[] receptorConcs;
    
    public SlowExchangeGUISave(TypesOfTitrations typeOfTitr,
                               boolean resonanceReversal,
                               double multiplier,
                               File outputDataFile,
                               File outputResultsFile,
                               List<File> fileList,
                               double[] ligandConcs,
                               double[] receptorConcs) {
        
        this.typeOfTitr = typeOfTitr;
        this.resonanceReversal = resonanceReversal;
        this.multiplier = multiplier;
        this.outputDataFile = outputDataFile;
        this.outputResultsFile = outputResultsFile;
        this.fileList = fileList;
        this.ligandConcs = ligandConcs;
        this.receptorConcs = receptorConcs;
    }
    
    
    
    
    
}

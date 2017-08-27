package edu.uconn.kddwcalc.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class with data hard-coded for testing the application
 * 
 * @author Alex R.
 */
public class ResonanceTest {
    public static void main(String[] args) throws IOException {
        /*
        Resonance amideNitro = AmideNitrogen.validateAndCreate(115.9);
        Resonance amideProton = AmideProton.validateAndCreate(8.1);
        Resonance methylCarbon = MethylCarbon.validateAndCreate(20.1);
        Resonance methylProton = MethylProton.validateAndCreate(1.2);
        
        List<Resonance> resis = new ArrayList<>();
        
        resis.add(amideNitro);
        resis.add(amideProton);
        resis.add(methylCarbon);
        resis.add(methylProton);
        
        TitrationPoint methyl1 = MethylCarbonProtonTitrationPoint.validateAndCreate(
            70.0, 200.0, methylCarbon, methylProton);
        TitrationPoint methyl2 = MethylCarbonProtonTitrationPoint.validateAndCreate(
            71.0, 201.0, methylCarbon, methylProton);
        TitrationPoint methyl3 = MethylCarbonProtonTitrationPoint.validateAndCreate(
            72.0, 202.0, methylCarbon, methylProton);
        TitrationPoint methyl4 = MethylCarbonProtonTitrationPoint.validateAndCreate(
            73.0, 203.0, methylCarbon, methylProton);
        
        TitrationPoint amide1 = AmideNitrogenProtonTitrationPoint.validateAndCreate(
            54, 32, amideNitro, amideProton);
        TitrationPoint amide2 = AmideNitrogenProtonTitrationPoint.validateAndCreate(
            55, 33, amideNitro, amideProton);
        TitrationPoint amide3 = AmideNitrogenProtonTitrationPoint.validateAndCreate(
            56, 34, amideNitro, amideProton);
        TitrationPoint amide4 = AmideNitrogenProtonTitrationPoint.validateAndCreate(
            57, 35, amideNitro, amideProton);
        
        /*
        System.out.println(methyl);
        
        System.out.println();
        
        System.out.println(amide);
        
        
        
        Titration amideTitr = new Titration(20.0);
        Titration methylTitr = new Titration(30.0);
        
        amideTitr.addPoint(amide1);
        amideTitr.addPoint(amide2);
        amideTitr.addPoint(amide3);
        amideTitr.addPoint(amide4);
        
        
        methylTitr.addPoint(methyl1);
        methylTitr.addPoint(methyl2);
        methylTitr.addPoint(methyl3);
        methylTitr.addPoint(methyl4);
        
        
        
        
        TitrationSeries series = new TitrationSeries();
        
        series.addTitration(amideTitr);
        series.addTitration(methylTitr);
        
        */
        
        //RawData
        
        //TitrationSeries series = 
        //series.printTitrationSeries();
        
        // this code fits 15N ubz at 50 uM to LB-ubiquitin
        
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("0.txt"));
        paths.add(Paths.get("0.25.txt"));
        paths.add(Paths.get("0.5.txt"));
        paths.add(Paths.get("0.75.txt"));
        paths.add(Paths.get("1.txt"));
        paths.add(Paths.get("1.5.txt"));
        paths.add(Paths.get("2.txt"));
        paths.add(Paths.get("3.txt"));
        paths.add(Paths.get("4.txt"));
        
        List<Double> ligandConcs = new ArrayList<>();
        ligandConcs.add(0.0);
        ligandConcs.add(12.405);
        ligandConcs.add(24.629);
        ligandConcs.add(36.675);
        ligandConcs.add(48.548);
        ligandConcs.add(71.788);
        ligandConcs.add(94.377);
        ligandConcs.add(137.709);
        ligandConcs.add(178.743);
        
        List<Double> receptorConcs = new ArrayList<>();
        receptorConcs.add(50.0);
        receptorConcs.add(49.632);
        receptorConcs.add(49.269);
        receptorConcs.add(48.912);
        receptorConcs.add(48.559);
        receptorConcs.add(47.870);
        receptorConcs.add(47.199);
        receptorConcs.add(45.914);
        receptorConcs.add(44.696);
        
        // end of whats required for 15NUBZ + LB-ub fitting
        
        /*
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get("d221a/0.txt"));
        paths.add(Paths.get("d221a/0.25.txt"));
        paths.add(Paths.get("d221a/0.5.txt"));
        paths.add(Paths.get("d221a/0.75.txt"));
        paths.add(Paths.get("d221a/1.txt"));
        paths.add(Paths.get("d221a/1.5.txt"));
        paths.add(Paths.get("d221a/2.txt"));
        paths.add(Paths.get("d221a/3.txt"));
        paths.add(Paths.get("d221a/4.txt"));
        
        List<Double> ligandConcs = new ArrayList<>();
        ligandConcs.add(0.0);
        ligandConcs.add(87.0);
        ligandConcs.add(165.0);
        ligandConcs.add(235.0);
        ligandConcs.add(298.0);
        ligandConcs.add(408.0);
        ligandConcs.add(499.0);
        ligandConcs.add(644.0);
        ligandConcs.add(753.0);
        
        List<Double> receptorConcs = new ArrayList<>();
        receptorConcs.add(200.0);
        receptorConcs.add(188.0);
        receptorConcs.add(178.0);
        receptorConcs.add(169.0);
        receptorConcs.add(160.0);
        receptorConcs.add(146.0);
        receptorConcs.add(134.0);
        receptorConcs.add(115.0);
        receptorConcs.add(101.0);
        
        */
        
        RawData rawData = RawData.createRawData(paths, ligandConcs, receptorConcs, 0.1 , false);
        
        AbsFactory factory = FactoryMaker.createFactory(TypesOfTitrations.AMIDEHSQC);
        
        TitrationSeries series = factory.analyzeDataFiles(rawData);
        
        //series.printTitrationSeries();
        
        /* code doesnt work becaues i made getCumulativeShifts return a double[] for least squrae target array
              //  this was originally to test the function that makes cumulative shifts array. it worked
        series.getCumulativeShifts().stream()
                                    .forEach(System.out::println);
        */ 
        
        //series.writeToFileForExcel();
        
        
        /*
        
        // want to change so LeastSquaresFitter.fit(series) return dataobject
        
        CumResults results = LeastSquaresFitter.fitCumulativeData(series.getLigandConcList(), 
                                                                  series.getReceptorConcList(), 
                                                                  series.getCumulativeShifts());
        double kd = results.getKd();
        
        System.out.printf("kd = %.6f%n", kd);
        System.out.printf("percent bound = %.6f%n", results.getPercentBound());
        
        
        
        List<Double> ligandList = series.getLigandConcList();
        List<Double> receptorList = series.getReceptorConcList();
        
        double[] cspArrayByResidue = series.getCSPbyResidueArray(kd);
        
            
        
        System.out.println();
        System.out.println("And the CSPs by residue:");
        
        for(int ctr = 0; ctr < cspArrayByResidue.length; ctr++)
        {
            System.out.println(cspArrayByResidue[ctr]);
        }
        
        */
        
        Results results = LeastSquaresFitter.fit(series);
        
        
        results.writeResultsToDisk();
        
        
 
    }
    
    
}

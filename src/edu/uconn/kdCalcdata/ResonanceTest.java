package edu.uconn.kdCalcdata;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResonanceTest
{
    public static void main(String[] args) throws IOException
    {
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
        
        RawData rawData = RawData.createRawData(paths, ligandConcs, receptorConcs, 0.1 , false);
        
        AbsFactory factory = new AmideNitrogenProtonFactory();
        
        TitrationSeries series = factory.analyzeDataFiles(rawData);
        
        series.printTitrationSeries();
        
        
    }
}

package edu.uconn.kdCalcdata;


import java.util.ArrayList;
import java.util.List;

public class ResonanceTest
{
    public static void main(String[] args)
    {
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
        
        */
        
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
        
        System.out.println(series);
         
    }
}

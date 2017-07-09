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
        
        TitrationPoint methyl = MethylCarbonProtonTitrationPoint.validateAndCreate(
            70.0, 200.0, methylCarbon, methylProton);
        
        TitrationPoint amide = AmideNitrogenProtonTitrationPoint.validateAndCreate(
            54, 32, amideNitro, amideProton);
        
        /*
        System.out.println(methyl);
        
        System.out.println();
        
        System.out.println(amide);
        */
        
        Titration titr = new Titration(20.0);
        
        titr.addPoint(amide);
        titr.addPoint(methyl);
        
        System.out.println(titr);
        
    }
}

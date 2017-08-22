package edu.uconn.kddwcalc.data;

import java.util.List;
import java.util.Scanner;

public class AmideNitrogenProtonFactory extends AbsFactory
{
    @Override
    public Resonance getFirstSpecificResonance(Scanner scanner)
    {
        return AmideNitrogen.validateAndCreate(scanner.nextDouble());
    }
    
    @Override
    public Resonance getSecondSpecificResonance(Scanner scanner)
    {
        return AmideProton.validateAndCreate(scanner.nextDouble());
    }

    @Override
    public TitrationPoint makeSpecificTypeOfPoint(double ligandConc, double receptorConc, 
                Resonance firstCoordinate, Resonance secondCoordinate)
    {
        return AmideNitrogenProtonTitrationPoint.validateAndCreate(ligandConc, receptorConc, 
                firstCoordinate, secondCoordinate);
    }
}

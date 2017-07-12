package edu.uconn.kdCalc.data;

import java.util.List;
import java.util.Scanner;

public class MethylCarbonProtonFactory extends AbsFactory
{
    @Override
    public Resonance getFirstSpecificResonance(Scanner scanner)
    {
        return MethylCarbon.validateAndCreate(scanner.nextDouble());
    }
    
    @Override
    public Resonance getSecondSpecificResonance(Scanner scanner)
    {
        return MethylProton.validateAndCreate(scanner.nextDouble());
    }
    
    @Override
    public TitrationPoint makeSpecificTypeOfPoint(double ligandConc, double receptorConc, 
                Resonance firstCoordinate, Resonance secondCoordinate)
    {
        return MethylCarbonProtonTitrationPoint.validateAndCreate(ligandConc, receptorConc, 
                firstCoordinate, secondCoordinate);
    }
}

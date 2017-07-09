package edu.uconn.kdCalc.data;

import java.util.List;
import java.util.Scanner;

public class AmideNitrogenProtonFactory extends AbsFactory
{

    @Override
    public Resonance[] makeTwoResonances(Scanner scanner, boolean resonanceReversal) 
    {
       final Resonance[] twoResonances = new Resonance[2];
       
       if(resonanceReversal == false)
       {
           twoResonances[0] = AmideNitrogen.validateAndCreate(scanner.nextDouble());
           twoResonances[1] = AmideProton.validateAndCreate(scanner.nextDouble());
       }
       else if (resonanceReversal == true)
       {
           twoResonances[1] = AmideProton.validateAndCreate(scanner.nextDouble());
           twoResonances[0] = AmideNitrogen.validateAndCreate(scanner.nextDouble()); 
       }
       
       return twoResonances;   
    }

    @Override
    public TitrationPoint makeTitrationPoint(Scanner scanner, double ligandConc, 
        double receptorConc, boolean resonanceReversal) 
    {
        final Resonance[] twoCoordinates = makeTwoResonances(scanner, resonanceReversal);

        TitrationPoint point = AmideNitrogenProtonTitrationPoint.validateAndCreate(ligandConc, receptorConc, 
                twoCoordinates[0], twoCoordinates[1]);

        return point;
    }



}

package edu.uconn.kdCalcdata;

import java.util.List;
import java.util.Scanner;

public class AmideNitrogenProtonFactory extends AbsFactory
{

    @Override
    public Resonance[] makeResonances(Scanner scanner, boolean resonanceReversal) 
    {
       Resonance[] twoResonances = new Resonance[2];
       
       if(resonanceReversal == false)
       {
           twoResonances[1] = AmideNitrogen.validateAndCreate(scanner.nextDouble());
           twoResonances[2] = AmideProton.validateAndCreate(scanner.nextDouble());
       }
       else if (resonanceReversal == true)
       {
           twoResonances[2] = AmideProton.validateAndCreate(scanner.nextDouble());
           twoResonances[1] = AmideNitrogen.validateAndCreate(scanner.nextDouble()); 
       }
       
       return twoResonances;   
    }

    @Override
    public TitrationPoint makeTitrationPoint(Scanner scanner, double ligandConc, 
        double receptorConc, boolean resonanceReversal) 
    {
        Resonance[] twoCoordinates = makeResonances(scanner, resonanceReversal);

        TitrationPoint point = AmideNitrogenProtonTitrationPoint.validateAndCreate(ligandConc, receptorConc, 
                twoCoordinates[1], twoCoordinates[2]);

        return point;
    }



}

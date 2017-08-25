package edu.uconn.kddwcalc.data;

import java.util.Arrays;

/**
 * A class with a single static method that tests a set of arrays to make sure they have the 
 * same length. During the computation, arrays with ligand concentrations, receptor concentrations and either 
 * chemical shifts or chemical shift perturbations all have to have the same number of elements. This is because each 
 * index represents a single experimental point. In other words: <code>ligand[0], receptor[0] and chemShift[0]</code> 
 * are the value from the first titration point.
 * 
 * @author Alex R.
 * 
 * @see ArrayLengthsInvalidException
 * 
 */
public class ArrayLengthValidator {
    
    /**
     * Tests a variable number of arrays to make sure they have the same length
     * 
     * @param arrays objects to test to ensure all have equal lengths
     * 
     * @return <code>true</code> if all arrays have the same length, otherwise <code>false</code>
     */
    public static boolean isValidLengths(double[]...arrays) {
        long num = Arrays.stream(arrays)
                         .mapToLong(array -> array.length)
                         .distinct()
                         .count();
        
        boolean isValidLength = false; // start by assuming the worst
        
        if(num == 1) // will be true if the double[] arrays passed in have same length
            isValidLength = true;
        
        return isValidLength;    
    }
} // end class ArrayLengthValidator

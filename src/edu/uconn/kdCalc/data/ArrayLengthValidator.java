package edu.uconn.kdCalc.data;

import java.util.Arrays;

       

public class ArrayLengthValidator 
{
    public static boolean isValidLengths(double[]...arrays)
    {
        long num = Arrays.stream(arrays)
                         .mapToLong(array -> array.length)
                         .distinct()
                         .count();
        
        boolean isValidLength = false;
        
        if(num == 1) // because all the double[] arrays passed in have same length
            isValidLength = true;
        
        return isValidLength;    
    }
} // end class ArrayLengthValidator

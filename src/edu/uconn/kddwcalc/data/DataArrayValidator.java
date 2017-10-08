package edu.uconn.kddwcalc.data;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.TextField;

/**
 * A class with a single public static method that tests a set of arrays to make sure they have the 
 * same length and no duplicate data. During the computation, arrays with ligand concentrations, 
 * receptor concentrations and either chemical shifts or chemical shift perturbations all have to 
 * have the same number of elements. This is because each index represents a single experimental point.
 * In other words: <code>ligand[0], receptor[0] and chemShift[0]</code> are the value from the first 
 * titration point. In addition, no protein concentrations of chemical shift perturbations should be 
 * duplicated to the 15th decimal point. Duplicate data is most likely caused by a user entering 
 * the same values twice or choosing the same file twice.
 * 
 * @author Alex R.
 * 
 * @see ArraysInvalidException
 * 
 */
public class DataArrayValidator {
    
    /**
     * Tests a set of array to ensure they all have the same length and that each individual array
     * does not contain duplicate data.
     * 
     * @param array each array, which is wrapped into a 2D array, most likely has either 
     * protein concentrations or chemical shifts.
     * 
     * @return <code>true</code> if the arrays have same length and no duplicates, <code>false</code> otherwise
     */
    public static boolean isValid(double[]...array) {
        return (isValidLengths(array) && !isDuplicates(array));
    }
    
    /**
     * Tests to make sure the user inputted an equal number of ligand concentrations, receptor concentrations, and
     * peak list files with chemical shifts. 
     * 
     * @param fileList peak lists
     * @param ligandConcList ligand concentrations
     * @param receptorConcList receptor concentrations
     * 
     * @return true if all three argument {@link List} objects have the same length 
     */
    public static boolean isListLengthsAllEqual(List<File> fileList, 
                                                double[] ligandConcList,
                                                double[] receptorConcList) {
        
        return (fileList.size()       ==  ligandConcList.length
            &&  ligandConcList.length ==  receptorConcList.length
            &&  fileList.size()       ==  receptorConcList.length);
    }
    
    /**
     * Tests a variable number of arrays to make sure they have the same length
     * 
     * @param arrays objects to test to ensure all have equal lengths
     * 
     * @return <code>true</code> if all arrays have the same length, otherwise <code>false</code>
     */
    private static boolean isValidLengths(double[][] arrays) {
        long num = Arrays.stream(arrays)
                         .mapToLong(array -> array.length)
                         .distinct()
                         .count();
        
        boolean isValidLength = false; // start by assuming the worst
        
        if(num == 1) // will be true if the double[] arrays passed in have same length
            isValidLength = true;
        
        return isValidLength;    
    }
    
    /**
     * Tests a multidimensional array to make sure there are no duplicates within a row. Its very unlikely to 
     * have two identical (to the 15th decimal place) protein concentrations or chemical shift perturbations.
     * 
     * @param arrays multidimensional array to be tested with rows containing protein concentrations or chemical
     * shift perturbations.
     * 
     */
    private static boolean isDuplicates(double [][] arrays) {
        long num = Arrays.stream(arrays) // 
                         .mapToLong(array -> Arrays.stream(array)
                                                   .distinct()
                                                   .count())
                         .filter(lengthOfArray -> lengthOfArray == arrays[0].length)
                         .count();
        
        boolean isDuplicates = true; // start by assuming the worst
        
        if(num == arrays.length) // will be true if the double[] arrays passed have 
            isDuplicates = false;             // same length before and after removing duplicates
        
        return isDuplicates;    
    }
} // end class DataArrayValidator

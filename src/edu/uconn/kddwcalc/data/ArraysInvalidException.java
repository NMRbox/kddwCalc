package edu.uconn.kddwcalc.data;

/**
 * Exception that is thrown if the data arrays end up with a different number of elements. 
 * 
 * In the program, the arrays with receptor/ligand concentrations and chemical shifts or 
 * chemical shift perturbations must have the same number of elements since each index 
 * represents a single experimental point. 
 * 
 * In addition, the arrays can't contain any duplicates
 * 
 * @author Alex Ri.
 */
public class ArraysInvalidException extends Exception {
    
    /**
     * Initializes an instance with a default message
     */
    public ArraysInvalidException() {
        super("The tested arrays don't have the same length or have duplicates");
    }
    
    /**
     * Initializes an instance with a custom message
     * 
     * @param string the custom message
     */
    public ArraysInvalidException(String string) {
        super(string);
    }
    
    /**
     * Initializes an instance with a custom message and attaches another {@link Throwable}
     * for chaining exceptions
     * 
     * @param string the custom message
     * @param throwable the {@link Throwable} to chain
     */
    public ArraysInvalidException(String string, Throwable throwable) {
        super(string, throwable);
    }
    
    /**
     * Initializes an instance with a {@link Throwable} object that will be chained
     * 
     * @param throwable the {@link Throwable} to chain
     */
    public ArraysInvalidException(Throwable throwable) {
        super(throwable);
    }
    
}

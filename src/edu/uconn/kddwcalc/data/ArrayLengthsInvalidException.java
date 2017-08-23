package edu.uconn.kddwcalc.data;

public class ArrayLengthsInvalidException extends Exception {
    
    public ArrayLengthsInvalidException() {
        super("The tested arrays don't have the same length");
    }
    
    public ArrayLengthsInvalidException(String string) {
        super(string);
    }
    
    public ArrayLengthsInvalidException(String string, Throwable throwable) {
        super(string, throwable);
    }
    
    public ArrayLengthsInvalidException(Throwable throwable) {
        super(throwable);
    }
    
}

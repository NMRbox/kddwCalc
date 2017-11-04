/**
 * Provides the classes necessary to perform nonlinear least squares fitting 
 * of data.
 * 
 * <code>Calculatable</code> objects are sent to methods of this class for a one-
 * or two-parameter fitting. The results are sent back as a double (in case of
 * one-parameter fitting) or a <code>ResultsTwoParamKdMaxObjs</code> object
 * in the case of a two-parameter fit.
 * 
 * Any dataset that extends <code>Calculatable</code> and has an observable whose
 * signal represents the percentage bound when compared with the free and fully
 * bound points can use this package.
 * 
 * Non-linear least squares fitting is performed using packages from the 
 * Apache Commons Mathematics Library
 */
package edu.uconn.kddwcalc.fitting;

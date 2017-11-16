package edu.uconn.kddwcalc.data;

import edu.uconn.kddwcalc.fitting.Calculatable;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

/**
 * A class that contains all the data from a fast exchange NMR listOfPoints *for a single residue* to determine affinity, 
 delta-omega (dw) and other parameters
 * 
 * @see edu.uconn.kddwcalc.analyze.AbsFactory
 * @see TitrationSeries
 * @see TitrationPoint
 * 
 * @author Alex R.
 * 
 * @since 1.8
 */
public class Titration implements Calculatable {
    
    private final List<TitrationPoint> listOfPoints;
    private final double scalingFactor;
    private final String identifier;
    
    /**
     * Initializes a {@link Titration} instance with a scalingFactor from the user. After this is 
     * performed, the <code>List{@literal <}TitrationPoint{@literal >}</code> is empty. 
     * 
     * @param multiplier the value from the user that indicates how to scale the two nuclei into 1H-ppm
     */
    private Titration(List<TitrationPoint> listOfPoints, 
                      double multiplier,
                      String identifier) {
        
        this.listOfPoints = listOfPoints;
        this.scalingFactor = multiplier;
        this.identifier = identifier;
    }
    
//    /**
//     * Adds a {@link TitrationPoint} to the instance variable <code>listOfPoints</code>
//     * 
//     * @param pnt contains the information for one peak from one spectrum 
//     * (ligand and receptor concentrations and two chemical shifts (subclasses of {@link Resonance})
//     * 
//     * @see edu.uconn.kddwcalc.analyze.AbsFactory
//     */
//    public final void addPoint(TitrationPoint pnt) {
//        listOfPoints.add(pnt);
//    }
    
    /**
     * 
     * 
     * @param listOfPoints the experimental points
     * @param scalingFactor the scaling factor for the two dimensions
     * @param identifier name this this data (e.g. residue number)
     * 
     * @return instance of <code>Titration</code> 
     */
    public static Titration makeTitration(List<TitrationPoint> listOfPoints,
                                          double scalingFactor,
                                          String identifier) {
        
        if (scalingFactor <= 0)
            throw new IllegalArgumentException("scaling factor zero or negative");
        
        if (listOfPoints.stream().anyMatch(pnt -> pnt == null))
            throw new IllegalArgumentException("null point creating Titration");
        
        return new Titration(listOfPoints, scalingFactor, identifier);
    }
    
    /**
     * Gets the receptor concentrations.
     * 
     * @return an array containing the receptor concentrations
     */
    @Override
    public double[] getReceptorConcs() {
        return listOfPoints.stream()
                           .mapToDouble(TitrationPoint::getReceptorConc)
                           .toArray();
    }
    
    /**
     * Gets the ligand concentrations.
     * 
     * @return an array containing the ligand concentrations
     */
    @Override
    public double[] getLigandConcs() {
        return listOfPoints.stream()
                           .mapToDouble(TitrationPoint::getLigandConc)
                           .toArray();
    }
    
    /**
     * Effectively, this uses the equation for distance
     *[sqrt((xn-x0)^2 + (yn-y0)^2)] with scaling based on the scalingFactor. 
     * The first point should have a CSP = 0, while the remaining points are 
     * calculated from the first point with free receptor.
     *
     * Note the use of {@link TitrationPoint#getResonance1}  and 
     * {@link TitrationPoint#getResonance2. This introduces coupling between this class and others.
     * 
     * @return the chemical shift perturbations as 1H-ppm (because of scaling)
     */
    @Override
    public double[] getObservables() {

        return listOfPoints.stream().mapToDouble(point -> {
               
            return Math.sqrt(Math.pow(point.getResonance2().getChemShift() 
                - listOfPoints.get(0).getResonance2().getChemShift(), 2.0)
                + Math.pow(scalingFactor * (point.getResonance1().getChemShift() 
                - listOfPoints.get(0).getResonance1().getChemShift()), 2.0));
                })
                .toArray();
        
    } // end method getObservables
    
    /**
     * Prints information from an NMR listOfPoints for a single residue to a text file.
     * 
     * @param output the {@link Formatter} instance that writes finalResults.txt
     * 
     * @see TitrationSeries
     */
    public void printTitration(Formatter output) {
        output.format("Titration: %s%nScaling Factor: %.5f%nCSPs: %s%n"
            + "LigandConc     ReceptorConc    Resonance1      Resonance2%n",
            getIdentifier(), getScalingFactor(), Arrays.toString(getObservables()));
        
        listOfPoints.stream() // results in a Stream<TitrationPoint>
                    .forEach(titrPoint -> 
                                 output.format("%s%n", titrPoint.toString()));
    }

    private double getScalingFactor() {
        return scalingFactor;
    }
    
    @Override
    public String getIdentifier() {
        return identifier;
    }
    
    public double[] getFreeReceptorPoint () {
        return new double[] {listOfPoints.get(0).getResonance1().getChemShift(), 
                             listOfPoints.get(0).getResonance2().getChemShift()};
    }
    
    
}

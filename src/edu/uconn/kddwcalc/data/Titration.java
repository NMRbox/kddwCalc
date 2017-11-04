package edu.uconn.kddwcalc.data;

import java.util.ArrayList;
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
    private final double multiplier;
    
    /**
     * Initializes a {@link Titration} instance with a multiplier from the user. After this is 
     * performed, the <code>List{@literal <}TitrationPoint{@literal >}</code> is empty. 
     * 
     * @param multiplier the value from the user that indicates how to scale the two nuclei into 1H-ppm
     */
    private Titration(List<TitrationPoint> listOfPoints, double multiplier) {
        
        this.listOfPoints = listOfPoints;
        this.multiplier = multiplier;
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
     * @param multiplier the scaling factor for the two dimensions
     * 
     * @return instance of <code>Titration</code> 
     */
    public static Titration makeTitration(List<TitrationPoint> listOfPoints,
                                          double multiplier) {
        
        // TODO add some kind of validation here
        
        return new Titration(listOfPoints, multiplier);
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
     * Takes the two {@link Resonance} variables from instance variable <code>listOfPoints</code> and extracts the 
     * chemical shift perturbation. Effectively, this uses the equation for distance
     * [sqrt((xn-x0)^2 + (yn-y0)^2)] with scaling based on the multiplier. The first point should have a CSP = 0,
     * while the remaining points are calculated from the first point with free receptor.
     * 
     * Note the use of {@link TitrationPoint#getResonance1}  and {@link TitrationPoint#getResonance2}. 
     * This introduces coupling between this class and others.
     * 
     * @return the chemical shift perturbations as 1H-ppm (because of scaling)
     */
    public List<Double> getCSPsFrom2DPoints() {
        final List<Double> csps = new ArrayList<>();
        
        for(int ctr = 0; ctr < listOfPoints.size(); ctr++) {
            csps.add(Math.sqrt(Math.pow(listOfPoints.get(ctr).getResonance2().getChemShift() 
                - listOfPoints.get(0).getResonance2().getChemShift(), 2.0)
                + Math.pow(multiplier * (listOfPoints.get(ctr).getResonance1().getChemShift() 
                - listOfPoints.get(0).getResonance1().getChemShift()), 2.0)));
        }

        return csps;
    }
    
    /**
     * Turns the <code>List{@literal <}Double{@literal >}</code> from {@link #getCSPsFrom2DPoints}
     * and turns it into <code>double[]</code>.
     * 
     * @return array of <code>double</code> with the chemical shift perturbations as 1H-ppm
     */
    @Override
    public double[] getObservables() {
        return getCSPsFrom2DPoints().stream()
                                    .mapToDouble(Double::doubleValue)
                                    .toArray();
    }
    
    /**
     * Prints information from an NMR listOfPoints for a single residue to a text file.
     * 
     * @param output the {@link Formatter} instance that writes finalResults.txt
     * 
     * @see TitrationSeries
     */
    public void printTitration(Formatter output) {
        output.format("Titration with multiplier of %.5f%nCSPs: %s%n"
            + "LigandConc     ReceptorConc    Resonance1      Resonance2%n", multiplier, getCSPsFrom2DPoints().toString());
        
        listOfPoints.stream() // results in a Stream<TitrationPoint>
                 .forEach(titrPoint -> output.format("%s%n", titrPoint.toString()));
    }
}

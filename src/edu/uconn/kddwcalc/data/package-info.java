/**
 * Provides the classes to hold the data model.
 * 
 * The data model is structured as follows:
 * 
 * A {@link TitrationSeries} object contains all the data for a complete NMR titration data collection
 * 
 * A {@link TitrationSeries} is composed of {@link Titration} objects
 * A {@link Titration} is composed of {@link TitrationPoint} objects
 * A {@link TitrationPoint} is composed of two {@link Resonance} objects
 *  and two concentrations for receptor and ligand.
 * 
 * 
 * As of 171013, a {@link TitrationSeries} is created by taking a {@link edu.uconn.kddwcalc.gui.RawData} object and using 
 * the factories in the sorting package.
 * 
 * @author Alex R.
 * 
 * @since 1.8
 */
package edu.uconn.kddwcalc.data;

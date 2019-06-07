package edu.uconn.kddwcalc.data;

import edu.uconn.kddwcalc.gui.FastExchangeGUIController;

/**
 * Represent the types of titrations. This is used for the switch in 
 *  {@link edu.uconn.kddwcalc.analyze.FactoryMaker}.
 * 
 * @author Alex R.
 * 
 * @see edu.uconn.kddwcalc.analyze.FactoryMaker
 * @see edu.uconn.kddwcalc.analyze.AbsFactory
 * @see FastExchangeGUIController
 */
public enum TypesOfTitrations {

    /**
     * Represents a 1H-15N HSQC-based NMR titration (TROSY also works).
     */
    AMIDEHSQC,

    /**
     *Represents a 1H-13N methyl HMQC-based NMR titration.
     */
    METHYLHMQC }

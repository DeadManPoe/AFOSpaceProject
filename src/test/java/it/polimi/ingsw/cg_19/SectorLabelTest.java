package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Coordinate;

import client.SectorLabel;

/**
 * Some test for the SectorLabel class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class SectorLabelTest {

	/**
	 * Test for the SectorLabel constructor Test the getCoordinate method and
	 * the getImageFilename method
	 */
	@Test
	public void testSectorLabel() {
		SectorLabel lbl = new SectorLabel(new Coordinate('A', 1), "prova.png");
		assertEquals(new Coordinate('A', 1), lbl.getCoordinate());
		assertEquals("prova.png", lbl.getImageFilename());
	}

	/**
	 * Test for the getCoordinate method
	 */
	@Test
	public void testGetCoordinate() {
		SectorLabel lbl = new SectorLabel(new Coordinate('A', 1), "");
		assertEquals(new Coordinate('A', 1), lbl.getCoordinate());
	}

}

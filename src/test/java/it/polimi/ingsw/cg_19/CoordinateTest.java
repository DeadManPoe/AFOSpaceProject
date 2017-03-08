package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Coordinate;

/**
 * Some tests for the Coordinate class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class CoordinateTest {

	private Coordinate c;

	/**
	 * Test the getCoordinate method. This test checks whether getCoordinate()
	 * return a consistent value or not
	 */
	@Test
	public void getCoordinateTest() {
		c = new Coordinate('A', 1);
		assertEquals(1, c.getY());
		assertEquals('A', c.getX());
	}

	/**
	 * Test the equals method. This test checks if equals(override of Object's
	 * equals) return a correct boolean value
	 */
	@Test
	public void equalsTest() {
		c = new Coordinate('A', 1);
		assertTrue(c.equals(new Coordinate('A', 1)));
		assertFalse(c.equals(new Coordinate('B', 2)));
		assertFalse(c.equals(null));
		assertFalse(c.equals(new String("s")));
	}

}

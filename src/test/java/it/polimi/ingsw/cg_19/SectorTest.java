/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Coordinate;
import common.Sector;
import common.SectorType;

public class SectorTest {

	*/
/**
	 * @author Andrea Sessa
	 * @see Sector Some tests for the sector class
	 *//*


	private Sector s;

	*/
/**
	 * This test checks the constructor of the Sector Class verifying that
	 * legality is set according to the type of sector
	 *//*

	@Test
	public void testSector() {
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.ALIEN);
		Sector s2 = new Sector(new Coordinate('A', 1), SectorType.HUMAN);
		Sector s3 = new Sector(new Coordinate('A', 1), SectorType.DANGEROUS);
		Sector s4 = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		Sector s5 = new Sector(new Coordinate('A', 1), SectorType.OPEN_RESCUE);
		Sector s6 = new Sector(new Coordinate('A', 1), SectorType.CLOSED_RESCUE);

		assertEquals(SectorLegality.NONE, s1.getSectorLegality());
		assertEquals(SectorLegality.NONE, s2.getSectorLegality());
		assertEquals(SectorLegality.ALL, s3.getSectorLegality());
		assertEquals(SectorLegality.ALL, s4.getSectorLegality());
		assertEquals(SectorLegality.HUMAN, s5.getSectorLegality());
		assertEquals(SectorLegality.HUMAN, s6.getSectorLegality());

	}

	*/
/**
	 * This test checks if getCoordinate returns the correct coordinate
	 * according to the coordinates of the sector
	 *//*

	@Test
	public void testGetCoordinate() {
		s = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		assertEquals(new Coordinate('A', 1), s.getCoordinate());
		assertNotEquals(new Coordinate('A', 2), s.getCoordinate());
	}

	*/
/**
	 * @see SectorType This test checks if getSectorType returns the correct
	 *      sectorType according to the type of the sector
	 *//*

	@Test
	public void testGetSectorType() {
		s = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		assertEquals(SectorType.SAFE, s.getSectorType());
		assertNotEquals(SectorType.ALIEN, s.getSectorType());
	}

	*/
/**
	 * This test checks if equals(override of equals() Object) returns a correct
	 * boolean value true if equals, false if not
	 *//*

	@Test
	public void testEqualsObject() {
		s = new Sector(new Coordinate('A', 1), SectorType.ALIEN);
		assertTrue(s
				.equals(new Sector(new Coordinate('A', 1), SectorType.ALIEN)));
		assertFalse(s.equals(new Sector(new Coordinate('B', 2),
				SectorType.DANGEROUS)));
		assertFalse(s.equals(null));
		assertFalse(s.equals(new String("s")));
	}

	*/
/**
	 * Checks that the contains method correctly spots the players in the sector
	 *//*

	@Test
	public void testContains() {
		Player p = new Player(PlayerType.HUMAN, "");
		Sector s1 = new Sector(new Coordinate('A', 1), SectorType.ALIEN);
		s1.addPlayer(p);
		assertTrue(s1.containsPlayer(p));

	}

}
*/

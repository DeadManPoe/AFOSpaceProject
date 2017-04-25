/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;
import org.junit.Test;
import common.Coordinate;
import common.MoveAction;
import common.Sector;
import common.SectorType;

*/
/**
 * Some tests for the MoveAction class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 *//*

public class MoveActionTest {

	*/
/**
	 * Test for the getTarget method Checks if the target sector passed to the
	 * constructor is returned correctly
	 *//*

	@Test
	public void testGetTarget() {
		Sector target = new Sector(new Coordinate('A', 1), SectorType.SAFE);
		MoveAction action = new MoveAction(target);
		assertEquals(target, action.getTarget());
	}

	*/
/**
	 * Test for the equals method
	 *//*

	@Test
	public void testEqualsObject() {
		MoveAction action = new MoveAction(null);
		assertTrue(action.equals(new MoveAction(null)));
		assertFalse(action.equals(null));
		assertFalse(action.equals(new Player(PlayerType.ALIEN, "")));
	}

	*/
/**
	 * Test for the toString method
	 *//*

	@Test
	public void testToString() {
		MoveAction action = new MoveAction(null);
		assertEquals("MoveAction [target=null]", action.toString());
	}

}*/

package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AttackObjectCard;
import common.Coordinate;
import common.Sector;
import common.SectorType;

/**
 * Some tests for AttackObjectCard class
 * 
 * @author andrea
 *
 */
public class AttackObjectCardTest {

	/**
	 * Checks that the getAttackTarget method returns the target sector
	 * correctly
	 */
	@Test
	public void testAttackObjectCard() {
		AttackObjectCard objectCard = new AttackObjectCard(new Sector(
				new Coordinate('A', 1), SectorType.SAFE));
		assertEquals(new Sector(new Coordinate('A', 1), SectorType.SAFE),
				objectCard.getAttackTarget());
	}

	/**
	 * Test for the toString method"
	 */
	@Test
	public void testToString() {
		AttackObjectCard objectCard = new AttackObjectCard(new Sector(
				new Coordinate('A', 1), SectorType.SAFE));
		assertEquals("AttackObjectCard", objectCard.toString());
	}

}

package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;
import common.MoveAttackAction;
import common.Sector;
import common.SectorType;

/**
 * Some tests for the AttackAction Test
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class MoveAttackActionTest {

	/**
	 * Checks that the getTarget method return the correct target sector
	 */
	@Test
	public void testGetTarget() {
		Sector s = new Sector(null, SectorType.SAFE);
		MoveAttackAction action = new MoveAttackAction(s);
		assertEquals(s, action.getTargetSector());
	}
}

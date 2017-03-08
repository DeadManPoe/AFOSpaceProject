package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.EndTurnAction;

/**
 * Some tests for the EndTurnAction class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class EndTurnActionTest {

	@Test
	public void testToString() {
		EndTurnAction action = new EndTurnAction();
		assertEquals("EndTurnAction []", action.toString());
	}

}

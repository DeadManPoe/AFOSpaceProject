package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.DrawRescueCardAction;

/**
 * Some tests for the DrawRescueCard action
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DrawRescueCardActionTest {
	@Test
	public void testToString() {
		DrawRescueCardAction action = new DrawRescueCardAction();
		assertEquals("DrawRescueCardAction []", action.toString());
	}

}

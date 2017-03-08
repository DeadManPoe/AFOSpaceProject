package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.DrawObjectCardAction;

/**
 * Test for DrawActionFromObject
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DrawObjectCardActionTest {

	@Test
	public void testToString() {
		DrawObjectCardAction action = new DrawObjectCardAction();
		assertEquals("DrawActionFromObject []", action.toString());
	}

}

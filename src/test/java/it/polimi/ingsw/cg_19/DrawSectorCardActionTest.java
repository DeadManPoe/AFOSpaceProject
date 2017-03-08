package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.DrawSectorCardAction;

/**
 * Some tests for the DrawSectorCardAction class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DrawSectorCardActionTest {

	@Test
	public void testToString() {
		DrawSectorCardAction action = new DrawSectorCardAction();
		assertEquals("DrawActionFromSector []", action.toString());
	}

}

package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TeleportObjectCard;

/**
 * Some tests for the TeleportObjectCard class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class TeleportObjectCardTest {

	@Test
	public void testToString() {
		TeleportObjectCard card = new TeleportObjectCard();
		assertEquals("TeleportObjectCard", card.toString());
	}

}

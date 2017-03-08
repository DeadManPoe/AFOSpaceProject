package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.SilenceSectorCard;

/**
 * Some tests for the SilenceSectorCard class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class SilenceSectorCardTest {

	/**
	 * Checks that the constructor correctly initializes the Silence card
	 */
	@Test
	public void testSilenceSectorCard() {
		SilenceSectorCard card = new SilenceSectorCard();
		assertFalse(card.hasObjectAssociated());
	}

	@Test
	public void testToString() {
		SilenceSectorCard card = new SilenceSectorCard();
		assertEquals("SilenceSectorCard", card.toString());
	}

}

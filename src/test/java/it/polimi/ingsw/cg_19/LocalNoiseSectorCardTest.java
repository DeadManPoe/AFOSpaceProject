package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.LocalNoiseSectorCard;

/**
 * Some tests for the LocalNoiseSectorCard
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class LocalNoiseSectorCardTest {

	/**
	 * Checks that the constructor initializes the card correctyl
	 */
	@Test
	public void testLocalNoiseSectorCard() {
		LocalNoiseSectorCard card = new LocalNoiseSectorCard(false);
		assertFalse(card.hasObjectAssociated());
	}

	@Test
	public void testToString() {
		LocalNoiseSectorCard card = new LocalNoiseSectorCard(false);
		assertEquals("LocalNoiseSectorCard", card.toString());
	}

}

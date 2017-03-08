package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AdrenalineObjectCard;

/**
 * Some tests for the AdrenalineObjectCard class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class AdrenalineObjectCardTest {

	@Test
	public void testToString() {
		AdrenalineObjectCard card = new AdrenalineObjectCard();
		assertEquals("AdrenalineObjectCard", card.toString());
	}

}

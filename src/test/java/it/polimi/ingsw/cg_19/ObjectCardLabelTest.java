package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AdrenalineObjectCard;

import client.ObjectCardLabel;

/**
 * Test for ObjectCardLabel
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ObjectCardLabelTest {

	/**
	 * Checks that the getCard method returns the correct card after
	 * initializing the object
	 */
	@Test
	public void testObjectCardLabel() {
		AdrenalineObjectCard card = new AdrenalineObjectCard();
		ObjectCardLabel lbl = new ObjectCardLabel(card);
		assertEquals(card, lbl.getCard());
	}
}

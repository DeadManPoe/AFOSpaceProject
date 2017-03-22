package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;
import common.AdrenalineObjectCard;
import common.DiscardAction;
import common.ObjectCard;

/**
 * Some tests for the DiscardAction
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class DiscardActionTest {

	/**
	 * Checks that the getObjectCard method returns the AdrenalineObjectCard
	 */
	@Test
	public void testGetObjectCard() {
		ObjectCard card = new AdrenalineObjectCard();
		DiscardAction action = new DiscardAction(card);
		assertEquals(card, action.payload);
	}

}

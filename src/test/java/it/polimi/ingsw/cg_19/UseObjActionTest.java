package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AdrenalineObjectCard;
import common.SuppressorObjectCard;
import common.UseObjAction;

/**
 * Some tests for the UseObjBeforeAction Class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 */
public class UseObjActionTest {

	/**
	 * Test for the constructor of UseObjAction
	 */
	@Test
	public void testUseObjAction() {
		AdrenalineObjectCard card = new AdrenalineObjectCard();
		SuppressorObjectCard card2 = new SuppressorObjectCard();
		UseObjAction action = new UseObjAction(card);
		assertEquals(card, action.getObjectCard());
		assertNotEquals(card2, action.getObjectCard());
	}

	/**
	 * Test for the equals method of UseObjAction
	 */
	@Test
	public void testEqualsObject() {
		AdrenalineObjectCard card = new AdrenalineObjectCard();
		UseObjAction action = new UseObjAction(card);
		assertTrue(action.equals(new UseObjAction(card)));
		assertFalse(action.equals(null));
		assertFalse(action.equals(card));
	}

}

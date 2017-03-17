/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.DefenseObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import effects.DefenseObjCardEffect;

*/
/**
 * Some tests for the DefenseObjCardEffect
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class DefenseObjCardEffectTest {

	*/
/**
	 * Checks that the executeEffect of the card return true
	 *//*

	@Test
	public void testExecuteEffect() {
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();
		DefenseObjectCard card = new DefenseObjectCard();
		DefenseObjCardEffect effect = new DefenseObjCardEffect(card);

		Game game = new Game("FERMI");
		assertTrue(effect.executeEffect(game, n1, n2));
	}

	*/
/**
	 * Checks that the getObjectCard method return the DefenseObjectCard
	 *//*

	@Test
	public void testDefenseEffectDefenseObjectCard() {
		DefenseObjectCard card = new DefenseObjectCard();
		DefenseObjCardEffect effect = new DefenseObjCardEffect(card);
		assertEquals(card, effect.getObjectCard());
	}

}
*/

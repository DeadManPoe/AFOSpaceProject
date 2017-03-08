package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.AdrenalineObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import effects.AdrenalineObjCardEffect;

/**
 * Some tests for the AdrenalineObjCardEffect class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class AdrenalineObjCardEffectTest {

	/**
	 * Checks that after using an adrenaline card the player is adrenalined
	 */
	@Test
	public void testExecuteEffect() {
		RRClientNotification n1 = new RRClientNotification();
		PSClientNotification n2 = new PSClientNotification();
		AdrenalineObjectCard card = new AdrenalineObjectCard();
		AdrenalineObjCardEffect effect = new AdrenalineObjCardEffect(card);

		Game game = new Game("GALILEI");
		Player p = new Player(PlayerType.HUMAN, "Andrea");
		game.addPlayer(p);
		assertTrue(effect.executeEffect(game, n1, n2));
		assertTrue(p.isAdrenaline());
	}

	/**
	 * Checks that the getObjectCard method return the AdrenalineObjectCard
	 * correctly
	 */
	@Test
	public void testAdrenalineEffect() {
		AdrenalineObjectCard card = new AdrenalineObjectCard();
		AdrenalineObjCardEffect effect = new AdrenalineObjCardEffect(card);
		assertEquals(card, effect.getObjectCard());
	}

}

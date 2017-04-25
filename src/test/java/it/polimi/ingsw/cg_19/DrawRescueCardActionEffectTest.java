/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;
import common.DrawRescueCardAction;
import common.PSClientNotification;
import common.RRClientNotification;
import effects.DrawRescueCardEffect;

*/
/**
 * Some tests for the DrawRescueCardActionEffect
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class DrawRescueCardActionEffectTest {

	*/
/**
	 * Test for the executionEffect method It checks if in a real game(the map
	 * loaded is fermi) the effect of drawing a card from the rescue deck is
	 * executed correctly
	 *//*

	@Test
	public void testExecuteEffect() {
		RRClientNotification clientNotification = new RRClientNotification();
		PSClientNotification psNotification = new PSClientNotification();

		Player player1 = new Player(PlayerType.HUMAN, "");
		Player player2 = new Player(PlayerType.ALIEN, "");

		Game game = new Game("FERMI");
		game.addPlayer(player1);
		game.addPlayer(player2);
		game.startGame();

		player1.setSector(game.getMap().getHumanSector());
		player2.setSector(game.getMap().getAlienSector());
		game.getMap().getAlienSector().addPlayer(player2);
		game.getMap().getHumanSector().addPlayer(player1);

		DrawRescueCardAction action = new DrawRescueCardAction();
		DrawRescueCardEffect effect = new DrawRescueCardEffect(action);

		assertTrue(effect.executeEffect(game, clientNotification,
				psNotification));
	}

	*/
/**
	 * Test for getAction method It checks if given a DrawRescueCardActionEffect
	 * initialized with a null action, the getAction method returns null
	 *//*

	@Test
	public void testDrawRescueCardActionEffect_1() {
		DrawRescueCardEffect effect = new DrawRescueCardEffect();
		assertEquals(null, effect.getAction());
	}

	*/
/**
	 * Test for getAction method It checks if given a DrawRescueCardActionEffect
	 * initialized with a not null action, the getAction method returns the
	 * action correctly
	 *//*

	@Test
	public void testDrawRescueCardActionEffect_2() {
		DrawRescueCardAction action = new DrawRescueCardAction();
		DrawRescueCardEffect effect = new DrawRescueCardEffect(action);
		assertEquals(action, effect.getAction());
	}

}
*/

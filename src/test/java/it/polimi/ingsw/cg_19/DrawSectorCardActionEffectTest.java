/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;
import common.DrawSectorCardAction;
import common.PSClientNotification;
import common.RRClientNotification;
import effects.DrawSectorCardEffect;

*/
/**
 * Some tests for the DrawActionFromSectorEfffect
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *//*

public class DrawSectorCardActionEffectTest {

	*/
/**
	 * Test for the executeEffect() method It checks that in a "small" game with
	 * two safe sector connected to each other the effect of the draw action is
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

		DrawSectorCardAction action = new DrawSectorCardAction();
		DrawSectorCardEffect effect = new DrawSectorCardEffect(action);

		assertTrue(effect.executeEffect(game, clientNotification,
				psNotification));
	}

	*/
/**
	 * Test for the getAction method It checks that given a
	 * DrawActionFromSectorEffect initialized with null action the getAction
	 * method return a null
	 *//*

	@Test
	public void testDrawActionFromSectorEffect_1() {
		DrawSectorCardEffect effect = new DrawSectorCardEffect();
		assertEquals(null, effect.getAction());
	}

	*/
/**
	 * Test for the getAction method It checks that given a
	 * DrawActionFromSectorEffect initialized with a given action the getAction
	 * method returns it correctly
	 *//*

	@Test
	public void testDrawActionFromSectorEffect_2() {
		DrawSectorCardAction action = new DrawSectorCardAction();
		DrawSectorCardEffect effect = new DrawSectorCardEffect(action);
		assertEquals(action, effect.getAction());
	}

}
*/

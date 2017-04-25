/*
package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import org.junit.Test;

import common.DrawObjectCardAction;
import common.PSClientNotification;
import common.RRClientNotification;
import effects.DrawObjectCardEffect;

*/
/**
 * Some test for the DrawObjectCardActionEffect
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 *
 *//*

public class DrawObjectCardEffectTest {

	*/
/**
	 * Test for the executeEffect method. This test checks that after drawing 25
	 * card the 26th is null
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

		DrawObjectCardAction action = new DrawObjectCardAction();
		DrawObjectCardEffect effect = new DrawObjectCardEffect(action);
		int i = 0;
		while (i < 12) {
			effect.executeEffect(game, clientNotification, psNotification);
			i++;
		}

		effect.executeEffect(game, clientNotification, psNotification);
		assertTrue(psNotification.getMessage().contains("No more object cards"));
	}

	*/
/**
	 * Test for the empty constructor
	 *//*

	@Test
	public void textDrawObjectCardEffect() {
		DrawObjectCardEffect effect = new DrawObjectCardEffect();
		assertNull(effect.getAction());
	}

}
*/

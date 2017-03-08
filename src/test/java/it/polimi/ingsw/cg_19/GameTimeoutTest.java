package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import java.util.Timer;

import org.junit.Test;

import client.GameTimeout;

/**
 * Some tests for the GameTimeout tests
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GameTimeoutTest {

	/**
	 * Tests that the constructor initializes the class correctly
	 */
	@Test
	public void testGetTime() {
		Game game = new Game("FERMI");
		Timer timer = new Timer();
		GameTimeout timeout = new GameTimeout(null, game, timer);
		assertEquals(timeout, timeout);
	}
}

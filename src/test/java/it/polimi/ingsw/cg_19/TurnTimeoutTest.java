package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import java.util.Timer;

import org.junit.Test;

/**
 * Some tests for the TurnTimeout class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class TurnTimeoutTest {

	/**
	 * Tests that the constructor initializes the class correctly
	 */
	@Test
	public void testGetTime() {
		Game game = new Game("FERMI");
		Timer timer = new Timer();
		TurnTimeout timeout = new TurnTimeout(game, timer);
		assertEquals(game, timeout.getGame());
	}
}
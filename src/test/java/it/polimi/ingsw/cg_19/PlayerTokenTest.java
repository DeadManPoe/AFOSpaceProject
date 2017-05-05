package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import common.PlayerType;
import org.junit.Test;
import common.PlayerToken;

/**
 * Some tests for the PlayerToken class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class PlayerTokenTest {

	/**
	 * Checks that the constructor initializes the player type correctly
	 */
	@Test
	public void testPlayerToken() {
		PlayerType type = PlayerType.HUMAN;
		PlayerToken token = new PlayerToken(type,1);
		assertEquals(type, token.getPlayerType());
	}

	/**
	 * Test for the equals method
	 */
	@Test
	public void testEqualsObject() {
		PlayerToken token_1 = new PlayerToken(PlayerType.HUMAN,1);
		PlayerToken token_2 = new PlayerToken(PlayerType.HUMAN,1);
		PlayerToken token_3 = new PlayerToken(PlayerType.HUMAN,1);

		assertTrue(token_2.equals(token_2));
		assertFalse(token_1.equals(token_3));
		assertFalse(token_1.equals(null));
		assertFalse(token_1.equals(new String("hello")));
	}

}

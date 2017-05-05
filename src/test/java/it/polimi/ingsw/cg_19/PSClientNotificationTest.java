package it.polimi.ingsw.cg_19;

import static org.junit.Assert.*;

import common.PlayerType;
import org.junit.Test;

import common.PSClientNotification;
import common.PlayerToken;

/**
 * Some tests for PSClientNotification class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class PSClientNotificationTest {

	/**
	 * Checks that a player is added correctly to the list of dead players
	 */
	@Test
	public void testGetAddDeadPlayers() {
		PSClientNotification ps = new PSClientNotification();
		PlayerToken p = new PlayerToken(PlayerType.HUMAN, 1);
		ps.addDeadPlayers(p);
		assertEquals(p, ps.getDeadPlayers().get(0));
	}

	/**
	 * Checks that a player is added correctly to the list of attacked players
	 */
	@Test
	public void testGetAddAttackedPlayers() {
		PSClientNotification ps = new PSClientNotification();
		PlayerToken p = new PlayerToken(PlayerType.HUMAN,1);
		ps.addAttackedPlayers(p);
		assertEquals(p, ps.getAttackedPlayers().get(0));
	}

}

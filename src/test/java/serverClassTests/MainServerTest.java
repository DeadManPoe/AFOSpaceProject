package serverClassTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import server.GameManager;
import server.MainServer;

/**
 * Some test for the MainServer class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class MainServerTest {
	private MainServer server;
	private ServerConnection serverConn = new ServerConnection(000, 000, "");

	/**
	 * Checks that the getGameManager return the correct reference to the
	 * gameManager
	 * 
	 * @throws IOException
	 */
	@Test
	public void getGameManagerTest() throws IOException {
		server = new MainServer(serverConn);
		assertTrue(GameManager.getInstance() == server.getGameManager());
		assertEquals(GameManager.getInstance(), server.getGameManager());
	}
}

package serverClassTests;

import static org.junit.Assert.*;

import org.junit.Test;

import server.ServerConnection;

/**
 * Some tests for the ServerConnection class
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ServerConnectionTest {
	private ServerConnection serverConnection;

	/**
	 * Checks that the getRegistryPort returns the correct port
	 */
	@Test
	public void getRegistryPortTest() {
		serverConnection = new ServerConnection(0000, 0000, "sadasd");
		assertEquals(0000, serverConnection.getRegistryPort());
	}

	/**
	 * Checks that the getRegistryPort returns the correct port
	 */
	@Test
	public void getSocketPortTest() {
		serverConnection = new ServerConnection(0000, 0000, "sadasd");
		assertEquals(0000, serverConnection.getSocketPort());
	}

	/**
	 * Checks that the getServiceName returns the correct service name
	 */
	@Test
	public void getServiceNameTest() {
		serverConnection = new ServerConnection(0000, 0000, "sadasd");
		assertEquals("sadasd", serverConnection.getServiceName());
		serverConnection = new ServerConnection(0000, 0000, "");
		assertEquals("", serverConnection.getServiceName());
	}

}

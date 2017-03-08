package server;

/**
 * Represents a container of the server's connection details
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ServerConnection {
	// Rmi registry port
	private final int REGISTRYPORT;
	// Socket port
	private final int SOCKETPORT;
	// The name of the server's rmi exposed group of services
	private final String SERVICENAME;

	/**
	 * Constructs a container of the server's connection details. This container
	 * is constructed from: a port used by the server to publish the rmi
	 * registry, a port used by the server to communicate with the client using
	 * sockets and the name of the server's rmi exposed group of methods
	 * 
	 * @param registryPort
	 *            the port used to publish the rmi registry
	 * @param socketPort
	 *            the port used to communicate with the client using sockets
	 * @param serviceName
	 *            the name of the server's rmi exposed group of methods
	 */
	public ServerConnection(int registryPort, int socketPort, String serviceName) {
		this.REGISTRYPORT = registryPort;
		this.SOCKETPORT = socketPort;
		this.SERVICENAME = serviceName;
	}

	/**
	 * Gets the port used by the server to publish the rmi registry
	 * 
	 * @return the port used by the server to publish the rmi registry
	 */
	public int getRegistryPort() {
		return REGISTRYPORT;
	}

	/**
	 * Gets the port used by the server to communicate with the client using
	 * sockets
	 * 
	 * @return the port used by the server to communicate with the client using
	 *         sockets
	 */
	public int getSocketPort() {
		return SOCKETPORT;
	}

	/**
	 * Gets the name of the server's rmi exposed group of methods
	 * 
	 * @return the name of the server's rmi exposed group of methods
	 */
	public String getServiceName() {
		return SERVICENAME;
	}
}

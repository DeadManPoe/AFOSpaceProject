package client;

/**
 * Represents a container of the client's connection details
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ClientConnection {
	// The port used by a client to communicate with a server
	private final int PORT;
	// The ip address of the server
	private final String HOST;
	// The name of the gropu of services exposed via rmi by the server
	private final String RMISERVICENAME;

	/**
	 * Constructs a connection details container from the port used to
	 * communicate with the server, from the sever's ip address and from the
	 * name of the server's group of methods exposed via rmi
	 * 
	 * @param port
	 *            the port used by the client to communicate with the server
	 * @param host
	 *            the server's ip address
	 * @param rmiServiceName
	 *            the name of the server's set of services exposed via rmi
	 */
	public ClientConnection(int port, String host, String rmiServiceName) {
		PORT = port;
		HOST = host;
		RMISERVICENAME = rmiServiceName;
	}

	/**
	 * Constructs a connection details container from the port used to
	 * communicate with the server and from the server's ip address. These
	 * connection details are the only ones that matter in a socket based
	 * communication.
	 * 
	 * @param port
	 *            the port used by a client to communicate with a server
	 * @param host
	 *            the server's ip address
	 */
	public ClientConnection(int port, String host) {
		PORT = port;
		HOST = host;
		RMISERVICENAME = null;
	}

	/**
	 * Gets the name of the sever's group of methods exposed via rmi
	 * 
	 * @return the name of the server's group of methods exposed via rmi
	 */
	public String getRmiServiceName() {
		return RMISERVICENAME;
	}

	/**
	 * Gets the port used to communicate with the server
	 * 
	 * @return the port used to communicate with the server
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * Gets the server's ip address
	 * 
	 * @return the server's ip address
	 */
	public String getHost() {
		return HOST;
	}

}

package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Represents a thread that handles a request by a client in the logic of the
 * client server pattern
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class SocketThread extends Thread {
	// The server this thread refers to
	private MainServer server;
	// The socket used to handle the client's request
	private Socket socket;

	/**
	 * Constructs a thread that handles a request by a client in the logic of
	 * the client server pattern. This thread is constructed from a server and a
	 * socket that represents the client itself and that will be used to handle
	 * the client's request
	 * 
	 * @param server
	 *            the server this thread refers to
	 * @param socket
	 *            the socket this thread uses to handle the client's request and
	 *            that represents the client itself
	 */
	public SocketThread(MainServer server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	/**
	 * Runs the thread. The thread handles the client's request by receiving the
	 * client data, processing it and invoking on the client a remote method,
	 * all is done through a {@link SocketRemoteDataExchange}
	 * 
	 * @see SocketRemoteDataExchange
	 */
	public void run() {
		SocketRemoteDataExchange dataExchange;
		try {
			// Client's request handling
			dataExchange = new SocketRemoteDataExchange(server, socket);
			server.setSocketDataExchange(dataExchange);
			dataExchange.receiveData();
		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			ServerLogger.getLogger().log(Level.SEVERE,
					"Could not perform action | SocketThread", e);
		} catch (IOException e) {
			ServerLogger.getLogger().log(Level.SEVERE,
					"Could not communicate with the client | SocketThread", e);
		}

	}
}

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.logging.Level;

import common.RemoteMethodCall;

/**
 * Represents a thread that handles async messages from the server in the logic
 * of the pub/sub pattern(the client is the subscriber)
 * 
 * @see ClientRemoteServices
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class PubSubHandler extends Thread {
	/** The socket used to receive async messages from the server */
	private Socket socket;
	/** The methods the client offers to the server in order to exchange data */
	private ClientRemoteServices clientServices;
	/** The object input stream associated with the socket of the thread */
	private ObjectInputStream input;

	/**
	 * Constructs a thread that handles async messages from the server in the
	 * logic of the pub/sub pattern(the client is the subscriber). The socket
	 * used by the thead to communicate with the server is provided; the methods
	 * the client offers to the server in order to exchange data are provided
	 * 
	 * @param socket
	 *            the socket used by the thead to communicate with the server
	 * @param clientServices
	 *            the methods the client offers to the server in order to
	 *            exchange data
	 * @throws IOException
	 */
	public PubSubHandler(ObjectInputStream stream, ClientRemoteServices clientServices)
			throws IOException {
		this.socket = socket;
		this.input = stream;
		this.clientServices = clientServices;
	}

	/**
	 * Runs the thread. The thread waits for messages from the server and
	 * processes them
	 */
	public void run() {
		RemoteMethodCall methodCall;
		while (true) {
			try {
				methodCall = (RemoteMethodCall) input.readObject();
				clientServices.processRemoteInvocation(methodCall);
			} catch (ClassNotFoundException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"reflection error on client | PubSubHandler", e);
			} catch (IOException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"com error on client | PubSubHandler", e);
			} catch (IllegalAccessException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"reflection error on client | PubSubHandler", e);
			} catch (InvocationTargetException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"reflection error on client | PubSubHandler", e);
			} catch (NoSuchMethodException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"reflection error on client | PubSubHandler", e);
			} catch (SecurityException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"com error on client | PubSubHandler", e);
			}

		}
	}
}

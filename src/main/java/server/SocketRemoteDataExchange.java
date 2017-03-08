package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.logging.Level;

import common.RemoteMethodCall;

/**
 * Represents an exchange of data between the server and the client using a
 * socket based communication. The data exchanged between the server and the
 * client is in the form of remote method calls.
 *
 * @see MainServer
 * @see ServerServicesViaSocket
 * @see RemoteMethodCall
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class SocketRemoteDataExchange {
	// The server this remote data exchange refers to
	private MainServer server;
	// The method the server offers to the client in order to exchange data
	private ServerServicesViaSocket serverServices;
	// The socket used by the server to communicate with the client
	private Socket socket;
	// The server's output stream
	private ObjectOutputStream outputStream;
	// The server's input stream */
	private ObjectInputStream inputStream;

	/**
	 * Constructs an exchange of data between the server and the client using a
	 * socket based communication. This remote data exchange is constructed from
	 * a server and a socket used by the server to communicate with the client
	 *
	 * @param server
	 *            the server this remote data exchange refers to
	 * @param socket
	 *            the socket used by the server to communicate with the client
	 * @throws IOException
	 */
	public SocketRemoteDataExchange(MainServer server, Socket socket)
			throws IOException {
		this.socket = socket;
		this.server = server;
		this.serverServices = server.getServicesViaSocket();
		outputStream = new ObjectOutputStream(this.socket.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(this.socket.getInputStream());
	}

	/**
	 * Receives and processes data from the client
	 *
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void receiveData() throws ClassNotFoundException, IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		RemoteMethodCall remoteMethodCall = (RemoteMethodCall) inputStream
				.readObject();
		ServerLogger.getLogger().log(Level.INFO, remoteMethodCall.toString());
		// Method invocation from the remote method call object
		serverServices.processRemoteInvocation(remoteMethodCall);
	}

	/**
	 * Sends data to the client
	 *
	 * @param remoteMethodCall
	 *            the remote method the server wants to call on the client
	 * @throws IOException
	 */
	public void sendData(RemoteMethodCall remoteMethodCall) throws IOException {
		outputStream.writeObject(remoteMethodCall);
		outputStream.flush();
		// sendPubNotification and sendToken
		if (!remoteMethodCall.getMethodName().equals("sendPubNotification")
				&& !remoteMethodCall.getMethodName().equals("sendToken")) {
			this.closeDataFlow();
		}
	}

	/**
	 * Closes the communication between the server and the client
	 *
	 * @throws IOException
	 */
	public void closeDataFlow() throws IOException {
		outputStream.close();
		inputStream.close();
		socket.close();
	}

	/**
	 * Keeps alive the communication between the server and the client, by
	 * creating an appropriate thread that handles the messages to the
	 * subscribers of a topic in the logic of the pub/sub pattern
	 * 
	 * @throws IOException
	 */
	public SubscriberHandler keepAlive() throws IOException {
		SubscriberHandler handler = new SocketSubscriberHandler(socket);
		server.addPubSubThread(handler);
		return handler;
	}

}

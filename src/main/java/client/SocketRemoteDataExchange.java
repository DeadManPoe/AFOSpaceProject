package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.rmi.NotBoundException;

import common.RemoteMethodCall;

/**
 * Represents a data exchange between the client and the server using socket
 * based communication
 * 
 * @see RemoteDataExchange
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class SocketRemoteDataExchange extends RemoteDataExchange {
	/*
	 * The object output stream associated with the socket used by the remote
	 * data exchange
	 */
	private ObjectOutputStream outputStream;
	/*
	 * The object input stream associated with the socket used by the remote
	 * data exchange
	 */
	private ObjectInputStream inputStream;
	// The socket with the remote data exchange
	private Socket socket;
	// The client connection details
	private ClientConnection connection;

	/**
	 * Constructs a data exchange between the client and the server using socket
	 * based communication, creating a socket with its associated object
	 * streams. This data exchange is constructed from a client.
	 * 
	 * @param client
	 *            the client this data exchange refers to
	 * @throws IOException
	 *             signals an error in the opening of the socket's streams
	 *             associated with the remote data exchange
	 */
	public SocketRemoteDataExchange(Client client) throws IOException {
		super(client);
		connection = client.getConnection();
		clientServices = client.getClientServices();
		try {
			socket = new Socket(connection.getHost(), connection.getPort());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			socket.close();
			throw new IOException(e);
		}

	}

	/**
	 * @see RemoteDataExchange#sendData
	 */
	@Override
	public void sendData(RemoteMethodCall remoteCall) throws IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, NotBoundException {
		try {
			outputStream.writeObject(remoteCall);
			outputStream.flush();
		} catch (IOException e) {
			socket.close();
			throw new IOException(e);
		}

	}

	/**
	 * Closes the data flow relative to the data exchange
	 * 
	 * @throws IOException
	 *             signals an error in closing the remote data exchange's
	 *             associated socket
	 */
	public void closeDataFlow() throws IOException {
		socket.close();
	}

	/**
	 * @see RemoteDataExchange#keepAlive
	 * @throws IOException
	 *             signals an error in the creation of the thread used by the
	 *             client to receive async notifications from the server in the
	 *             logic of the pub/sub pattern
	 */
	@Override
	public void keepAlive() throws IOException {
		client.startPubSub(socket, clientServices);
	}

	/**
	 * @see RemoteDataExchange#receiveData
	 */
	@Override
	public void receiveData() throws ClassNotFoundException, IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		RemoteMethodCall remoteCall = (RemoteMethodCall) inputStream
				.readObject();
		// Data processing: from the remote method call object to the actual
		// method invocation
		clientServices.processRemoteInvocation(remoteCall);

	}
}

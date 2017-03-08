package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;

import common.RemoteMethodCall;

/**
 * Represents a data exchange between the client and the server either using a
 * socket based or a rmi based communication. The data exchanged between the
 * client and the server is in the form of remote method calls.
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public abstract class RemoteDataExchange {
	// The client the data exchange refers to
	protected Client client;
	// The methods the client offers to the server in order to exchange data */
	protected ClientRemoteServices clientServices;

	/**
	 * Constructs a data exchange between the client and the server from a
	 * client and the methods that client offers to the server in order to
	 * exchange data
	 * 
	 * @param client
	 *            the client the data exchange refers to
	 * @param clientRemoteServices
	 *            the methods the client offers to the server to exchange data
	 */
	public RemoteDataExchange(Client client) {
		this.client = client;
		this.clientServices = client.getClientServices();
	}

	/**
	 * Receives and processes data from the server
	 * 
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 */
	public abstract void receiveData() throws ClassNotFoundException,
			IOException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException;

	/**
	 * Sends a remote method call to the server
	 * 
	 * @param remoteCall
	 *            the method call to be sent to the server
	 * @throws IOException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws InvocationTargetException
	 *             signals a com. error
	 * @throws NoSuchMethodException
	 *             signals a com. error
	 * @throws SecurityException
	 *             signals a com. error
	 * @throws ClassNotFoundException
	 *             signals a com. error
	 * @throws NotBoundException
	 *             signals a com. error
	 */
	public abstract void sendData(RemoteMethodCall remoteCall)
			throws IOException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, NotBoundException;

	/**
	 * Closes the data flow between the client and the server
	 * 
	 * @throws IOException
	 *             signals an error in closing the data flow between the client
	 *             and the server
	 */
	public abstract void closeDataFlow() throws IOException;

	/**
	 * Keeps alive the data flow between the client and the server and creates a
	 * new thread to manage that data flow; this thread is a thread that handles
	 * async messages from the server to the client(the subscriber), in the
	 * logic of the pub/sub pattern
	 * 
	 * @throws IOException
	 *             signals an error the the creation the thread that handles
	 *             async messages from the server to the client
	 */
	public abstract void keepAlive() throws IOException;

}

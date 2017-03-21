package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.RemoteMethodCall;

/**
 * Represents a complete communication procedure between the client and the
 * server. The client sends a request, waits for the server to response and
 * closes or maintains the connection. The connection is maintained when the
 * client requests the invocation of the "joinNewGame" or of the "joinGame"
 * methods, that's because these methods subscribe the client to a topic in the
 * logic of the pub/sub pattern. This subscription requires a continuously
 * opened connection and a thread that processes the async messages sent to the
 * client by the server.
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ComSession {
	private Client client;

	/**
	 * Constructs a complete communication procedure between the client and the
	 * server. This procedure is constructed from a client and from the name and
	 * the paramaters of the remote method the client wants to call on the
	 * server in order to exchange data
	 * 
	 * @see RemoteDataExchange
	 * @see RemoteMethodCall
	 * @param client
	 *            the client the communication procedure refers to. This
	 *            reference is used to manipulate only communication sessions
	 *            defined for the client
	 * @param methodToCall
	 *            the name of the remote method the client wants to call on the
	 *            server in order to exchange data
	 * @param methodParams
	 *            the parameters of the remote method the client wants to call
	 *            on the server in order to exchange data
	 * @throws RemoteException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
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
	public ComSession(Client client) {
		this.client = client;
	}

	public void start(String methodToCall, ArrayList<Object> methodParams)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		RemoteDataExchange session = client.getDataExcFactory().make();
		session.sendData(new RemoteMethodCall(methodToCall, methodParams));
		session.receiveData();
		session.closeDataFlow();
	}

	/**
	 * Constructs a complete communication procedure between the client and the
	 * server with the client and the name of the remote method that the client
	 * wants to call on the server in order to exchange data. In this case the
	 * remote method to be called on the server accepts no parameters
	 * 
	 * @param client
	 *            the client the communication procedure refers to. This
	 *            reference is used to manipulate only communication sessions
	 *            defined for the client
	 * @param methodToCall
	 *            the name of the remote method the client wants to call on the
	 *            server in order to exchange data
	 * @throws RemoteException
	 *             signals a com. error
	 * @throws IOException
	 *             signals a com. error
	 * @throws IllegalAccessException
	 *             signals a com. error
	 * @throws IllegalArgumentException
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
	public void start(String methodToCall) throws RemoteException, IOException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			NotBoundException {
		RemoteDataExchange session = client.getDataExcFactory().make();
		session.sendData(new RemoteMethodCall(methodToCall));
		session.receiveData();
		if (methodToCall.equals("joinGame")
				|| methodToCall.equals("joinNewGame")) {
			session.keepAlive();
		} else {
			session.closeDataFlow();
		}
	}
}

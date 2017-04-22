package client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import common.RemoteMethodCall;

/**
 * Represents an exchange of data between the client and the server using a rmi
 * based communication
 * 
 * @see RemoteDataExchange
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class RmiRemoteDataExchange extends RemoteDataExchange {
	// The client connection details
	private ClientConnection connection;
	// The rmi registry that contains the server's stub
	private Registry registry;

	/**
	 * Constructs a rmi based data exchange between the client and the server
	 * and locates the registry that contains the methods the server exposes to
	 * the client via rmi. This data exchange is constructed from a client.
	 * 
	 * @param client
	 *            the client associated with the data exchange
	 * @throws RemoteException
	 *             signals an error in the getting of the rmi registry
	 */
	public RmiRemoteDataExchange(Client client) throws RemoteException {
		super(client);
		connection = client.getConnection();
		registry = LocateRegistry.getRegistry(connection.getHost());
	}

	/*
	 * To maintain a consistent behavior between the RmiComSession and the
	 * SocketComSession, the method below still accepts a RemoteMethodCall
	 * object that specifies the name and the parameters of the method to invoke
	 * using rmi. From this object, using reflection, the invocation is
	 * performed
	 */
	@Override
	public void sendData(RemoteMethodCall remoteCall) throws IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, NotBoundException {
		ServerServicesViaRmiInt serverServices = (ServerServicesViaRmiInt) registry
				.lookup(connection.getRmiServiceName());
		// Invokes the right method exposed by rmi
		String methodName = remoteCall.getMethodName();
		ArrayList<Object> parameters = remoteCall.getMethodParameters();
		parameters.add(0, clientServices);
		Class<?>[] parametersClasses = new Class[parameters.size()];
		for (int i = 1; i < parameters.size(); i++) {
			if (parameters.get(i).getClass().getName().contains("Action")) {
				parametersClasses[i] = parameters.get(i).getClass()
						.getSuperclass();
			} else {
				parametersClasses[i] = parameters.get(i).getClass();

			}

		}
		// Rmi stubs must be interfaces so we change the type of the stub:
		// from ClientRemoteServices to ClientRemoteServicesInterface
		parametersClasses[0] = ClientRemoteServicesInterface.class;
		serverServices.getClass()
				.getDeclaredMethod(methodName, parametersClasses)
				.invoke(serverServices, parameters.toArray());
	}

	/**
	 * @see RemoteDataExchange#closeDataFlow
	 */
	@Override
	public void closeDataFlow() {
		/*
		 * With rmi there's no need and no way to close the data exchange
		 */
	}

	/**
	 * @see RemoteDataExchange#closeDataFlow
	 */
	@Override
	public void keepAlive() {
		/*
		 * With rmi there's no need to use a thread to receive async messages
		 * from the server
		 */
	}

	@Override
	public void receiveData() throws ClassNotFoundException, IOException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
	}

}

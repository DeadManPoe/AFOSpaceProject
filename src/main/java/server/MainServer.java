package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * Represents the server in the logic of the client/server pattern
 * 
 * @see SocketRemoteDataExchange
 * @see ServerConnection
 * @see GameManager
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class MainServer {
	// The server's connection
	private final ServerConnection connection;
	private final Handler fileHandler;
	// The socket the server uses to receive requests
	private final ServerSocket serverSocket;
	// The rmi registry the server uses to expose methods
	private Registry registry;
	// The server's associated game manager
	private volatile GameManager gameManager;
	// The server's associated pub/sub thread pool
	private ExecutorService pubSubThreadExecutor;
	// The server's associated request/response upon socket thread pool
	private ExecutorService socketThreadExecutor;
	// The server's current remote data exchange based on sockets
	private SocketRemoteDataExchange socketDataExchange;
	// The server's services via socket
	private ServerServicesViaSocket servicesViaSocket;
	// The server's services via rmi
	private ServerServicesViaRmi servicesViaRmi;

	/**
	 * Constructs a server from its connection details. An rmi registry to be
	 * used by the server to expose methods, the server's associated game
	 * manager, the server's associated pub/sub thread pool and the services the
	 * server offers to the client in order to exchange data via sockets or rmi
	 * are automatically created
	 * 
	 * @param connection
	 *            the server's connection details
	 * @throws IOException
	 */
	public MainServer(ServerConnection connection) throws IOException {
		this.connection = connection;
		this.registry = LocateRegistry.createRegistry(connection
				.getRegistryPort());
		this.serverSocket = new ServerSocket(connection.getSocketPort());
		this.gameManager = GameManager.getInstance();
		this.pubSubThreadExecutor = Executors.newCachedThreadPool();
		this.socketThreadExecutor = Executors.newCachedThreadPool();
		this.servicesViaRmi = new ServerServicesViaRmi(this);
		this.servicesViaSocket = new ServerServicesViaSocket(this);
		this.fileHandler = new FileHandler("serverLog.log");
		ServerLogger.getLogger().addHandler(fileHandler);
		this.fileHandler.setLevel(Level.ALL);
		ServerLogger.getLogger().setLevel(Level.ALL);

	}

	/**
	 * Starts a socket based communication
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void startSocketServer() throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException {
		ServerLogger.getLogger().log(Level.INFO, "Server is running");
		Socket socket;
		while (true) {
			// Accepts connections
			socket = serverSocket.accept();
			// Handles the request using a new thread
			socketThreadExecutor.submit(new SocketThread(this, socket));
		}
	}

	/**
	 * Starts a rmi based communication
	 * 
	 * @throws AccessException
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public void startRmiServer() throws AccessException, RemoteException,
			AlreadyBoundException {
		ServerLogger.getLogger().log(Level.INFO, "Server is running");
		// ServerRemoteServicesInterface serverStub = new ServerServicesViaRmi(
		// gameManager, this);
		ServerServicesViaRmiInt remote = (ServerServicesViaRmiInt) UnicastRemoteObject
				.exportObject(this.getServicesViaRmi(), 0);
		registry.bind(connection.getServiceName(), remote);
	}

	/**
	 * Adds to the server's pub/sub thread pool a thread used to perform async
	 * remote method calls on the subscribers.
	 * 
	 * @param handler
	 *            the thread used to send async messages to the subscribers
	 */
	public void addPubSubThread(SubscriberHandler handler) {
		this.pubSubThreadExecutor.submit(handler);
	}

	/**
	 * Gets the server's game manager
	 * 
	 * @return the server's game manager
	 */
	public GameManager getGameManager() {
		return gameManager;
	}

	/**
	 * Gets the server's current remote data exchange based on sockets
	 * 
	 * @return the server's current remote data exchange based on sockets
	 */
	public SocketRemoteDataExchange getSocketDataExchange() {
		return socketDataExchange;
	}

	/**
	 * Sets the server's current remote data exchange based on sockets
	 * 
	 * @param socketDataExchange
	 *            the new remote data exchange based on sockets associated with
	 *            the server
	 */
	public void setSocketDataExchange(
			SocketRemoteDataExchange socketDataExchange) {
		this.socketDataExchange = socketDataExchange;
	}

	/**
	 * Gets the services the server offers to the client in order to exchange
	 * data via sockets
	 * 
	 * @return the services the server offers to the client in order to exchange
	 *         data via sockets
	 */
	public ServerServicesViaSocket getServicesViaSocket() {
		return servicesViaSocket;
	}

	/**
	 * Gets the services the server offers to the client in order to exchange
	 * data via rmi
	 * 
	 * @return the services the server offers to the client in order to exchange
	 *         data via rmi
	 */
	public ServerServicesViaRmi getServicesViaRmi() {
		return servicesViaRmi;
	}

	public static void main(String[] args) throws AlreadyBoundException,
			IOException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException {
		ServerConnection connection = new ServerConnection(1099, 29999, "GAME");
		MainServer server = new MainServer(connection);
		server.startRmiServer();
		server.startSocketServer();

	}
}

package client;

import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import common.ClientNotification;
import common.GamePublicData;
import common.PSClientNotification;
import common.PlayerToken;
import common.PrivateDeck;
import common.RRClientNotification;
import common.RemoteMethodCall;
import factories.FermiGameMapFactory;
import factories.GalileiGameMapFactory;
import factories.GalvaniGameMapFactory;

/**
 * Represents an implementation of the {@link ClientRemoteServicesInterface}
 * interface
 * 
 * @see ClientRemoteServicesInterface
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ClientRemoteServices extends UnicastRemoteObject implements
		Serializable, ClientRemoteServicesInterface {

	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	// The client that offers these services
	private transient Client client;

	/**
	 * Constructs a group of services from the client that offers these
	 * services. These services are used to exchange data between the client and
	 * the server.
	 * 
	 * @param client
	 *            the client this group of services refers to
	 * @throws RemoteException
	 *             signals a com. error
	 */
	public ClientRemoteServices(Client client) throws RemoteException {
		this.client = client;
	}

	/**
	 * @see ClientRemoteServicesInterface#sendToken
	 * @throws IOException
	 *             signals a com. error
	 * @throws RemoteException
	 *             signals a com. error
	 */
	public void sendToken(PlayerToken token) throws IOException,
			RemoteException {
		client.setToken(token);
	}

	/**
	 * @see ClientRemoteServicesInterface#sendAvailableGames
	 * @throws IOException
	 *             signals a com. error
	 * @throws RemoteException
	 *             signals a com. error
	 */
	public void sendAvailableGames(ArrayList<GamePublicData> availableGames)
			throws IOException, RemoteException {
		client.setAvailableGames(availableGames);
	}

	/**
	 * @see ClientRemoteServicesInterface#publishChatMsg
	 * @throws IOException
	 *             signals a com. error
	 * @throws RemoteException
	 *             signals a com. error
	 */
	public void publishChatMsg(String msg) throws RemoteException {
		client.displayMessage(msg);
	}

	/**
	 * @see ClientRemoteServicesInterface#sendNotification
	 * @throws IOException
	 *             signals a com. error
	 * @throws RemoteException
	 *             signals a com. error
	 */
	@Override
	public void sendNotification(ClientNotification clientNotification)
			throws RemoteException, IOException {
		RRClientNotification clientNotificationa = (RRClientNotification) clientNotification;
		client.setNotification(clientNotificationa);
	}

	/**
	 * @see ClientRemoteServicesInterface#sendPubNotification
	 * @throws IOException
	 *             signals a com. error
	 * @throws RemoteException
	 *             signals a com. error
	 */
	@Override
	public void sendPubNotification(ClientNotification psNotification) {
		PSClientNotification notification = (PSClientNotification) psNotification;
		client.psNotify(notification);
	}

	/**
	 * Processes the details of a remote method call made by the server so that
	 * the right method with the specified parameters is invoked on the client
	 * 
	 * @see RemoteMethodCall
	 * @param remoteClientInvocation
	 *            an object that represents a remote method call. This object
	 *            specifies the name and the parameters of the method to be
	 *            invoked on the client
	 * @throws IllegalAccessException
	 *             signals a reflection error
	 * @throws InvocationTargetException
	 *             signals a reflection error
	 * @throws NoSuchMethodException
	 *             signals a reflection error
	 * @throws SecurityException
	 *             signals a reflection error
	 */
	public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String methodName = remoteClientInvocation.getMethodName();
		ArrayList<Object> parameters = remoteClientInvocation
				.getMethodParameters();
		Class<?>[] parametersClasses = new Class[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getClass().getName().contains("action")
					|| parameters.get(i).getClass().getName()
							.contains("ClientNotification")) {
				parametersClasses[i] = parameters.get(i).getClass()
						.getSuperclass();
			} else {
				parametersClasses[i] = parameters.get(i).getClass();
			}

		}
		this.getClass().getDeclaredMethod(methodName, parametersClasses)
				.invoke(this, parameters.toArray());
	}

	/**
	 * @see ClientRemoteServicesInterface#sendMap
	 */
	@Override
	public void sendMap(String mapName, PlayerToken playerToken) {
		if (mapName.equals("GALILEI")) {
			GalileiGameMapFactory factory = new GalileiGameMapFactory();
			GameMap map = factory.makeMap();
			client.setGameMap(map);
			client.setPrivateDeck(new PrivateDeck());
			if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
				client.setCurrentSector(map.getAlienSector());
			} else {
				client.setCurrentSector(map.getHumanSector());
			}

		} else if (mapName.equals("FERMI")) {
			FermiGameMapFactory factory = new FermiGameMapFactory();
			GameMap map = factory.makeMap();
			client.setGameMap(map);
			client.setPrivateDeck(new PrivateDeck());
			if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
				client.setCurrentSector(map.getAlienSector());
			} else {
				client.setCurrentSector(map.getHumanSector());
			}
		} else if (mapName.equals("GALVANI")) {
			GalvaniGameMapFactory factory = new GalvaniGameMapFactory();
			GameMap map = factory.makeMap();
			client.setGameMap(map);
			client.setPrivateDeck(new PrivateDeck());
			if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
				client.setCurrentSector(map.getAlienSector());
			} else {
				client.setCurrentSector(map.getHumanSector());
			}
		} else {
			throw new IllegalArgumentException("The type of map is undefined");
		}
		if (playerToken.equals(client.getToken())) {
			client.setIsMyTurn(true);
		}
		// START THE GAME
		synchronized (client) {
			client.setGameStarted(true);
			client.notifyAll();
		}
	}

	/**
	 * @see ClientRemoteServicesInterface#kick
	 */
	@Override
	public void kick(PlayerToken playerToken) {
		if (playerToken.equals(client.getToken())) {
			client.shutdown();
		}
	}

	/**
	 * @see ClientRemoteServicesInterface#allowTurn
	 */
	@Override
	public void allowTurn(PlayerToken playerToken) {
		if (playerToken.equals(client.getToken())) {
			client.setIsMyTurn(true);
		}
	}

	/**
	 * @see ClientRemoteServicesInterface#endGame
	 */
	@Override
	public void endGame() {
		synchronized (client) {
			client.setGameEnded(true);
			client.notifyAll();
		}
	}

	/**
	 * @see ClientRemoteServicesInterface#ackMessage
	 */
	@Override
	public void ackMessage() {
		return;
	}
}

package server;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ClientRemoteServicesInterface;
import common.Action;
import common.PlayerToken;

/**
 * Defines methods offered by the server to the client in order to exchange
 * data. These methods are offered by the server to the client using a rmi based
 * communication
 * 
 * @see PlayerToken
 * @see ClientRemoteServicesInterface
 * @see ActionContainer
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public interface ServerServicesViaRmiInt extends Remote {
	/**
	 * A service that sends to the client/player the list of all available games
	 * 
	 * @param clientServices
	 *            the services the client offers to the server to exchange data
	 *            via rmi (the client rmi stub)
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void getGames(ClientRemoteServicesInterface clientServices)
			throws RemoteException, IOException;

	/**
	 * A service that creates a new game upon a specified map, associate the
	 * client with the game and sends to the client a unique identifier used in
	 * future communications. A notification is sent to the client as well.
	 * 
	 * @param clientServices
	 *            the services the client offers to the server to exchange data
	 *            via rmi (the client rmi stub)
	 * @param gameMapName
	 *            the name of the map chosen by the client/player
	 * @throws RemoteException
	 * @throws IOException
	 */
	void joinNewGame(ClientRemoteServicesInterface clientServices,
			String gameMapName, String playerName) throws RemoteException,
			IOException;

	/**
	 * A service that makes the client to join a specified existing game and
	 * that sends to the client a unique identifier used in future
	 * communications . A notification is sent to the client as well.
	 * 
	 * @param clientServices
	 *            the services the client offers to the server to exchange data
	 *            via rmi (the client rmi stub)
	 * @param gameId
	 *            the id of the game the client wants to join
	 * @throws RemoteException
	 * @throws IOException
	 */
	void joinGame(ClientRemoteServicesInterface clientServices, Integer gameId,
			String playerName) throws RemoteException, IOException;

	/**
	 * A service that processes an action sent by the client/player and notifies
	 * the client/player
	 * 
	 * @param clientServices
	 *            the services the client offers to the server to exchange data
	 *            via rmi (the client rmi stub)
	 * @param action
	 *            the action sent by the client to the server to be performed on
	 *            the game
	 * @param playerToken
	 *            the client/player unique identifier
	 * @throws RemoteException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void makeAction(ClientRemoteServicesInterface clientServices,
			Action action, PlayerToken playerToken) throws RemoteException,
			IOException, InstantiationException, IllegalAccessException;

	/**
	 * A service that delivers a text message to all the subscribers of a
	 * specific topic in the logic of the pub/sub pattern
	 * 
	 * @param clientServices
	 *            the services the client offers to the server to exchange data
	 *            via rmi (the client rmi stub)
	 * @param message
	 *            the text message to be delivered to all the subscribers of a
	 *            specific topic in the logic of the pub/sub pattern
	 * @param token
	 *            the token of the player who wants to send the text message.
	 *            From this token the topic(a game), whose subscribers have to
	 *            be delivered the text message, is derived.
	 * @throws RemoteException
	 */
	public void publishGlobalMessage(
			ClientRemoteServicesInterface clientServices, String message,
			PlayerToken token) throws RemoteException;

	/**
	 * A services that allows a client to force(without waiting the timeout) the
	 * start of the game only if there are at least two player
	 * 
	 * @param token
	 *            The token of the player who wants to start the game
	 * 
	 * @throws RemoteException
	 */
	public void forceGameStart(ClientRemoteServicesInterface clientServices,
			PlayerToken token) throws RemoteException;

}
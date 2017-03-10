package client;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.ClientNotification;
import common.GamePublicData;
import common.PlayerToken;

/**
 * Represents a container that groups methods offered by the client to the
 * server in order to exchange data. These methods are offered by the client to
 * the server either using a socket based or a rmi based communication
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public interface ClientRemoteServicesInterface extends Remote {
	/**
	 * A service that sets the client's unique identifier
	 * 
	 * @param token
	 *            the new clien't unique identifier
	 * @throws IOException
	 *             signals a rmi com. error
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void sendToken(PlayerToken token) throws RemoteException,
			IOException;

	/**
	 * A service that shows to the client the games available.
	 * 
	 * @param availableGames
	 *            the list of available games
	 * @throws IOException
	 *             signals a rmi com. error
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void sendAvailableGames(ArrayList<GamePublicData> availableGames)
			throws RemoteException, IOException;

	/**
	 * A service that notifies the client with a message
	 * 
	 * @param notification
	 *            the notification to be sent to the client
	 * @throws IOException
	 *             signals a rmi com. error
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void sendNotification(ClientNotification notification)
			throws RemoteException, IOException;

	/**
	 * A service that notifies the client with a message delivered using a
	 * pub/sub logic so the client doesn't close the connection after the
	 * arrival of the message
	 * 
	 * @param notification
	 *            the notification to be sent to the client
	 * @throws RemoteException
	 *             rmi com. error
	 */
	public void sendPubNotification(ClientNotification notification)
			throws RemoteException;

	/**
	 * A service that shows to the client a chat message
	 * 
	 * @param message
	 *            the chat message to be shown to the client
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void publishChatMsg(String message) throws RemoteException;

	/**
	 * A service that loads the game map on the client
	 * 
	 * @param MapName
	 *            The name of the game map to load
	 * @throws IOException
	 *             signals a rmi com. error
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void sendMap(String MapName, PlayerToken playerToken)
			throws RemoteException;

	/**
	 * Allows the server to inform the player of the next current player
	 * 
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void allowTurn(PlayerToken playerToken) throws RemoteException;

	/**
	 * Allows the server to "kick" out of the game a player
	 * 
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void kick(PlayerToken playerToken) throws RemoteException;

	/**
	 * Allows the server to inform the client that the games will not be
	 * launched because the game hasn't reached the minimum number of player
	 * 
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void endGame() throws RemoteException;

	/**
	 * Allows the server to notify positively the client that has asked to send
	 * a chat message
	 * 
	 * @throws RemoteException
	 *             signals a rmi com. error
	 */
	public void ackMessage() throws RemoteException;
}

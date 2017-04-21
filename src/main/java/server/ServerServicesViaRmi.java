package server;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.ClientRemoteServicesInterface;
import common.Action;
import common.ClientNotification;
import common.GamePublicData;
import common.PlayerToken;
import common.RemoteMethodCall;

/**
 * Represents a container that groups methods offered by the server to the
 * client in order to exchange data. These methods are offered by the server to
 * the client using a rmi based communication
 * 
 * @see ServerServicesViaRmiInt
 * @see MainServer
 * @see Game
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ServerServicesViaRmi implements ServerServicesViaRmiInt {
	// The game manager associated with the container
	private GameManager gameManager;
	// The server associated with the container
	private MainServer server;

	/**
	 * Constructs a container that groups methods offered by the server to the
	 * client in order to exchange data via rmi. This container is constructed
	 * from a server.
	 * 
	 * @param server
	 *            the server the container refers to
	 */
	public ServerServicesViaRmi(MainServer server) {
		this.gameManager = server.getGameManager();
		this.server = server;
	}

	/**
	 * @see ServerServicesViaRmiInt#getGames
	 */
	@Override
	public synchronized void getGames(ClientRemoteServicesInterface clientServices)
			throws IOException {
		ArrayList<GamePublicData> gamesList = new ArrayList<GamePublicData>();
		for (Game game : this.gameManager.getGames()) {
			gamesList.add(game.getPublicData());
		}
		clientServices.sendAvailableGames(gamesList);

	}

	/**
	 * @see ServerServicesViaRmiInt#makeAction
	 */
	@Override
	public synchronized void makeAction(ClientRemoteServicesInterface clientServices,
			Action action, PlayerToken playerToken) throws IOException,
			InstantiationException, IllegalAccessException {
		Game game = gameManager.getGame(playerToken);
		ClientNotification[] notification = game.makeAction(action,
				playerToken, false);
		clientServices.sendNotification(notification[0]);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(notification[1]);
		game.notifySubscribers(new RemoteMethodCall("sendPubNotification",
				parameters));

	}

	/**
	 * @see ServerServicesViaRmiInt#joinNewGame
	 */
	@Override
	public synchronized void joinNewGame(ClientRemoteServicesInterface clientServices,
			String gameMapName, String playerName) throws IOException {
		Game game = new Game(gameMapName);
		this.gameManager.addGame(game);

		RmiSubscriberHandler handler = new RmiSubscriberHandler(clientServices);
		server.addPubSubThread(handler);
		PlayerToken playerToken = game.addPlayer(playerName);
		this.gameManager.addPlayerToGame(playerToken, game.getId());
		clientServices.sendToken(playerToken);
		game.addSubscriber(handler);

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add("You've joined a new game");

		game.notifySubscribers(new RemoteMethodCall("publishChatMsg", parameters));

	}

	/**
	 * @see ServerServicesViaRmiInt#joinGame
	 */
	@Override
	public synchronized void joinGame(ClientRemoteServicesInterface clientServices,
			Integer gameId, String playerName) throws IOException {
		Game game = this.gameManager.getGame(gameId);
		RmiSubscriberHandler handler = new RmiSubscriberHandler(clientServices);
		server.addPubSubThread(handler);
		PlayerToken playerToken = game.addPlayer(playerName);
		this.gameManager.addPlayerToGame(playerToken, gameId);
		clientServices.sendToken(playerToken);
		game.addSubscriber(handler);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add("A new player has joined the game");
		game.notifySubscribers(new RemoteMethodCall("publishChatMsg", parameters));
		if (this.gameManager.getGame(gameId).getPublicData().getPlayersCount() == 8)
			game.startGame();
		// game.startGame();

	}

	/**
	 * @see ServerServicesViaRmiInt#publishGlobalMessage
	 */
	@Override
	public synchronized void publishGlobalMessage(
			ClientRemoteServicesInterface clientServices, String message,
			PlayerToken token) throws RemoteException {
		Game game = this.gameManager.getGame(token);
		Player player = game.fromTokenToPlayer(token);
		clientServices.ackMessage();
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add("[" + player.getName() + "]: " + message);
		game.notifySubscribers(new RemoteMethodCall("publishChatMsg", parameters));
	}

	/**
	 * @throws RemoteException
	 * @throws IOException
	 * @see ServerServicesViaRmiInt#forceGameStart
	 */
	@Override
	public synchronized void forceGameStart(ClientRemoteServicesInterface clientServices,
			PlayerToken token) throws RemoteException {
		Game game = this.gameManager.getGame(token);
		if (game.getPublicData().getPlayersCount() > 1) {
			game.startGame();
		}
		clientServices.ackMessage();
	}

}

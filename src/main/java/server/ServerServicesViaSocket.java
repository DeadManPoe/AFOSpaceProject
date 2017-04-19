package server;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import common.Action;
import common.ClientNotification;
import common.GamePublicData;
import common.PlayerToken;
import common.RemoteMethodCall;

/**
 * Represents a container that groups methods offered by the server to the
 * client in order to exchange data. These methods are offered by the server to
 * the client using a socket based communication
 *
 * @see ServerServicesViaRmiInt
 * @see GameManager
 * @see MainServer
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class ServerServicesViaSocket {
	// The game manager associated with the container
	private volatile GameManager gameManager;
	// The server associated with the container
	private MainServer server;

	/**
	 * Constructs a container that groups methods offered by the server to the
	 * client in order to exchange data via sockets. This container is
	 * constructed from a server
	 * 
	 * @param server
	 *            the server this container refers to
	 */
	public ServerServicesViaSocket(MainServer server) {
		this.gameManager = server.getGameManager();
		this.server = server;
	}

	/**
	 * A service that sends to the client/player the list of all available games
	 * 
	 * @throws IOException
	 */
	public synchronized void getGames() throws IOException {
		List<GamePublicData> gamesList = new ArrayList<GamePublicData>();
		for (Game game : this.gameManager.getGames()) {
			gamesList.add(game.getPublicData());
		}
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(gamesList);
		server.getSocketDataExchange().sendData(
				new RemoteMethodCall("sendAvailableGames", parameters));
	}

	/**
	 * A service that creates a new game upon a map and associates that game to
	 * the client/player. A notification is sent to the client/player as well.
	 * 
	 * @param gameMapName
	 *            the name of map to be associated with the new game
	 * @param playerToken
	 *            the client/player unique identifier
	 * @throws IOException
	 */
	public synchronized void joinNewGame(String gameMapName, String playerName)
			throws IOException {
		Game game = new Game(gameMapName);
		this.gameManager.addGame(game);
		PlayerToken playerToken = game.addPlayer(playerName);
		this.gameManager.addPlayerToGame(playerToken, game.getId());
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(playerToken);
		server.getSocketDataExchange().sendData(
				new RemoteMethodCall("sendToken", parameters));
		SubscriberHandler handler = server.getSocketDataExchange().keepAlive();
		game.addSubscriber(handler);

		parameters.clear();
		parameters.add("You've joined a new game");
		game.notifyListeners(new RemoteMethodCall("publishChatMsg", parameters));

	}

	/**
	 * A service that associates the client/player to the specified existing
	 * game. A notification is sent to the client/player as well
	 * 
	 * @param gameId
	 *            the id of game the client wants to join
	 * @param playerToken
	 *            the client/player unique identifier
	 * @throws IOException
	 */
	public synchronized void joinGame(Integer gameId, String playerName) throws IOException {
		Game game = this.gameManager.getGame(gameId);
		PlayerToken playerToken = game.addPlayer(playerName);
		this.gameManager.addPlayerToGame(playerToken, gameId);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(playerToken);
		server.getSocketDataExchange().sendData(
				new RemoteMethodCall("sendToken", parameters));
		game.addSubscriber(server.getSocketDataExchange().keepAlive());
		parameters.clear();
		parameters.add("A new player joined the game");
		game.notifyListeners(new RemoteMethodCall("publishChatMsg", parameters));
		// game.startGame();
	}

	/**
	 * A service that processes the specified action sent by the client/player
	 * and notifies the client/player
	 * 
	 * @param action
	 *            the action sent by the client/player to be performed on the
	 *            game
	 * @param playerToken
	 *            the client/player unique identifier
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public synchronized void makeAction(Action action, PlayerToken playerToken)
			throws IOException, InstantiationException, IllegalAccessException {
		Game game = this.gameManager.getGame(playerToken);
		ArrayList<Object> parameters = new ArrayList<Object>();
		ClientNotification[] notification = game.makeAction(action,
				playerToken, false);
		parameters.add(notification[0]);
		server.getSocketDataExchange().sendData(
				new RemoteMethodCall("sendNotification", parameters));
		parameters.clear();
		parameters.add(notification[1]);
		game.notifyListeners(new RemoteMethodCall("sendPubNotification",
				parameters));
	}

	/**
	 * A service that delivers a text message to all the subscribers of a
	 * specific topic in the logic of the pub/sub pattern
	 * 
	 * @param message
	 *            the text message to be delivered to all the subscribers of a
	 *            specific topic in the logic of the pub/sub pattern
	 * @param token
	 *            the token of the player who wants to send the text message.
	 *            From this token the topic(a game), whose subscribers have to
	 *            be delivered the text message, is derived.
	 * @throws IOException
	 */
	public synchronized void publishGlobalMessage(String message,
			PlayerToken token) throws IOException {
		Game game = this.gameManager.getGame(token);
		Player player = game.fromTokenToPlayer(token);
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add("[" + player.getName() + "]: " + message);
		server.getSocketDataExchange().sendData(
				new RemoteMethodCall("ackMessage"));
		game.notifyListeners(new RemoteMethodCall("publishChatMsg", parameters));
	}

	/**
	 * A services that allows a client to force(without waiting the timeout) the
	 * start of the game only if there are at least two player
	 * 
	 * @param token
	 *            The token of the player who wants to start the game
	 * @throws IOException
	 */
	public synchronized void forceGameStart(PlayerToken token) {
		Game game = this.gameManager.getGame(token);
		if (game.getPublicData().getPlayersCount() > 1) {
			game.startGame();
		}
		try {
			server.getSocketDataExchange().sendData(
					new RemoteMethodCall("ackMessage"));
		} catch (IOException e) {
			ServerLogger.getLogger().log(Level.SEVERE,
					"Error in sending ackMessage rmethodcall", e);
		}
	}

	/**
	 * Processes a remote method call and invokes to corresponding method on the
	 * server.
	 * 
	 * @see common.RemoteMethodCall
	 * @param remoteServerInvocation
	 *            an object that represents a remote method call
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void processRemoteInvocation(RemoteMethodCall remoteServerInvocation)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		String methodName = remoteServerInvocation.getMethodName();
		ArrayList<Object> parameters = remoteServerInvocation
				.getMethodParameters();
		Class<?>[] parametersClasses = new Class[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).getClass().getName().contains("Action")
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

}

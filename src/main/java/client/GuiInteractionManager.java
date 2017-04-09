package client;

import common.*;
import it.polimi.ingsw.cg_19.PlayerType;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class is the component that manages the interaction between the user and
 * the GUI, it exposes a set standard method that allows the user to modify the
 * state of the game
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class GuiInteractionManager implements Observer {
	private JFrame mainFrame;
	private GUInitialWindow initialWindow;
	private GUIGameList gameListWindow;
	private Client client;
	private GUIGamePane GUIGamePane;
	private String playerName;
	private boolean myTurn;
	
	// The singleton instance of InteractionManager
	private static GuiInteractionManager instance;
	
	/**
	 * Constructs one and only one manager of the client-application
	 * interaction. No more than one manager of the client-application
	 * interaction could be constructed(pattern singleton). A manager of the
	 * client-application interaction is constructed from a client
	 * 
	 * @param client
	 *            the client the interaction manager refers to
	 * @return a singleton instance of interaction manager
	 */
	public static GuiInteractionManager init(Client client) {
		if (instance == null)
			instance = new GuiInteractionManager(client);
		return instance;
	}

	private GuiInteractionManager(Client client) {
		mainFrame = new JFrame("Escape from aliens in the outer space");

		this.client = client;
		client.addObserver(this);

		initialWindow = new GUInitialWindow();

		gameListWindow = new GUIGameList();
		gameListWindow
				.setLayout(new BoxLayout(gameListWindow, BoxLayout.Y_AXIS));
		gameListWindow.setBackground(Color.BLACK);

		GUIGamePane = new GUIGamePane();
		GUIGamePane.setLayout(new GridBagLayout());

		initialWindow.setVisible(true);
		initialWindow.load();
		mainFrame.add(initialWindow);
		this.myTurn = false;
	}

	/**
	 * @return A reference to the JFram displaying the GUI
	 */
	public JFrame getFrame() {
		return this.mainFrame;
	}

	/**
	 * @return A reference to client class, used to retrieve the state of the
	 *         player after an action and properly display it on the GUI
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * This method hides the main window panel and displays the game list window
	 * 
	 * @param connectionMethod
	 *            The connection method socket or rmi
	 */
	public void ConnectAction(String connectionMethod) throws RemoteException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException {
		this.initialWindow.setVisible(false);
		this.gameListWindow.load();
		this.gameListWindow.setVisible(true);
		this.mainFrame.add(this.gameListWindow);
	}

	/**
	 * Allows the user to join a new game This method is triggered by Join
	 * JButton on the Game list panel
	 * 
	 * @param gameId
	 *            The id of the game to join
	 * @param playerName
	 *            The name chooses by the user
	 * @throws InterruptedException
	 * @throws NotBoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public synchronized void JoinGame(int gameId, String playerName)
			throws InterruptedException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		this.playerName = playerName;
		client.joinGame(gameId, playerName);

		while (!client.isGameStarted() && !client.isGameEnded()) {
			synchronized (client) {
				try {
					client.wait();
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
		}

		if (client.isGameEnded()) {
			this.gameListWindow.setVisible(false);
			this.mainFrame.remove(gameListWindow);
			this.initialWindow.setVisible(true);
			this.mainFrame.add(initialWindow);
		} else {
			/*
			 * At this point the game has started, it's the time for displaying
			 * the game map panel and hiding the game list panel
			 */
			this.gameListWindow.setVisible(false);
			this.GUIGamePane.setVisible(true);
			this.GUIGamePane.load(client.getGameMap().getName());
			String welcomeMsg = "Welcome, " + playerName + " you're "
					+ client.getToken().getPlayerType().toString();
			if (client.getIsMyTurn())
				welcomeMsg += " - It's your turn!";
			else
				welcomeMsg += " - Waiting your turn!";
			this.GUIGamePane.setStateMessage(welcomeMsg);
			this.GUIGamePane.getMapPane().lightSector(
					client.getCurrentSector().getCoordinate(), "Y", playerName);
			this.mainFrame.add(GUIGamePane);
		}
	}

	/**
	 * This method is called when the client on the GUI game list panel attempts
	 * to create a new game, passing his/her playerName and the name of the map
	 * he/she has choosen for the new game
	 */
	public void JoinNewGame(String playerName, String mapName)
			throws RemoteException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		client.joinNewGame(mapName, playerName);
		this.playerName = playerName;
		GameWaitingThread waitGameThread = new GameWaitingThread(this, mapName);
		Thread thread = new Thread(waitGameThread);
		thread.start();
		client.setIsMyTurn(true);
	}

	/**
	 * This method inits the game window(GUIGamePane) displaying the map
	 * choosen, button, message log, ecc...
	 * 
	 * @param mapName
	 *            The name of the map to load
	 */
	public void initGame(String mapName) {
		if (client.isGameEnded()) {
			this.client.setGameEnded(false);
			this.gameListWindow.setVisible(false);
			this.mainFrame.remove(gameListWindow);
			gameListWindow = new GUIGameList();
			gameListWindow.setLayout(new BoxLayout(gameListWindow,
					BoxLayout.Y_AXIS));
			gameListWindow.setBackground(Color.BLACK);
			this.initialWindow.setVisible(true);
			this.mainFrame.add(initialWindow);
		} else {
			/*
			 * At this point the game has started, it's the time for displaying
			 * the game map panel and hiding the game list panel
			 */
			this.gameListWindow.setVisible(false);
			this.GUIGamePane.setVisible(true);
			this.GUIGamePane.load(mapName);
			String welcomeMsg = "Welcome, " + playerName + " you're "
					+ client.getToken().getPlayerType().toString();
			if (client.getIsMyTurn())
				welcomeMsg += " - It's your turn!";
			else
				welcomeMsg += " - Waiting your turn!";
			this.GUIGamePane.setStateMessage(welcomeMsg);
			this.GUIGamePane.getMapPane().lightSector(
					client.getCurrentSector().getCoordinate(), "Y", playerName);
			this.mainFrame.add(GUIGamePane);
		}
	}

	/**
	 * Display a dialog box displaying a message
	 * 
	 * @param message
	 *            A message to display
	 */
	public void showMessageBox(String message) {
		JOptionPane.showMessageDialog(mainFrame, message);
	}

	/**
	 * This method handles and updates the gui after receiving a notify from the
	 * server about the result of an action
	 * 
	 * @param clientNotification
	 *            the action received from the server
	 * @throws ClassNotFoundException
	 * @throws NotBoundException
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void handleAction(RRClientNotification clientNotification) throws ClassNotFoundException {
		Card firstCard = null, secondCard = null;
		if (clientNotification.getActionResult()) {
			if (client.getIsMyTurn()) {
				this.setGUIState(true);
				GUIGamePane.appendMsg(clientNotification.getMessage());
				List<Card> drawedCards = clientNotification.getDrawnCards();

				if (client.isAskLight()) {
					this.GUIGamePane
							.setStateMessage("Select a sector for the light cards!");
					this.GUIGamePane.getMapPane().changeMapMenu(
							MenuType.LIGHT_MENU);
				} else if (client.isAskAttack()) {
					this.GUIGamePane
							.setStateMessage("Select a sector fot the attack card!");
					this.GUIGamePane.getMapPane().changeMapMenu(
							MenuType.ATTACK_MENU);
				} else if (!drawedCards.isEmpty()) {
					if (drawedCards.size() > 0)
						firstCard = drawedCards.get(0);
					if (drawedCards.size() > 1)
						secondCard = drawedCards.get(1);
					CardSplashScreen splash = new CardSplashScreen(firstCard,
							secondCard, mainFrame);

					if (drawedCards.size() > 0) {
						if (drawedCards.get(0) instanceof GlobalNoiseSectorCard) {
							this.GUIGamePane
									.setStateMessage("Select a sector for the global noise card!");
							this.GUIGamePane.getMapPane().changeMapMenu(
									MenuType.NOISE_MENU);
							this.GUIGamePane.changeCardMenu(MenuType.EMPTY);
							// I've drawn a object card
							if (drawedCards.size() > 1) {
								client.getPrivateDeck().addCard(
										(ObjectCard) drawedCards.get(1));
								this.GUIGamePane
										.addCardToPanel((ObjectCard) drawedCards
												.get(1));
							}
						} 
						else if (drawedCards.size() > 1) {
							client.getPrivateDeck().addCard(
									(ObjectCard) drawedCards.get(1));
							this.GUIGamePane
									.addCardToPanel((ObjectCard) drawedCards
											.get(1));
							if (client.getPrivateDeck().getSize() > 3)
								this.manyCardHandler();
						}
					}
					if (!(firstCard instanceof GlobalNoiseSectorCard) && this.client.getPrivateDeck().getSize() < 4) {
						this.GUIGamePane
								.setStateMessage("Continue your turn...");
						this.GUIGamePane.showEndTurnButton(true);
					}
				} 
				else {
					if (client.getPrivateDeck().getSize() > 3)
						this.manyCardHandler();
					else if (this.client.getToken().getPlayerType() == PlayerType.HUMAN) {
						this.GUIGamePane
								.setStateMessage("Continue your turn...");
						this.GUIGamePane.showEndTurnButton(true);
						this.GUIGamePane.getMapPane().changeMapMenu(
								MenuType.EMPTY);
						if (!this.client.getHasMoved()) {
							this.GUIGamePane.getMapPane().changeMapMenu(
									MenuType.HUMAN_INITIAL);
							this.GUIGamePane.showEndTurnButton(false);
						}

					} else {
						this.GUIGamePane.changeCardMenu(MenuType.EMPTY);
						this.GUIGamePane.getMapPane().changeMapMenu(
								MenuType.EMPTY);
						this.GUIGamePane.showEndTurnButton(true);
						this.GUIGamePane
								.setStateMessage("Continue your turn...");
						if (!this.client.getHasMoved()) {
							this.GUIGamePane.getMapPane().changeMapMenu(
									MenuType.ALIEN_INITIAL);
							this.GUIGamePane.showEndTurnButton(false);
						}
					}
				}
			} else {
				// It's not my turn -> The player has concluded its turn
				this.setGUIState(client.getIsMyTurn());
				this.GUIGamePane.showEndTurnButton(false);
			}
		}
	}

	/**
	 * This method is used to handle the situation in which the user has drawn
	 * the fourth objetc card and need to discard or use one
	 */
	private void manyCardHandler() {

		if (client.getToken().getPlayerType() == PlayerType.HUMAN) {
			this.GUIGamePane.changeCardMenu(MenuType.HUMAN_USE_DISC_MENU);
		} else {
			this.GUIGamePane.changeCardMenu(MenuType.ALIEN_CARD_MENU);
		}
		
		this.GUIGamePane.setStateMessage("Use or discard an object card");
		this.GUIGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
		this.GUIGamePane.showEndTurnButton(false);
		this.GUIGamePane.repaint();
	}

	/**
	 * Allows the user to moveToSector on the given(target) sector
	 */
	public void move(Coordinate target) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		client.move(target.getX(), target.getY());
		this.updatePosition();
		if (client.getCurrentNotification().getActionResult())
			this.client.setHasMoved(true);
		this.handleAction(client.getCurrentNotification());
		if (!this.client.getCurrentNotification().getActionResult())
			this.GUIGamePane.setStateMessage("You cannot moveToSector in that sector!");
	}

	/**
	 * Updates the map panel displaying the new position of the player after the
	 * playet has moved
	 */
	private void updatePosition() {
		Sector newPosition = client.getCurrentSector();
		GUIGamePane.getMapPane().delightAllSectors();
		GUIGamePane.getMapPane().lightSector(newPosition.getCoordinate(), "Y",
				playerName);
	}

	/**
	 * Updates the map panel after using a light object card It displays the
	 * lighted sector and evidence the player in that sector
	 * 
	 * @param sectors
	 *            The list of sectord to light
	 */
	private void updateLightedSector(List<Sector> sectors) {
		String nameList = "";
		for (Sector s : sectors) {
			nameList = "";
			for (server_store.Player p : s.getPlayers()) {
				nameList += " " + p.name;
			}
			GUIGamePane.getMapPane().lightSector(s.getCoordinate(), "P",
					nameList);
		}
	}

	/**
	 * Display in the richTextBox and process the messages coming from
	 * PublisherSubScriber
	 */
	@Override
	public void update(Observable o, Object arg) {
		// If the turn flag has changed then i need to update the GUI
		if (this.myTurn != this.client.getIsMyTurn()) {
			myTurn = this.client.getIsMyTurn();
			this.setGUIState(this.client.getIsMyTurn());
		}
		if (arg instanceof PSClientNotification) {
			try {
				this.processPSNotification((PSClientNotification) arg);
			} catch (InterruptedException e) {
				ClientLogger.getLogger().log(Level.SEVERE, e.getMessage(), e);
			}
			String[] toDisplay = ((PSClientNotification) arg).getMessage()
					.split("\n");
			for (String m : toDisplay)
				this.GUIGamePane.appendMsg(m);
		} else {
			GUIGamePane.appendMsg((String) arg);
		}
	}

	/**
	 * Entails whether the player is dead or not, and if it has used its defence
	 * object card, if so the card is removed from its private deck
	 * 
	 * @param notification
	 *            The publisher/subscriber notification
	 * @throws InterruptedException
	 */
	private void processPSNotification(PSClientNotification notification)
			throws InterruptedException { 
		if(notification.getEscapedPlayer() != null) {
			if(notification.getEscapedPlayer().equals(this.client.getToken())) {
				this.GUIGamePane.setStateMessage("You're ESCAPED!");
				this.GUIGamePane.changeCardMenu(MenuType.EMPTY);
				this.GUIGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
				this.GUIGamePane.showEndTurnButton(false);
			}
		}
		
		if (notification.getAlienWins()) {
			if (notification.getHumanWins()) {
				GUIFinalWindow finalWindow = new GUIFinalWindow(this.mainFrame,
						"HumanAlienW.png", true);
			} else {
				GUIFinalWindow finalWindow = new GUIFinalWindow(this.mainFrame,
						"AlienW.png", true);
			}
		} else if (notification.getHumanWins()) {
			GUIFinalWindow finalWindow;
			if (notification.getAlienWins()) {
				finalWindow = new GUIFinalWindow(this.mainFrame,
						"HumanAlienW.png", true);
			} else {
				finalWindow = new GUIFinalWindow(this.mainFrame, "HumanW.png",
						true);
			}
		}
		if (notification.getDeadPlayers().contains(this.client.getToken())) {
			GUIFinalWindow deadWindow = new GUIFinalWindow(this.mainFrame,
					"dead.jpg", false);
			this.GUIGamePane.setStateMessage("You're DEAD!");
			this.GUIGamePane.changeCardMenu(MenuType.EMPTY);
			this.GUIGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
			this.GUIGamePane.showEndTurnButton(false);
		} else if (notification.getAttackedPlayers()
				.contains(client.getToken())) {
			this.showMessageBox("You've used your Defence Object card, You're still alive!");
			ObjectCard toRemove = null;
			for (ObjectCard c : this.client.getPrivateDeck().getContent()) {
				if (c instanceof DefenseObjectCard) {
					toRemove = c;
					break;
				}
			}
			this.client.getPrivateDeck().getContent().remove(toRemove);
			this.GUIGamePane.refreshCardPanel();
			this.updateCardsPanel();
			this.GUIGamePane.setStateMessage("You're safe!");
		}
	}

	/**
	 * Change the state of the GUI(buttons and menu) according to the turn of
	 * the player
	 * 
	 * @param myTurn
	 *            True if is the turn of this player, False if not
	 */
	public synchronized void setGUIState(boolean myTurn) {
		if (myTurn) {
			this.GUIGamePane.setStateMessage("It's your turn!");
			if (client.getToken().getPlayerType() == PlayerType.HUMAN) {
				this.GUIGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
				this.GUIGamePane.getMapPane().changeMapMenu(
						MenuType.HUMAN_INITIAL);
			} else {
				this.GUIGamePane.changeCardMenu(MenuType.EMPTY);
				this.GUIGamePane.getMapPane().changeMapMenu(
						MenuType.ALIEN_INITIAL);
			}
		} else {
			this.GUIGamePane.setStateMessage("Waiting your turn!");
			this.GUIGamePane.changeCardMenu(MenuType.EMPTY);
			this.GUIGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
		}
	}

	/**
	 * Produces an attack action to the given coordinates
	 */
	public void attack(Coordinate coords, boolean humanAttack)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, ClassNotFoundException, IOException,
			NotBoundException {
		this.client.attack(coords.getX(), coords.getY(), humanAttack);
		this.updatePosition();
		this.GUIGamePane.refreshCardPanel();
		this.updateCardsPanel();
		this.handleAction(this.client.getCurrentNotification());
	}

	/**
	 * Produces a noise effect to the given coordinates
	 */
	public void noise(Coordinate coords) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, ClassNotFoundException,
			IOException, NotBoundException {
		this.client.globalNoise(coords.getX(), coords.getY(), true);
		this.handleAction(this.client.getCurrentNotification());
		this.GUIGamePane.setStateMessage("You've made noise on sector "
				+ coords.getX() + "" + coords.getY());
	}

	/**
	 * Produces a light effect to the given coordinates
	 */
	public void light(Coordinate coords) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		this.client.lights(coords.getX(), coords.getY());
		this.updateLightedSector(this.client.getCurrentNotification()
				.getLightedSectors());
		this.handleAction(this.client.getCurrentNotification());
		this.GUIGamePane.refreshCardPanel();
		this.updateCardsPanel();
		this.GUIGamePane.setStateMessage("You've lighted sector "
				+ coords.getX() + "" + coords.getY());
	}

	/**
	 * Allows the client to use an object card
	 */
	public void useObjectCard(ObjectCard card) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		int index = client.getPrivateDeck().getContent().indexOf(card);
		this.client.useObjCard(index + 1);
		this.updatePosition();
		this.GUIGamePane.refreshCardPanel();
		this.updateCardsPanel();
		this.handleAction(this.client.getCurrentNotification());
		if (!this.client.getCurrentNotification().getActionResult())
			this.GUIGamePane.setStateMessage("You cannot use this card!");
		else {
			if(!(card instanceof LightsObjectCard) && !(card instanceof AttackObjectCard))
				this.GUIGamePane.setStateMessage("You've used an object card!");
		}
	}

	/**
	 * Allows the client to discard an object card
	 */
	public void discard(ObjectCard card) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		int index = client.getPrivateDeck().getContent().indexOf(card);
		this.client.discardCard(index + 1);
		this.GUIGamePane.refreshCardPanel();
		this.updateCardsPanel();
		this.handleAction(this.client.getCurrentNotification());
		this.GUIGamePane.setStateMessage("You've discarded an object card");
	}

	/**
	 * Allows the user to terminate its turn
	 */
	public void endTurn() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		this.client.endTurn();
		this.handleAction(this.client.getCurrentNotification());
	}

	/**
	 * Allows the users to send a chat message to all the users of the game
	 * 
	 * @param msg
	 */
	public void sendGlobalMessage(String msg) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		this.client.sendMessage(msg);
	}

	/**
	 * Update the card panel hiding the card used or displaing new cards
	 */
	private void updateCardsPanel() {
		for (ObjectCard card : this.client.getPrivateDeck().getContent()) {
			this.GUIGamePane.addCardToPanel(card);
		}
		this.GUIGamePane.repaint();
	}

	/**
	 * Allows the client to force the start of the game without waiting the
	 * timeout
	 * 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws NotBoundException
	 */
	public void forceGameStart() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, IOException, NotBoundException {
		this.client.forceGameStart();
	}
}

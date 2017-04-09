package client;

import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;
import it.polimi.ingsw.cg_19.RescueType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;

import common.Card;
import common.Coordinate;
import common.GamePublicData;
import common.GlobalNoiseSectorCard;
import common.ObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.RescueCard;
import common.Sector;

/**
 * Represents the manager of the client-application interaction considering a
 * CLI interface.
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class CliInteractionManager implements Observer {
	// Reference to the system's terminal
	private Scanner console;
	// Reference to the client
	private Client client;
	// The singleton instance of InteractionManager
	private static CliInteractionManager instance;

	/**
	 * Constructs a manager of the client-application interaction considering a
	 * CLI interface. This interaction manager is constructed from a client.
	 * 
	 * @param client
	 *            the client the interaction manager refers to
	 */
	private CliInteractionManager(Client client) {
		this.console = new Scanner(System.in);
		this.client = client;
		this.client.addObserver(this);
	}

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
	public static CliInteractionManager init(Client client) {
		if (instance == null)
			instance = new CliInteractionManager(client);
		return instance;
	}

	/**
	 * Shows to the client the game's map
	 * 
	 */
	public void showMap() {
		Sector comparisonSector;
		Sector clientSector = client.getCurrentSector();
		GameMap gameMap = client.getGameMap();
		int horizontalMapLength = gameMap.getHorizontalLength();
		int verticalMapLength = gameMap.getVerticalLength();
		int startingHorizontalCoord = gameMap.getStartingHorizontalCoord();
		int startingVerticalCoord = gameMap.getStartingVerticalCoord();
		String tmpYCoordinate;
		for (int i = startingVerticalCoord; i < verticalMapLength
				+ startingVerticalCoord; i++) {
			for (int j = startingHorizontalCoord; j < horizontalMapLength
					+ startingHorizontalCoord; j++) {
				comparisonSector = gameMap.getSectorByCoords(new Coordinate(
						(char) j, i));
				if (comparisonSector != null) {
					if (comparisonSector.equals(clientSector)) {
						tmpYCoordinate = Integer.toString(comparisonSector
								.getCoordinate().getY());
						if (tmpYCoordinate.length() == 1) {
							tmpYCoordinate = "0" + tmpYCoordinate;
						}
						System.out.print("|"
								+ comparisonSector.getSectorType().toString()
										.substring(0, 1) + "$"
								+ comparisonSector.getCoordinate().getX()
								+ tmpYCoordinate + " |");
					} else {
						tmpYCoordinate = Integer.toString(comparisonSector
								.getCoordinate().getY());
						if (tmpYCoordinate.length() == 1) {
							tmpYCoordinate = "0" + tmpYCoordinate;
						}
						System.out.print("|"
								+ comparisonSector.getSectorType().toString()
										.substring(0, 1) + " "
								+ comparisonSector.getCoordinate().getX()
								+ tmpYCoordinate + " |");
					}
				} else {
					System.out.print("|      |");
				}
			}
			System.out.println("\n");
		}
		this.showMenu();
	}

	/**
	 * Shows to the client the object cards in its private deck and doesn't ask
	 * the client if it wants to use one of them
	 * 
	 */
	public void showObjectCardsOnly() {
		List<ObjectCard> objectCards = client.getPrivateDeck().getContent();
		for (ObjectCard objectCard : objectCards) {
			System.out.println(objectCard.toString());
		}
	}

	/**
	 * Shows to the client the object cards in its private deck and asks to the
	 * client if it wants to use one of them
	 */
	public void showObjectCards() {
		// Getting the player's object cards
		List<ObjectCard> objectCards = client.getPrivateDeck().getContent();
		int index = 1;
		if (!objectCards.isEmpty()) {
			for (ObjectCard objectCard : objectCards) {
				System.out.println("[" + index + "] " + objectCard.toString());
				index++;
			}
			System.out.println("Do you want to use a card?(y/n)");
			String input = this.requestInput();
			try {
				if (input.equals("y")) {
					this.useObjectCard();
				} else if (input.equals("n")) {
					this.showMenu();
				} else {
					throw new IllegalArgumentException(
							"Undefinied choice, please try again");
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				this.showObjectCards();
			}
		} else {
			System.out.println("You do not own any object card");
			this.showMenu();
		}
	}

	/**
	 * Manages the client's request of using an object card
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws NotBoundException
	 * @throws NonValidActionException
	 */
	private void useObjectCard() {
		System.out
				.println("Specify the object card you want to use(If you don't want to use any card insert 'none'):");
		String input = this.requestInput();
		if (input.equals("none")) {
			this.showMap();
		} else {
			try {
				int parsedInput = Integer.parseInt(input);
				client.useObjCard(parsedInput);
				this.handleActionResult(client.getCurrentNotification());
				this.showMenu();

			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				this.useObjectCard();
			} catch ( SecurityException e) {
				ClientLogger.getLogger().log(Level.SEVERE, "com error | moveToSector ",
						e);
				System.out
						.println("A communication error with the server occured, please try again. If the problem persist contact us");
			}
		}

	}

	/**
	 * Manages the client's request to moveToSector to a specific sector
	 * 
	 */
	public void move() {
		System.out.println("Specify the sector in which you want to moveToSector:");
		String input = this.requestInput();
		try {
			char horCoord = input.charAt(0);
			int vertCoord = Integer.parseInt(input.substring(1));
			client.move(horCoord, vertCoord);
			this.handleActionResult(client.getCurrentNotification());
			this.showMenu();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.move();
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE, "com error | moveToSector ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Asks to the client information to accomplish the resolution of a lights
	 * object card
	 * 
	 */
	private void askForLights() {
		try {
			System.out
					.println("Nominate a sector, if there are players in its adiacents sectors then these players' position"
							+ " will be revealed");
			String input = this.requestInput();
			char horCoord = input.charAt(0);
			int vertCoord = Integer.parseInt(input.substring(1));
			client.lights(horCoord, vertCoord);
			this.handleActionResult(client.getCurrentNotification());
			this.showMenu();
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"com error | askForLights ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Asks to the client information to accomplish the resolution of a
	 * globalNoiseCard
	 * 
	 * @param sectorCard
	 *            the globalNoiseCard the client has just drawn
	 */
	private void askForGlobalNoise(GlobalNoiseSectorCard sectorCard) {
		try {
			System.out.println("Noise in which sector?");
			String input = this.requestInput();
			char horCoord = input.charAt(0);
			int vertCoord = Integer.parseInt(input.substring(1));
			client.globalNoise(horCoord, vertCoord,
					sectorCard.hasObjectAssociated());
			this.handleActionResult(client.getCurrentNotification());
		} catch (NumberFormatException e) {
			System.out.println("Undefined sector, please try again");
			this.askForGlobalNoise(sectorCard);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.askForGlobalNoise(sectorCard);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"com error | askForGlobalNoise ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}

	}

	/**
	 * Manages an overflow of object cards in the client's private deck(3 is the
	 * max number of object cards admitted in its private deck)
	 * 
	 * @param drawnCard
	 *            the object card the client has just drawn
	 */
	private void askForDiscardOrUseObject(ObjectCard drawnCard) {
		client.getPrivateDeck().addCard(drawnCard);
		if (client.getPrivateDeck().getContent().size() == 4) {
			System.out.println("Here's your object cards:");
			this.showObjectCardsOnly();
			try {
				if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
					System.out
							.println("You have too many object cards, please discard an object card");
					System.out.println("Specifiy the object card to discard:");
					String input = this.requestInput();
					int parsedInput = Integer.parseInt(input);
					client.discardCard(parsedInput);
					this.handleActionResult(client.getCurrentNotification());
				} else {
					System.out
							.println("You have too many object cards, either use an object card or discard an object card(D/u)");
					String input = this.requestInput();
					if (input.equals("D")) {
						System.out
								.println("Specifiy the object card to discard:");
						input = this.requestInput();
						int parsedInput = Integer.parseInt(input);
						client.discardCard(parsedInput);
						this.handleActionResult(client.getCurrentNotification());
					} else if (input.equals("u")) {
						this.useObjectCard();
					} else {
						System.out.println("Undefined choice please try again");
						client.getPrivateDeck().getContent().remove(drawnCard);
						this.askForDiscardOrUseObject(drawnCard);
					}
				}
			} catch (NumberFormatException e) {
				System.out
						.println("please enter a valid number for the object to discard");
				client.getPrivateDeck().removeCard(drawnCard);
				this.askForDiscardOrUseObject(drawnCard);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				client.getPrivateDeck().removeCard(drawnCard);
				this.askForDiscardOrUseObject(drawnCard);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException | IOException | NotBoundException e) {
				ClientLogger.getLogger().log(Level.SEVERE,
						"com error | askForDiscardOrUseObject ", e);
				System.out
						.println("A communication error with the server occured, please try again. If the problem persist contact us");
			}

		}
	}

	/**
	 * Handles the result of a client's action. The handling is performed by
	 * analyzing the notification the server has sent in response to the
	 * client's request of performing a specific action
	 * 
	 * @param clientNotification
	 *            the notification the server has sent in response to the
	 *            client's request of performing a specific action
	 */
	private void handleActionResult(RRClientNotification clientNotification) {

		boolean actionResult = clientNotification.getActionResult();
		String actionMessage = clientNotification.getMessage();
		List<Card> drawnCards = clientNotification.getDrawnCards();

		try {
			if (actionResult) {
				if (client.isAskLight()) {
					this.askForLights();
				} else if (client.isAskAttack()) {
					this.attack(true);
				} else if (!drawnCards.isEmpty()) {
					System.out.println(actionMessage);
					System.out.println("You have drawn the following cards:");
					for (Card card : drawnCards) {
						System.out.println(card.toString());
					}
					if (drawnCards.get(0) instanceof GlobalNoiseSectorCard) {
						// GlobalNoiseSectorCard effect resolution
						this.askForGlobalNoise((GlobalNoiseSectorCard) drawnCards
								.get(0));
					} else if (drawnCards.get(0) instanceof RescueCard) {
						RescueCard card = (RescueCard) drawnCards.get(0);
						if (card.getType().equals(RescueType.GREEN)) {
							client.setHasEscaped(true);
						}
					}
					if (drawnCards.size() == 2) {
						// Overflow of object cards
						if (drawnCards.get(1).getClass().getName()
								.contains("Object")) {
							this.askForDiscardOrUseObject((ObjectCard) drawnCards
									.get(1));
						}
					}
				} else {
					System.out.println(actionMessage);
				}
			} else {
				throw new NonValidActionException(
						"The action is not valid. Or its not your turn or the action can't be performed");
			}
		} catch (NonValidActionException e) {
			System.out.println(e.getMessage());
			this.showMenu();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.handleActionResult(clientNotification);
		}
	}

	/**
	 * Manages the client's end turn request
	 * 
	 */
	public void endTurn() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, ClassNotFoundException, IOException,
			NotBoundException, NonValidActionException {
		System.out.println("Do you want really want to end your turn?(y/n)");
		String input = this.requestInput();
		try {
			if (input.equals("y")) {
				client.endTurn();
				this.handleActionResult(client.getCurrentNotification());
				this.showMenu();
			} else if (input.equals("n")) {
				this.showMenu();
			} else {
				throw new IllegalArgumentException(
						"Undefinied choice, please try again");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.endTurn();
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE, "com error | endTurn ",
					e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}

	}

	/**
	 * Gets and returns the client's input
	 * 
	 * @return the client's input
	 */
	public String requestInput() {
		return console.nextLine();
	}

	/**
	 * Shows to the client the actions it could perform. Note that one or more
	 * of these actions could be not valid
	 * 
	 */
	public void showMenu() {
		String toPrint;
		if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
			toPrint = "MENU\n1 SHOW MAP\n2 SEND MESSAGE\n3 SHOW MY OBJECT CARDS\n4 MOVE\n5 ATTACK\n6 END TURN\nMake your choice:";
		} else {
			toPrint = "MENU\n1 SHOW MAP\n2 SEND MESSAGE\n3 SHOW/USE MY OBJECT CARDS\n4 MOVE\n5 END TURN\nMake your choice:";
		}
		System.out.println(toPrint);
		String input = this.requestInput();
		try {
			this.makeChoice(input);
		} catch (NonValidActionException e) {
			System.out.println(e.getMessage());
			this.showMenu();
		}
	}

	/**
	 * Maps the input inserted by the client to chose an action with a procedure
	 * that manages the execution of that action
	 * 
	 * @param choice
	 *            the input inserted by the client to chose an action
	 */
	public void makeChoice(String choice) throws NonValidActionException {
		PlayerType type = client.getToken().getPlayerType();
		boolean isClientTurn = client.getIsMyTurn();
		boolean isClientDead = client.getIsDead();
		boolean hasClientEscaped = client.getHasEscaped();
		String exceptionMsg = "";
		try {
			if (isClientDead) {
				exceptionMsg += "Undefinied choice or the action can't be made because you're dead, please try again";
			} else if (hasClientEscaped) {
				exceptionMsg += "Undefinied choice or the action can't be made because you're escaped, please try again";
			} else {
				exceptionMsg += "Undefinied choice or its not your turn, please try again";
			}
			if (choice.equals("1")) {
				this.showMap();
			} else if (choice.equals("2")) {
				this.sendMessage();
			} else if (choice.equals("3") && isClientTurn) {
				if (type.equals(PlayerType.ALIEN)) {
					this.showObjectCardsOnly();
					this.showMenu();
				} else {
					this.showObjectCards();
				}
			} else if (choice.equals("4") && isClientTurn) {
				this.move();
			} else if (choice.equals("5") && isClientTurn) {
				if (type.equals(PlayerType.ALIEN)) {
					this.attack(false);
				} else {
					this.endTurn();
					this.handleActionResult(client.getCurrentNotification());
				}

			} else if (choice.equals("6") && isClientTurn
					&& type.equals(PlayerType.ALIEN)) {
				this.endTurn();
				this.handleActionResult(client.getCurrentNotification());
			} else {
				throw new IllegalArgumentException(exceptionMsg);
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.showMenu();
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"com error | makeChoice ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Manages the client's request of sending a chat message
	 * 
	 */
	private void sendMessage() {
		try {
			System.out.println("Insert the message:");
			String input = this.requestInput();
			client.sendMessage(input);
			this.showMenu();
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"com error | sendMessage ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Manages the client's request of joining a new game
	 * 
	 */
	private void joinNewGame() {
		try {
			System.out
					.println("Please chose a game map:Galilei, Fermi, Galvani(GALILEI,FERMI,GALVANI)");
			String input = this.requestInput();
			if (!(input.equals("GALILEI") || input.equals("FERMI") || input
					.equals("GALVANI"))) {
				throw new IllegalArgumentException(
						"Undifined map, please try again");
			}
			System.out.println("Insert a name to be used in the game:");
			String input1 = this.requestInput();
			client.joinNewGame(input, input1);
			System.out.println("You will be a "+client.getToken().getPlayerType().toString()+" in the game");
			System.out.println("Waiting for the game to start");
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"com error | joinNewGame ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Manages the client's request of joining an existing game
	 * 
	 * @param gamesAmount
	 *            the number of games available
	 */
	private void joinGame(int gamesAmount) {
		try {
			System.out
					.println("Specify the number of the game you want to join");
			String input = this.requestInput();
			int parsedInput = Integer.parseInt(input);
			if (parsedInput <= gamesAmount && parsedInput > 0) {
				System.out.println("Insert a name to be used in the game:");
				String input1 = this.requestInput();
				client.joinGame(parsedInput, input1);
				System.out.println("You will be a "+client.getToken().getPlayerType().toString()+" in the game");
				System.out.println("Waiting for the game to start");
			} else {
				throw new IllegalArgumentException(
						"Undefined game, please try again");
			}
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE, "com error | joinGame ",
					e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Shows to the client the list of available games and asks if it wants to
	 * join a new or an existing game
	 * 
	 */
	public void showGames() {
		try {
			List<GamePublicData> availableGames = client.getGames();
			int gamesAmount = availableGames.size();
			if (gamesAmount > 0) {
				for (GamePublicData gamePublicData : availableGames) {
					System.out.println(gamePublicData.toString());
				}
				System.out
						.println("Would you like to create a new game or join an existing one?(n/j)");
				String input = this.requestInput();
				if (input.equals("n")) {
					joinNewGame();
				} else if (input.equals("j")) {
					joinGame(gamesAmount);
				} else {
					throw new IllegalArgumentException(
							"Undefined choice, please try again");
				}
			} else {
				System.out.println("No games yet");
				System.out.println("Would you like to create a new game?(y/n)");
				String input = this.requestInput();
				if (input.equals("y")) {
					joinNewGame();
				} else if (input.equals("n")) {
					this.showGames();
				} else {
					throw new IllegalArgumentException(
							"Undefined choice, please try again");
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.showGames();
		}catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}
	}

	/**
	 * Welcomes the client and asks what type of connection he wants to use
	 */
	public void connChoice() {
		System.out
				.println("Hi, Welcome to Escape from the Aliens in Outer Space!");
		System.out
				.println("To start select a type of client/server communication: Socket or Rmi?(s/r)");
		String input = this.requestInput();
		try {
			if (input.equals("s")) {
				client.buildDataRemoteExchangeFactory("SOCKET");
			} else if (input.equals("r")) {
				client.buildDataRemoteExchangeFactory("RMI");
			} else {
				throw new IllegalArgumentException(
						"Undifined choice, please try again");
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.connChoice();
		}
	}

	/**
	 * Manages the client's request of attacking a sector
	 * 
	 */
	public void attack(boolean fromHuman) {
		System.out.println("Specify the sector you want to attack:");
		String input = this.requestInput();
		try {
			char horCoord = input.charAt(0);
			int vertCoord = Integer.parseInt(input.substring(1));
			client.attack(horCoord, vertCoord, fromHuman);
			this.handleActionResult(client.getCurrentNotification());
			this.showMenu();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			this.attack(fromHuman);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | SecurityException
				| ClassNotFoundException | IOException | NotBoundException e) {
			ClientLogger.getLogger().log(Level.SEVERE,
					"com error | showGames ", e);
			System.out
					.println("A communication error with the server occured, please try again. If the problem persist contact us");
		}

	}

	/**
	 * Represent an exception that signals that the action performed is not
	 * valid.
	 * 
	 * @author Andrea Sessa
	 * @author Giorgio Pea
	 */
	private class NonValidActionException extends Exception {
		private static final long serialVersionUID = 1L;

		public NonValidActionException(String message) {
			super(message);
		}
	}

	/**
	 * Updates the interaction manager in response to the arrival of a
	 * PSClientNotification. This arrival is registered in the client, and the
	 * client notifies the interaction manager in the logic of the
	 * observable/observer pattern
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof PSClientNotification) {
			PSClientNotification psNotification = (PSClientNotification) arg;
			
			if(psNotification.getEscapedPlayer() != null) {
				if(psNotification.getEscapedPlayer().equals(this.client.getToken())) {
					System.err.println("You're ESCAPED!");
				}
			}
			// //Check the client has been attacked
			if (psNotification.getAttackedPlayers().contains(client.getToken())) {
				client.removeDefenseCard();
			}
			// Check if the client has died
			if (psNotification.getDeadPlayers().contains(client.getToken())) {
				System.err
						.println("YOU HAVE DIED, YOU CANNOT MAKE ANYMORE ACTIONS");
				client.setIsDead(true);
			}
			// Check end of the game
			if (psNotification.getAlienWins() || psNotification.getHumanWins()) {
				client.shutdown();
			}
		}
	}
}

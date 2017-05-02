package it.polimi.ingsw.cg_19;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.ActionMapper;
import effects.EndTurnEffect;
import factories.*;
import server.ClientMethodsNamesProvider;
import server.GameManager;
import server.GameStatus;
import server.PubSubHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;

/**
 * Represents a generic game.
 *
 */
public class Game{
    private final static long TURN_TIMEOUT = 5 * 60 * 1000;
    private static int counter = 0;

    private volatile List<Player> players;
    private final List<PubSubHandler> pubSubHandlers;
    private final ClientMethodsNamesProvider clientMethodsNamesProvider;


    private volatile ObjectDeck objectDeck;
    private volatile RescueDeck rescueDeck;
    private volatile SectorDeck sectorDeck;
    private volatile GameMap gameMap;
    private volatile Player currentPlayer;
    private volatile int turnNumber;

    private volatile List<Class<? extends Action>> nextActions;
    private volatile Action lastAction;

    private TurnTimeout timeout;
    private Timer timer;

    private volatile GameMapFactory gameMapFactory;
    private volatile GamePublicData gamePublicData;

    private volatile ActionMapper actionMapper;

    private final GameManager gameManager;

    /**
     * Constructs a game from the name of its associated map. The resources of
     * the game are not initialized until the game is started. From the name of
     * the game's associated map the right map factory is automatically created
     * along with an empty list of players, an empty list of threads
     * representing the game's subscribers in the logic of the pub/sub pattern,
     * an empty container of the game's public data and an empty map between a
     * player's unique identifier and a player
     *
     * @param gameMapName name of the game's associated map
     */
    public Game(String gameMapName) throws NoSuchElementException{
        this.pubSubHandlers = new ArrayList<>();
        this.clientMethodsNamesProvider = ClientMethodsNamesProvider.getInstance();
        this.gameManager = GameManager.getInstance();
        switch (gameMapName) {
            case "GALILEI":
                this.gameMapFactory = new GalileiGameMapFactory();
                break;
            case "FERMI":
                this.gameMapFactory = new FermiGameMapFactory();
                break;
            case "GALVANI":
                this.gameMapFactory = new GalvaniGameMapFactory();
                break;
            default:
                throw new NoSuchElementException("No game map matches the given name");
        }
        this.players = new ArrayList<>();
        counter++;
        this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
        this.turnNumber = 0;
    }

    /**
     * Constructs a game from its associated map. The resources of the game are
     * not initialized until the game is started. an empty list of players, an
     * empty list of threads representing the game's subscribers in the logic of
     * the pub/sub pattern, an empty container of the game's public data and an
     * empty map between a player's unique identifier and a player are
     * automatically created. This constructor is only used for test purposes
     *
     * @param gameMap the game's associated map
     */
    public Game(GameMap gameMap) {
        this.clientMethodsNamesProvider = ClientMethodsNamesProvider.getInstance();
        this.gameManager = GameManager.getInstance();
        this.pubSubHandlers = new ArrayList<>();
        this.gameMap = gameMap;
        this.players = new ArrayList<>();
        counter++;
        this.gamePublicData = new GamePublicData(counter, "Game_" + counter);
        this.turnNumber = 0;
        DeckFactory deckFactory = new ObjectDeckFactory();
        this.objectDeck = (ObjectDeck) deckFactory.makeDeck();
        deckFactory = new SectorDeckFactory();
        this.sectorDeck = (SectorDeck) deckFactory.makeDeck();
        deckFactory = new RescueDeckFactory();
        this.rescueDeck = (RescueDeck) deckFactory.makeDeck();
    }

    /**
     * Starts the game by initializing its resources and sending a remote method
     * call to all the game's subscribers to signal them that the game is going
     * to start
     */
    public void startGame() {
        // Resources init
        DeckFactory deckFactory = new ObjectDeckFactory();
        this.objectDeck = (ObjectDeck) deckFactory.makeDeck();
        deckFactory = new SectorDeckFactory();
        this.sectorDeck = (SectorDeck) deckFactory.makeDeck();
        deckFactory = new RescueDeckFactory();
        this.rescueDeck = (RescueDeck) deckFactory.makeDeck();
        this.gameMap = gameMapFactory.makeMap();
        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(gameMap.getName());
        // Setting players' starting sector
        for (Player player : players) {
            if (player.getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)) {
                player.setCurrentSector(gameMap.getHumanSector());
                gameMap.getHumanSector().addPlayer(player);
            } else {
                player.setCurrentSector(gameMap.getAlienSector());
                gameMap.getAlienSector().addPlayer(player);
            }
        }
        // Init of the first game turn
        if (currentPlayer.getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)) {
            this.nextActions = HumanTurn.getInitialActions();
        } else {
            this.nextActions = AlienTurn.getInitialActions();
        }
        this.gamePublicData.setStatus(GameStatus.CLOSED);

        this.timer = new Timer();
        this.timeout = new TurnTimeout(this);
        this.timer.schedule(timeout, TURN_TIMEOUT);
        // Notification to the subscribers
        this.notifySubscribers(new RemoteMethodCall(this.clientMethodsNamesProvider.sendMapAndStartGame(), parameters));
        for (PubSubHandler handler : this.pubSubHandlers){
            if (handler.getPlayerToken().equals(this.currentPlayer.getPlayerToken())){
                handler.queueNotification(new RemoteMethodCall("startTurn",new ArrayList<>()));
                break;
            }
        }
    }

    /**
     * Adds a player to the game
     *
     * @param playerName the thread that will handle the pub/sub communication with the
     *                   client
     * @return the unique identifier(token) of the player inserted
     */
    public synchronized PlayerToken addPlayer(String playerName) {
        PlayerType playerType = assignTypeToPlayer(players.size() + 1);
        PlayerToken playerToken = new PlayerToken(playerType, this.gamePublicData.getId());
        Player player = new Player(playerName);
        player.setPlayerToken(playerToken);
        players.add(player);
        gamePublicData.addPlayer();
        if (currentPlayer == null) {
            this.currentPlayer = player;
        }
        return playerToken;
    }

    /**
     * Produces a player type based on the number of players already in game .
     * If the number of players already in game is even, the returned player
     * type is "HUMAN", otherwise is "ALIEN". This procedure is adopted in order
     * to guarantee a balanced number of aliens and humans
     *
     * @param numberOfPlayers the number of players already in game
     * @return a player type, either "HUMAN" or "ALIEN"
     */
    public PlayerType assignTypeToPlayer(int numberOfPlayers) {
        if (numberOfPlayers % 2 == 0) {
            return PlayerType.HUMAN;
        } else {
            return PlayerType.ALIEN;
        }

    }

    /**
     * Gets the public data of the game
     *
     * @return the public data of the game
     */
    public synchronized GamePublicData getPublicData() {
        return gamePublicData;
    }

    /**
     * Sets the next current player in the game
     */
    public void shiftCurrentplayer() {
        int currentPlayerIndex = players.indexOf(currentPlayer);
        do {
            currentPlayerIndex++;
            if (currentPlayerIndex == players.size())
                currentPlayerIndex = 0;
        } while (players.get(currentPlayerIndex).getPlayerState() == PlayerState.DEAD);

        this.currentPlayer = players.get(currentPlayerIndex);
    }

    /**
     * Gets the game's current player
     *
     * @return the game's current player
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Gets the game's associated deck of object cards
     *
     * @return the game's associated deck of object cards
     */
    public ObjectDeck getObjectDeck() {
        return objectDeck;
    }

    /**
     * Gets the game's associated deck of rescue cards
     *
     * @return the game's associated deck of rescue cards
     */
    public RescueDeck getRescueDeck() {
        return rescueDeck;
    }

    /**
     * Gets the game's associated deck of sector cards
     *
     * @return the game's associated deck of sector cards
     */
    public SectorDeck getSectorDeck() {
        return sectorDeck;
    }

    /**
     * Gets the game's associated map
     *
     * @return the game's associated map
     */
    public GameMap getMap() {
        return this.gameMap;
    }

    /**
     * Gets the game's turn number
     *
     * @return the game's turn number
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Sets the game's turn number. This method is used only for test purposes
     *
     * @param turnNumber the new game's turn number
     */
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    /**
     * Gets the game's last action performed
     *
     * @return the game's last performed action
     */
    public Action getLastAction() {
        return lastAction;
    }

    /**
     * Sets the game's last action performed
     *
     * @param lastAction the new game's last action performed
     */
    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    /**
     * Performs a game action
     *
     * @param action      the action to be performed
     * @param playerToken the token of the player that wants to perform the action
     * @return an array of two notifications to be sent to the game's
     * subscribers. The first one is to be sent only to the player that
     * wants to perform the action, the second one is to be sent to all
     * the game's subscribers
     * @see ClientNotification
     * @see Action
     */

    public synchronized RRClientNotification makeAction(Action action,
                                                        PlayerToken playerToken) {
        RRClientNotification clientNotification = new RRClientNotification();
        PSClientNotification psNotification = new PSClientNotification();
        Player actualPlayer = this.getPlayer(playerToken);
        boolean actionResult = false;
        if (!currentPlayer.equals(actualPlayer)) {
            clientNotification.setActionResult(false);
        } else {
            // If the player is ok then checks if the action is ok
            if (nextActions.contains(action.getClass())) {
                try {
                    Method executeMethod = ActionMapper.getInstance().getEffect(action.getClass())
                            .getMethod("executeEffect", Game.class, RRClientNotification.class,
                                    PSClientNotification.class, Action.class);
                    actionResult = (boolean) executeMethod.invoke(null,this, clientNotification,psNotification, action);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (actionResult) {
                    clientNotification.setActionResult(true);
                    /*
                     * If the last action has been and an end turn action the
					 * there is no need to update the nextAction field
					 */
                    if (!lastAction.getClass().equals(EndTurnAction.class)) {
                        if (actualPlayer.getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
                            nextActions = AlienTurn.nextAction(lastAction, actualPlayer);
                        } else {
                            nextActions = HumanTurn.nextAction(lastAction, actualPlayer);
                        }

                    } else {
                        if (this.currentPlayer.getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
                            nextActions = AlienTurn.getInitialActions();
                        } else {
                            nextActions = HumanTurn.getInitialActions();
                        }
                        turnNumber++;

                        // Reset the timeout

                        timer.cancel();
                        timer = new Timer();
                        timeout = new TurnTimeout(this);
                        timer.schedule(timeout, TURN_TIMEOUT);
                        for (PubSubHandler pubSubHandler : this.pubSubHandlers){
                            if (pubSubHandler.getPlayerToken().equals(currentPlayer.getPlayerToken())){
                                pubSubHandler.queueNotification(new RemoteMethodCall(this.clientMethodsNamesProvider.startTurn(), new ArrayList<>()));
                            }
                        }
                    }
                    boolean winH = checkWinConditions(PlayerType.HUMAN);
                    boolean winA = checkWinConditions(PlayerType.ALIEN);

                    if (winH) {
                        psNotification
                                .setMessage(psNotification.getMessage()
                                        + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                    }
                    if (winA) {
                        psNotification
                                .setMessage(psNotification.getMessage()
                                        + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");

                    }
                    if (winH || winA) {
                        psNotification.setAlienWins(winA);
                        psNotification.setHumanWins(winH);
                        this.gameManager.endGame(this);
                    }
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(psNotification);
                    this.notifySubscribers(new RemoteMethodCall(this.clientMethodsNamesProvider.asyncNotification(), parameters));
                }
                else {
                    clientNotification.setActionResult(false);
                    clientNotification.setMessage("The action cannot be performed");
                }
            } else {
                clientNotification.setMessage("The action cannot be performed");
                clientNotification.setActionResult(false);
            }

        }

        return clientNotification;
    }

    /**
     * Makes the current player end his turn in response to a notification
     * from {@link TurnTimeout}
     * @throws InstantiationException Reflection problem
     * @throws IllegalAccessException Reflection problem
     */
    public void timeoutUpdate() throws InstantiationException,
            IllegalAccessException {
        Player previousPlayer = this.currentPlayer;
        PSClientNotification psClientNotification = new PSClientNotification();
        RRClientNotification rrClientNotification = new RRClientNotification();
        EndTurnEffect.executeEffect(this, rrClientNotification, psClientNotification,null);
        this.timer.schedule(new TurnTimeout(this), TURN_TIMEOUT);
        for (PubSubHandler handler : this.pubSubHandlers) {
            ArrayList<Object> parameters = new ArrayList<>();
            if (handler.getPlayerToken().equals(this.currentPlayer.getPlayerToken())) {
                handler.queueNotification(new RemoteMethodCall(this.clientMethodsNamesProvider.startTurn(), new ArrayList<>()));
            }
            if (handler.getPlayerToken().equals(previousPlayer.getPlayerToken())) {
                handler.queueNotification(new RemoteMethodCall(this.clientMethodsNamesProvider.forceEndTurn(), new ArrayList<>()));
            }
            parameters.add(psClientNotification);
            handler.queueNotification(new RemoteMethodCall(this.clientMethodsNamesProvider.asyncNotification(), parameters));
        }
    }

    /**
     * Notifies the game's subscribers with a {@link RemoteMethodCall} that has to be
     * performed on them.
     *
     * @param remoteMethodCall The {@link RemoteMethodCall} to be performed on the game's
     *                         subscribers
     */
    public synchronized void notifySubscribers(RemoteMethodCall remoteMethodCall) {
        for (PubSubHandler pubSubHandler : this.pubSubHandlers) {
            pubSubHandler.queueNotification(remoteMethodCall);
        }
    }

    public int getId() {
        return this.gamePublicData.getId();
    }


    /**
     * Checks if the game has finished.
     *
     * @return True if someone(Alien, Human or both) has won the game and the
     * game has ended, false otherwise.
     */
    public boolean checkWinConditions(PlayerType playerType) {
        boolean allDeadHumans = this.checkStateAll(PlayerType.HUMAN, PlayerState.DEAD);
        boolean allEscapedHumans = this.checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED);
        boolean allDeadAliens = this.checkStateAll(PlayerType.ALIEN, PlayerState.DEAD);
        boolean existEscapes = this.gameMap.existEscapes();
        if (playerType == PlayerType.HUMAN) {
            // If all human players are escaped then Human wins!
            if (allEscapedHumans) {
                return true;
            } else if (!allDeadHumans && allDeadAliens) {
                return true;
            }
            return false;
        } else {
            // If all human player are all dead, alien wins!
            if (allDeadHumans) {
                return true;
            } else if (this.turnNumber == 39 && !allEscapedHumans) {
                return true;
            } else if (!allEscapedHumans && !existEscapes) {
                return true;
            }
            return false;
        }
    }

    /**
     * Checks if all the players of a given {@link PlayerType} are in the given {@link PlayerState}.
     *
     * @param playerType  The player's type to be considered for the checking.
     * @param playerState The state of a player to be consider for the checking.
     * @return True if all the players of a given player type are in the given {@link PlayerState}
     */
    private boolean checkStateAll(PlayerType playerType, PlayerState playerState) {
        for (Player player : players) {
            if (!player.getPlayerState().equals(playerState) && player.getPlayerToken().getPlayerType().equals(playerType)) {
                return false;
            }
        }
        return true;
    }


    public List<PubSubHandler> getPubSubHandlers() {
        return pubSubHandlers;
    }

    public void addPubSubHandler(PubSubHandler pubSubHandler) {
        this.pubSubHandlers.add(pubSubHandler);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(PlayerToken playerToken) throws NoSuchElementException {
        for (Player player : this.players) {
            if (player.getPlayerToken().equals(playerToken)) {
                return player;
            }
        }
        throw new NoSuchElementException("No player matches the given token");
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}

package store_reducers;

import common.EndTurnAction;
import common.PSClientNotification;
import common.PlayerToken;
import common.RRClientNotification;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.EndTurnEffect;
import factories.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import server.GameStatus;
import server_store.*;
import store_actions.*;

import java.util.List;
import java.util.Timer;

/**
 * Created by giorgiopea on 14/03/17.
 * <p>
 * Handles the logic related to the slice of the app's state
 * represented by a single game
 */
public class GameReducer extends Reducer {
    /**
     * @see server_store.Reducer
     */
    @Override
    public ServerState reduce(StoreAction action, ServerState state) {

        String actionType = action.getType();
        switch (actionType) {
            case "@GAME_ADD_PLAYER":
                this.addPlayer(action, state);
                break;
            case "@GAME_MAKE_ACTION":
                this.makeAction(action, state);
                break;
            case "@GAME_START_GAME":
                this.startGame(action, state);
                break;
            case "@GAME_TURNTIMEOUT_EXPIRED":
                this.turnTimeoutExpired(action, state);
        }
        return state;

    }

    /**
     * Changes a game in the list of games in the app's state so that the turn of the current player ends because
     * he/she hasn't made an action within the turn's timeout
     *
     * @param action The action that has triggered this task, see {@link store_actions.GameTurnTimeoutExpiredAction}
     * @param state  The app's current state
     * @return The app's new state
     */
    private ServerState turnTimeoutExpired(StoreAction action, ServerState state) {
        GameTurnTimeoutExpiredAction castedAction = (GameTurnTimeoutExpiredAction) action;
        for (Game game : state.getGames()) {
            if (game.gamePublicData.getId() == castedAction.getPayload()) {
                EndTurnEffect.executeEffect(game, new EndTurnAction());
                game.currentTimer.cancel();
                game.currentTimer = new Timer();
            }
        }
        return state;

    }

    /**
     * Changes a game in the list of games in the app's state so that the game
     * starts. This includes the initialization of the game's associated decks and map, along with
     * a turn timer
     *
     * @param action The action that has triggered this task, see {@link store_actions.GameStartGameAction}
     * @param state  The current app's state
     * @return The app's new state
     */
    private ServerState startGame(StoreAction action, ServerState state) {
        GameStartGameAction castedAction = (GameStartGameAction) action;
        Integer gameId = castedAction.getPayload();
        for (Game game : state.getGames()) {
            if (game.gamePublicData.getId() == gameId) {
                DeckFactory deckFactory = new ObjectDeckFactory();
                game.objectDeck = (ObjectDeck) deckFactory.makeDeck();
                deckFactory = new SectorDeckFactory();
                game.sectorDeck = (SectorDeck) deckFactory.makeDeck();
                deckFactory = new RescueDeckFactory();
                game.rescueDeck = (RescueDeck) deckFactory.makeDeck();

                game.gameMap = GameMapFactory.provideCorrectFactory(game.mapName).makeMap();

                for (Player player : game.players) {
                    if (player.playerType.equals(PlayerType.HUMAN)) {
                        player.currentSector = game.gameMap.getHumanSector();
                        game.gameMap.getHumanSector().addPlayer(player);
                    } else {
                        player.currentSector = game.gameMap.getAlienSector();
                        game.gameMap.getAlienSector().addPlayer(player);
                    }
                }
                if (game.currentPlayer.playerType.equals(PlayerType.HUMAN)) {
                    game.nextActions = HumanTurn.getInitialActions();
                } else {
                    game.nextActions = AlienTurn.getInitialActions();
                }
                game.gamePublicData.setStatus(GameStatus.CLOSED);
                game.currentTimer = new Timer();
                return state;
            }
        }
        return state;
    }

    /**
     * Changes a game in the list of games in the app's state in response to a player willing to make an in-game action.
     * This method checks if the player that wants to make an in-game action is authorized to do so, if the action
     * is allowed to be performed and if the action, once executed, causes the game to end. All the necessary textual
     * notification to be sent to players are produced too
     *
     * @param action The action that has triggered this task, see {@link store_actions.GameMakeActionAction}
     * @param state  The app's current state
     * @return The app's new state
     */
    private ServerState makeAction(StoreAction action, ServerState state) {
        GameMakeActionAction castedAction = (GameMakeActionAction) action;
        StoreAction gameAction = castedAction.payload.action;
        boolean winH;
        boolean winA;
        for (Game game : state.getGames()) {
            if (game.gamePublicData.getId() == castedAction.payload.playerToken.getGameId()) {
                game.lastPSclientNotification = new PSClientNotification();
                game.lastRRclientNotification = new RRClientNotification();
                Player actualPlayer = null;
                for (Player player : game.players) {
                    if (player.playerToken.getUUID().equals(castedAction.payload.playerToken.getUUID())) {
                        actualPlayer = player;
                        break;
                    }
                }
                if (!game.currentPlayer.equals(actualPlayer)) {
                    game.lastRRclientNotification.setActionResult(false);
                } else {
                    // If the player is ok then check if the action is ok
                    if (game.nextActions.contains(gameAction.getType())) {

                        // Executes the action's associated logic and get the result
                        ServerStore.getInstance().dispatchAction(new GameActionAction(gameAction, game));
                        if (game.lastActionResult) {
                            if (!game.lastAction.getClass().equals(EndTurnAction.class)) {
                                if (actualPlayer.playerType.equals(PlayerType.HUMAN)) {
                                    game.nextActions = HumanTurn.nextAction(game.lastAction, actualPlayer);
                                } else {
                                    game.nextActions = AlienTurn.nextAction(game.lastAction, actualPlayer);
                                }

                            } else {
                                if (actualPlayer.playerType.equals(PlayerType.HUMAN)) {
                                    game.nextActions = HumanTurn.getInitialActions();
                                } else {
                                    game.nextActions = AlienTurn.getInitialActions();
                                }
                                game.turnNumber++;
                            }
                            winH = checkWinConditions(PlayerType.HUMAN, game);
                            winA = checkWinConditions(PlayerType.ALIEN, game);

                            if (winH) {
                                game.lastPSclientNotification
                                        .setMessage(game.lastPSclientNotification.getMessage()
                                                + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                                game.lastPSclientNotification.setHumanWins(true);
                            }
                            if (winA) {
                                game.lastPSclientNotification
                                        .setMessage(game.lastPSclientNotification.getMessage()
                                                + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");
                                game.lastPSclientNotification.setAlienWins(true);

                            }
                            if (winH || winA) {
                                ServerStore.getInstance().dispatchAction(new GamesEndGameAction(game.gamePublicData.getId()));
                            } else {
                                game.currentTimer.cancel();
                                game.currentTimer = new Timer();
                            }
                            game.lastRRclientNotification.setActionResult(true);
                        }
                    } else {
                        game.lastRRclientNotification.setActionResult(false);
                    }

                }
                break;
            }
        }
        return state;
    }

    /**
     * Changes a game in the list of games in the app's state so that a new player is added to this game
     *
     * @param action The action that has triggered this task, see {@link store_actions.GameAddPlayerAction}
     * @param state  The app's current state
     * @return The app's new state
     */
    private ServerState addPlayer(StoreAction action, ServerState state) {
        GameAddPlayerAction castedAction = (GameAddPlayerAction) action;
        PlayerToken playerToken;
        for (Game game : state.getGames()) {
            if (game.gamePublicData.getId() == castedAction.getPayload().getGameId()) {
                PlayerType playerType = assignTypeToPlayer(game.players.size() + 1);
                playerToken = new PlayerToken(playerType, castedAction.getPayload().getGameId());
                server_store.Player player = new server_store.Player(playerType, castedAction.getPayload().getPlayerName(), playerToken);
                game.players.add(player);
                game.gamePublicData.addPlayer();
                if (game.currentPlayer == null) {
                    game.currentPlayer = player;
                }
                break;
            }
        }
        return state;
    }


    /**
     * Produces a player type based on the number of players already in game.
     * If the number of players already in game is even, the returned player
     * type is "HUMAN", otherwise is "ALIEN". This procedure is adopted in order
     * to guarantee a balanced number of aliens and humans
     *
     * @param numberOfPlayers The number of players already in game
     * @return A player type, either "HUMAN" or "ALIEN"
     */
    public PlayerType assignTypeToPlayer(int numberOfPlayers) {
        if (numberOfPlayers % 2 == 0) {
            return PlayerType.HUMAN;
        } else {
            return PlayerType.ALIEN;
        }

    }

    /**
     * Decides if the Humans or Aliens have won the game.
     * Aliens win if:
     * <ul>
     * <li>All human players are dead</li>
     * <li>Some human player is still alive but the turn number is 39</li>
     * <li>Some human player is still alive but no escape point is available</li>
     * </ul>
     * <br/>
     * Humans win if:
     * <ul>
     * <li>They have all escaped</li>
     * <li>No alien is left, at least one human is still alive and at least one escape point exists/li>
     * <li>At least one human has escaped but no more alive players exist</li>
     * </ul>
     *
     * @param playerType The type of the faction we want to check if has won or not
     * @param game       The game we are considering
     * @return True if the faction has won, false otherwise
     */
    private boolean checkWinConditions(PlayerType playerType, Game game) {
        boolean allDeadHumans = this.checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED, game.players);
        boolean allEscapedHumans = this.checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED, game.players);
        boolean allDeadAliens = this.checkStateAll(PlayerType.ALIEN, PlayerState.DEAD, game.players);
        boolean existEscapes = game.gameMap.existEscapes();
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
            } else if (game.turnNumber == 39 && !allEscapedHumans) {
                return true;
            } else if (!allEscapedHumans && !existEscapes) {
                return true;
            }
            return false;
        }
    }

    /**
     * Check if all the players of a given faction obey to a given status
     *
     * @param playerType The faction we are interested in
     * @param state      The status we are interested in
     * @param players    All the players of all factions
     * @return True if the players of the given faction obey to the given status, false otherwise
     */
    private boolean checkStateAll(PlayerType playerType, PlayerState state, List<Player> players) {
        for (Player player : players) {
            if (!player.playerState.equals(state)
                    && player.playerType.equals(playerType))
                return false;
        }
        return true;
    }
}

package store_reducers;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.EndTurnEffect;
import factories.*;
import common.GameStatus;
import server.AlienTurn;
import server.Game;
import server.HumanTurn;
import server.PubSubHandler;
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
public class GameReducer implements Reducer {
    /**
     * @see server_store.Reducer
     */
    @Override
    public ServerState reduce(StoreAction action, State state) {
        ServerState castedState = (ServerState) state;
        switch (action.type) {
            case "@GAME_ADD_PLAYER":
                return this.addPlayer(action, castedState);
            case "@GAME_MAKE_ACTION":
                return  this.makeAction(action, castedState);
            case "@GAME_START_GAME":
                return this.startGame(action, castedState);
            case "@GAME_TURNTIMEOUT_EXPIRED":
                return this.turnTimeoutExpired(action, castedState);
            case  "@GAME_PUT_CHAT_MSG":
                return this.putChatMsg(action,castedState);
        }
        return castedState;

    }

    private ServerState putChatMsg(StoreAction action, ServerState castedState) {
        GamePutChatMsg castedAction = (GamePutChatMsg) action;
        for (Game game : castedState.getGames()){
            if (game.getGamePublicData().getId() == castedAction.getPlayerToken().getGameId()){
                game.setLastRRclientNotification(new RRClientNotification(true,null,null,null));
                break;
            }
        }
        return castedState;
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
            if (game.getGamePublicData().getId() == castedAction.getPayload()) {
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
            if (game.getGamePublicData().getId() == gameId) {
                DeckFactory deckFactory = new ObjectDeckFactory();
                game.objectDeck = (ObjectDeck) deckFactory.makeDeck();
                deckFactory = new SectorDeckFactory();
                game.sectorDeck = (SectorDeck) deckFactory.makeDeck();
                deckFactory = new RescueDeckFactory();
                game.rescueDeck = (RescueDeck) deckFactory.makeDeck();

                game.gameMap = GameMapFactory.provideCorrectFactory(game.mapName).makeMap();

                for (Player player : game.players) {
                    if (player.playerToken.playerType.equals(PlayerType.HUMAN)) {
                        player.currentSector = game.gameMap.getHumanSector();
                        game.gameMap.getHumanSector().addPlayer(player);
                    } else {
                        player.currentSector = game.gameMap.getAlienSector();
                        game.gameMap.getAlienSector().addPlayer(player);
                    }
                }
                if (game.currentPlayer.playerToken.playerType.equals(PlayerType.HUMAN)) {
                    game.nextActions = HumanTurn.getInitialActions();
                } else {
                    game.nextActions = AlienTurn.getInitialActions();
                }
                game.getGamePublicData().setStatus(GameStatus.CLOSED);
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
        StoreAction gameAction = castedAction.getAction();
        for (Game game : state.getGames()) {
            if (game.getGamePublicData().getId() == castedAction.getPlayerToken().getGameId()) {
                game.setLastPSclientNotification(new PSClientNotification());
                game.setLastRRclientNotification(new RRClientNotification());
                Player actualPlayer = null;
                for (Player player : game.getPlayers()) {
                    if (player.getPlayerToken().equals(castedAction.getPlayerToken())) {
                        actualPlayer = player;
                        break;
                    }
                }
                if (!game.getCurrentPlayer().equals(actualPlayer)) {
                    game.getLastRRclientNotification().setActionResult(false);
                } else {
                    // If the player is ok then check if the action is ok
                    if (game.getNextActions().contains(gameAction.type)) {

                        // Executes the action's associated logic and get the result
                        ServerStore.getInstance().dispatchAction(new GameActionAction(gameAction, game));
                        if (game.isLastActionResult()) {
                            if (!game.getLastAction().type.equals("@GAMEACTION_END_TURN")) {
                                if (game.getCurrentPlayer().getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)) {
                                    game.setNextActions(HumanTurn.nextAction(game.getLastAction(), game.getCurrentPlayer()));
                                } else {
                                    game.setNextActions(AlienTurn.nextAction(game.getLastAction(), actualPlayer));
                                }

                            } else {
                                if (game.getCurrentPlayer().getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
                                    game.setNextActions(AlienTurn.getInitialActions());
                                } else {
                                    game.setNextActions(HumanTurn.getInitialActions());
                                }
                                game.setTurnNumber(game.getTurnNumber()+1);
                            }
                            game.setDidHumansWin(checkWinConditions(PlayerType.HUMAN, game));
                            game.setDidAlienWin(checkWinConditions(PlayerType.ALIEN, game));
                            game.getCurrentTimer().cancel();
                            game.setCurrentTimer(new Timer());
                            game.getLastRRclientNotification().setActionResult(true);
                            if (game.isDidHumansWin()) {
                                game.getLastPSclientNotification()
                                        .setMessage(game.getLastPSclientNotification().getMessage()
                                                + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                            }
                            if (game.isDidAlienWin()) {
                                game.getLastPSclientNotification()
                                        .setMessage(game.getLastPSclientNotification().getMessage()
                                                + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");

                            }
                            if (game.isDidAlienWin() || game.isDidHumansWin()) {
                                game.getLastPSclientNotification().setAlienWins(game.isDidAlienWin());
                                game.getLastPSclientNotification().setHumanWins(game.isDidHumansWin());
                            }
                        }
                    } else {
                        game.getLastRRclientNotification().setActionResult(false);
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
            if (game.getGamePublicData().getId() == castedAction.getPayload().getGameId()) {
                PlayerType playerType = assignTypeToPlayer(game.players.size() + 1);
                playerToken = new PlayerToken(playerType, castedAction.getPayload().getGameId());
                Player player = new Player(castedAction.getPayload().getPlayerName(),playerToken);
                game.players.add(player);
                game.getGamePublicData().addPlayer();
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
    private PlayerType assignTypeToPlayer(int numberOfPlayers) {
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
        boolean allDeadHumans = this.checkStateAll(PlayerType.HUMAN, PlayerState.DEAD, game.players);
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
                    && player.playerToken.playerType.equals(playerType))
                return false;
        }
        return true;
    }
}

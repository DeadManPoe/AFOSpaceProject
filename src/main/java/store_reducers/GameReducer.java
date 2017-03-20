package store_reducers;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.EndTurnEffect;
import effects.GameActionMapper;
import factories.*;
import it.polimi.ingsw.cg_19.*;
import server.GameStatus;
import server_store.*;
import server_store.AlienTurn;
import server_store.Game;
import server_store.HumanTurn;
import server_store.Player;
import server_store.Turn;
import store_actions.*;
import server_store.Reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameReducer extends Reducer {
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

    private ServerState turnTimeoutExpired(StoreAction action, ServerState state) {
        GameTurnTimeoutExpiredAction castedAction = (GameTurnTimeoutExpiredAction) action;
        for (Game game : state.getGames()){
            if (game.gamePublicData.getId() == castedAction.getPayload()){
                EndTurnEffect endTurnEffect = new EndTurnEffect();
                PSClientNotification psClientNotification = new PSClientNotification();
                RRClientNotification rrClientNotification = new RRClientNotification();
                endTurnEffect.executeEffect(game,rrClientNotification, psClientNotification);
                game.lastPSclientNotification = psClientNotification;
            }
        }
        return state;

    }

    private ServerState startGame(StoreAction action, ServerState state) {

        GameStartGameAction castedAction = (GameStartGameAction) action;
        Integer gameId = castedAction.getPayload();
        for (Game game : state.getGames()){
            if(game.gamePublicData.getId() == gameId ){
                DeckFactory deckFactory = new ObjectDeckFactory();
                game.objectDeck = (ObjectDeck) deckFactory.makeDeck();
                deckFactory = new SectorDeckFactory();
                game.sectorDeck = (SectorDeck) deckFactory.makeDeck();
                deckFactory = new RescueDeckFactory();
                game.rescueDeck = (RescueDeck) deckFactory.makeDeck();

                GameMapFactory gameMapFactory = null;

                if (game.mapName.equals("GALILEI")) {
                    gameMapFactory = new GalileiGameMapFactory();
                } else if (game.mapName.equals("FERMI")) {
                    gameMapFactory = new FermiGameMapFactory();
                } else if (game.mapName.equals("GALVANI")) {
                    gameMapFactory = new GalvaniGameMapFactory();
                } else {
                    throw new NoSuchElementException("No map matches with the given name");
                }
                game.gameMap = gameMapFactory.makeMap();
                for (Player player : game.players) {
                    if (player.playerType.equals(PlayerType.HUMAN)) {
                        player.currentSector = game.gameMap.getHumanSector();
                        game.gameMap.getHumanSector().addPlayer(player);
                    } else {
                        player.currentSector = game.gameMap.getAlienSector();
                        game.gameMap.getAlienSector().addPlayer(player);
                    }
                }
                Turn turn;
                // Init of the first game turn
                if (game.currentPlayer.playerType.equals(PlayerType.HUMAN)) {
                    game.nextActions = HumanTurn.getInitialActions();
                } else {
                    game.nextActions = AlienTurn.getInitialActions();
                }
                game.gamePublicData.setStatus(GameStatus.CLOSED);
                return state;
            }
        }
        return null;
    }

    private ServerState makeAction(StoreAction action, ServerState state) {
        GameMakeActionAction castedAction = (GameMakeActionAction) action;
        Action gameAction = castedAction.getPayload().getAction();
        UUID handlerUUID = castedAction.getPayload().getReqRespHandlerUUID();
        boolean gameActionResult = false;
        boolean winH = false;
        boolean winA = false;
        RRClientNotification rrClientNotification = new RRClientNotification();
        PSClientNotification psClientNotification = new PSClientNotification();
        for (Game game : state.getGames()) {
            if (game.gamePublicData.getId() == castedAction.getPayload().getGameId()) {
                //1. get and check actual player
                //2. get check actions
                //3. check win conditions
                //4. notify

                Player actualPlayer = null;
                for (Player player : game.players) {
                    if (player.playerToken.getUUID().equals(castedAction.getPayload().getPlayerToken().getUUID())) {
                        actualPlayer = player;
                        break;
                    }
                }
                if (!game.currentPlayer.equals(actualPlayer)) {
                    rrClientNotification.setActionResult(false);
                } else {
                    // If the player is ok then checks if the action is ok
                    if (game.nextActions.contains(gameAction.getClass())) {

                        // Executes the effect and get the result
                        try {
                            gameActionResult = GameActionMapper.getInstance().getEffect(gameAction).executeEffect(game, rrClientNotification, psClientNotification);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        if (gameActionResult) {
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
                                psClientNotification
                                        .setMessage(psClientNotification.getMessage()
                                                + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                                psClientNotification.setHumanWins(true);
                            }
                            if (winA) {
                                psClientNotification
                                        .setMessage(psClientNotification.getMessage()
                                                + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");
                                psClientNotification.setAlienWins(true);

                            }
                            if (winH || winA) {
                                ServerStore.getInstance().dispatchAction(new GameEndGameAction(game.gamePublicData.getId()));
                            }
                            rrClientNotification.setActionResult(true);
                        }
                    } else {
                        rrClientNotification.setActionResult(false);
                    }

                }
                game.lastRRclientNotification = rrClientNotification;
                game.lastPSclientNotification = psClientNotification;
                break;
            }
        }
        return state;
    }

    private ServerState addPlayer(StoreAction action, ServerState state) {
        GameAddPlayerAction castedAction = (GameAddPlayerAction) action;
        PlayerToken playerToken = null;
        for (Game game : state.getGames()) {
            if (game.gamePublicData.getId() == castedAction.getPayload().getGameId()) {
                PlayerType playerType = assignTypeToPlayer(game.players.size() + 1);
                playerToken = new PlayerToken(playerType);
                server_store.Player player = new server_store.Player(playerType, castedAction.getPayload().getPlayerName(), playerToken);
                game.players.add(player);
                game.gamePublicData.addPlayer();
                if(game.currentPlayer == null){
                    game.currentPlayer = player;
                }
                break;
            }
        }
        return state;
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

    private boolean checkWinConditions(PlayerType playerType, Game game) {
        if (playerType == PlayerType.HUMAN) {
            // If all human players are escaped then Human wins!
            if (this.checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED, game.players))
                return true;
                // Only one human player left with an escape possibility
            else if (this.getNumOfAlivePlayer(PlayerType.ALIEN, game.players) == 0
                    && this.getNumOfAlivePlayer(PlayerType.HUMAN, game.players) == 1
                    && game.gameMap.existEscapes())
                return true;
                // All human player are dead or escaped
            else if (!this.checkStateAll(PlayerType.HUMAN, PlayerState.DEAD, game.players)
                    && this.getNumOfAlivePlayer(PlayerType.HUMAN, game.players) == 0)
                return true;
        } else {
            // If all human player are all dead, alien wins!
            if (this.getNumOfAlivePlayer(PlayerType.HUMAN, game.players) == 0
                    && !checkStateAll(PlayerType.HUMAN, PlayerState.ESCAPED, game.players))
                return true;

            if (game.turnNumber == 39) {
                // Some human player is still alive, but turn = 39, so alien
                // wins
                if (this.getNumOfAlivePlayer(PlayerType.HUMAN, game.players) > 0)
                    return true;
                return false;
            } else {
                if (!game.gameMap.existEscapes()
                        && this.getNumOfAlivePlayer(PlayerType.HUMAN, game.players) > 0)
                    return true;
            }
        }
        return false;
    }

    private boolean checkStateAll(PlayerType playerType, PlayerState state, List<Player> players) {
        for (Player player : players) {
            if (!player.playerState.equals(state)
                    && player.playerType.equals(playerType))
                return false;
        }
        return true;
    }

    private int getNumOfAlivePlayer(PlayerType type, List<Player> players) {
        int count = 0;
        for (Player player : players) {
            if (player.playerState.equals(PlayerState.ALIVE)
                    && player.playerType.equals(type))
                count++;
        }
        return count;
    }
}

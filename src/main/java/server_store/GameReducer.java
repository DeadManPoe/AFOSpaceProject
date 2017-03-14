package server_store;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.ActionMapper;
import effects.DiscardObjCardEffect;
import factories.*;
import it.polimi.ingsw.cg_19.*;
import server.GameStatus;
import store_actions.GameAddPlayerAction;
import store_actions.GameMakeActionAction;
import sts.Action;
import sts.Reducer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameReducer extends Reducer {
    public GameReducer(List<String> writableStateKeys) {
        super(writableStateKeys);
    }

    @Override
    public ServerState reduce(Action action, ServerState state) {
        if (action.type.equals("@GAME_DISCARD_OBJ_CARD")) {

            new DiscardObjCardEffect();
        } else if (action.type.equals("@GAME_DRAW_OBJ_CARD")) {
            //Put here effect
        } else if (action.type.equals("@GAME_DRAW_RESCUE_CARD")) {
            //Put here effect
        } else if (action.type.equals("@GAME_DRAW_SECTOR_CARD")) {

        } else if (action.type.equals("@GAME_MOVE")) {

        } else if (action.type.equals("@GAME_MOVE_ATTACK")) {

        } else if (action.type.equals("@GAME_USE_OBJ_CARD")) {

        } else if (action.type.equals("@GAME_USE_SECTOR_CARD")) {

        } else if (action.type.equals("@GAME_ADD_PLAYER")) {
            this.addPlayer(action, state);
        } else if (action.type.equals("@GAME_START_GAME")) {
            this.startGame(action);
        }
    }

    private ServerState addPlayer(GameAddPlayerAction action, ServerState state) throws IOException {
        GamePlayerCommunicationSocket gamePlayerCommunicationSocket = action.payload;
        Game game = ServerStore.serverStore.getState().GAMES_BY_ID.get(gamePlayerCommunicationSocket.gameId);
        Player player = new Player(this.assignTypeToPlayer(game.players.size()),
                gamePlayerCommunicationSocket.playerName);
        PlayerToken playerToken = new PlayerToken(player.playerType);
        game.players.put(playerToken, player);
        game.pubSubHandlers.add(
                gamePlayerCommunicationSocket.communicationHandler.runPubSubThread(
                        gamePlayerCommunicationSocket.socket, playerToken));
        return state;
    }

    private void startGame(Action action) {
        Game game = (Game) action.payload;
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
            //
        }
        game.gameMap = gameMapFactory.makeMap();
        //ArrayList<Object> parameters = new ArrayList<Object>();
        //parameters.add(gameMap.getName());
        //parameters.add(this.fromPlayerToToken(currentPlayer));
        // Setting players' starting sector
        for (Map.Entry<PlayerToken, Player> entry : game.players.entrySet()) {
            Player player = entry.getValue();
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
            turn = new HumanTurn();
        } else {
            turn = new AlienTurn();
        }
        game.nextActions = turn.initialActions;
        game.gamePublicData.setStatus(GameStatus.CLOSED);

        //timer = new Timer();
        //timeout = new TurnTimeout(this, timer);
        //timer.schedule(timeout, TURN_TIMEOUT);
        // Notification to the subscribers
        //this.notifyListeners(new RemoteMethodCall("sendMap", parameters));
    }


    private ServerState makeAction(GameMakeActionAction gameMakeActionAction, ServerState state) throws IllegalAccessException, InstantiationException {

        //Check if the player that has made the action is allowed to do that
        //Dispatch the effect
        //Update the next possibile actions
        //Check if the game is finished
        //Dispatch the effect
        ActionMapper actionMapper = new ActionMapper();
        GameActionPlayerHandler gameActionPlayer = gameMakeActionAction.payload;
        Game game = ServerStore.serverStore.getState().GAMES_BY_ID.get(gameActionPlayer.gameId);
        game.lastClientResponse = new RRClientNotification();
        game.lastClientNotification = new PSClientNotification();
        Player actualPlayer = game.players.get(gameActionPlayer.playerToken);
        if (!game.currentPlayer.equals(actualPlayer)) {
            game.lastClientResponse.setActionResult(false);
        } else {

            // If the player is ok then checks if the action is ok
            if (game.nextActions.contains(gameActionPlayer.action.getClass())) {

                ServerStore.serverStore.dispatchAction(actionMapper.getEffect(gameActionPlayer.action));

                // Retrieve the related effect
                //ActionEffect effect = actionMapper.getEffect(gameActionPlayer.action);

                // Executes the effect and get the result
                //boolean actionResult = effect.executeEffect(game,
                //        clientNotification, psNotification);

					/*
                     * If the last action has been and an end turn action the
					 * there is no need to update the nextAction field
					 */
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

                    // Reset the timeout

                    //timer.purge();
                    //timer.cancel();
                    //timeout.cancel();
                    //timer = new Timer();
                    //timeout = new TurnTimeout(this, timer);
                    //timer.schedule(timeout, TURN_TIMEOUT);
                }
                boolean winH = checkWinConditions(PlayerType.HUMAN, game);
                boolean winA = checkWinConditions(PlayerType.ALIEN, game);

                if (winH) {
                    game.lastClientNotification
                            .setMessage(game.lastClientNotification.getMessage()
                                    + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                    game.lastClientNotification.setHumanWins(true);
                }
                if (winA) {
                    game.lastClientNotification
                            .setMessage(game.lastClientNotification.getMessage()
                                    + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");
                    game.lastClientNotification.setAlienWins(true);

                }
                game.lastClientResponse.setActionResult(true);
                //if (winH || winA) {
                //    //psNotification.setAlienWins(winA);
                //    psNotification.setHumanWins(winH);
                //    clientNotification.setActionResult(true);
                //    ClientNotification[] toReturn = { clientNotification,
                //            psNotification };
                //    this.gameManager.removeGame(this);
                //    return toReturn;
                //}
                //Dispatch remove game
                //clientNotification.setActionResult(true);
                //}
            } else {
                game.lastClientResponse.setActionResult(false);
            }

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

    private boolean checkStateAll(PlayerType playerType, PlayerState state, Map<PlayerToken, Player> players) {
        for (Map.Entry<PlayerToken, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            if (!player.playerState.equals(state)
                    && player.playerType.equals(playerType))
                return false;
        }
        return true;
    }

    private int getNumOfAlivePlayer(PlayerType type, Map<PlayerToken, Player> players) {
        int count = 0;
        for (Map.Entry<PlayerToken, Player> entry : players.entrySet()) {
            Player player = entry.getValue();
            if (player.playerState.equals(PlayerState.ALIVE)
                    && player.playerType.equals(type))
                count++;
        }
        return count;
    }

    private PlayerType assignTypeToPlayer(int numberOfPlayers) {
        if (numberOfPlayers % 2 == 0) {
            return PlayerType.HUMAN;
        } else {
            return PlayerType.ALIEN;
        }
    }

}

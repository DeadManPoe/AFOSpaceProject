package server_store;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.ActionEffect;
import factories.*;
import it.polimi.ingsw.cg_19.*;
import server.GameStatus;
import store_actions.GameAddPlayerAction;
import store_actions.GameMakeActionAction;
import sts.Action;
import sts.Reducer;

import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class GameReducer extends Reducer {
    public GameReducer(List<String> writableStateKeys) {
        super(writableStateKeys);
    }

    @Override
    public ServerState reduce(Action action, ServerState state) {
        if(action.type.equals("@GAME_DISCARD_OBJ_CARD")){

        }
        else if(action.type.equals("@GAME_DRAW_OBJ_CARD")){

        }
        else if(action.type.equals("@GAME_DRAW_RESCUE_CARD")){

        }
        else if(action.type.equals("@GAME_DRAW_SECTOR_CARD")){

        }
        else if(action.type.equals("@GAME_MOVE")){

        }
        else if(action.type.equals("@GAME_MOVE_ATTACK")){

        }
        else if(action.type.equals("@GAME_USE_OBJ_CARD")){

        }
        else if(action.type.equals("@GAME_USE_SECTOR_CARD")){

        }
        else if(action.type.equals("@GAME_ADD_PLAYER")){
            this.addPlayer(action, state);
        }
        else if(action.type.equals("@GAME_START_GAME")){
            this.startGame(action);
        }
    }

    private ServerState addPlayer(GameAddPlayerAction action, ServerState state) {
        GameIdPlayerName gameIdPlayerName = action.payload;
        Game game = ServerStore.serverStore.getState().GAMES_BY_ID.get(gameIdPlayerName.gameId);
        Player player = new Player(this.assignTypeToPlayer(game.players.size()),gameIdPlayerName.playerName);
        PlayerToken playerToken = new PlayerToken(player.playerType);
        game.players.put(playerToken,player);
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

        if(game.mapName.equals("GALILEI")){
            gameMapFactory = new GalileiGameMapFactory();
        }
        else if(game.mapName.equals("FERMI")){
            gameMapFactory = new FermiGameMapFactory();
        }
        else if(game.mapName.equals("GALVANI")){
            gameMapFactory = new GalvaniGameMapFactory();
        }
        else {
            //
        }
        game.gameMap = gameMapFactory.makeMap();
        //ArrayList<Object> parameters = new ArrayList<Object>();
        //parameters.add(gameMap.getName());
        //parameters.add(this.fromPlayerToToken(currentPlayer));
        // Setting players' starting sector
        for (Map.Entry<PlayerToken, Player> entry: game.players.entrySet()) {
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


    private ServerState makeAction(GameMakeActionAction gameMakeActionAction, ServerState state){
        GameActionPlayer gameActionPlayer = gameMakeActionAction.payload;
        Game game = ServerStore.serverStore.getState().GAMES_BY_ID.get(gameActionPlayer.gameId);
        //Prendiamoci il player e verifichiamo se è lui il player corrente
        Player actualPlayer = game.players.get(gameActionPlayer.playerToken);
        // if(turn.getInitialAction().contains(action.class)) &&
        if (!game.currentPlayer.equals(actualPlayer)) {
            //clientNotification.setActionResult(false);
        } else {

            // If the player is ok then checks if the action is ok
            if (game.nextActions.contains(gameActionPlayer.action.getClass())) {
                // Retrieve the related effect
                ActionEffect effect = actionMapper.getEffect(action);

                // Executes the effect and get the result
                boolean actionResult = effect.executeEffect(game,
                        clientNotification, psNotification);

                if (actionResult) {
					/*
					 * If the last action has been and an end turn action the
					 * there is no need to update the nextAction field
					 */
                    if (!game.lastAction.getClass().equals(EndTurnAction.class)) {
                        game.nextActions = turn.getNextActions(lastAction);
                    } else {

                        nextActions = turn.getInitialActions();
                        turnNumber++;

                        // Reset the timeout

                        timer.purge();
                        timer.cancel();
                        timeout.cancel();
                        timer = new Timer();
                        timeout = new TurnTimeout(this, timer);
                        timer.schedule(timeout, TURN_TIMEOUT);
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
                        clientNotification.setActionResult(true);
                        ClientNotification[] toReturn = { clientNotification,
                                psNotification };
                        this.gameManager.removeGame(this);
                        return toReturn;
                    }
                    clientNotification.setActionResult(true);
                }
            } else {
                clientNotification.setActionResult(false);
            }

        }
        ClientNotification[] toReturn = { clientNotification, psNotification };
        return toReturn;
    }
    private PlayerType assignTypeToPlayer(int numberOfPlayers) {
        if (numberOfPlayers % 2 == 0) {
            return PlayerType.HUMAN;
        } else {
            return PlayerType.ALIEN;
        }
    }

}

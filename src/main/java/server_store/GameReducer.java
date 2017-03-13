package server_store;

import common.*;
import decks.ObjectDeck;
import decks.RescueDeck;
import decks.SectorDeck;
import effects.ActionEffect;
import effects.ActionMapper;
import factories.*;
import it.polimi.ingsw.cg_19.*;
import server.GameStatus;
import sts.Action;
import sts.Reducer;

import java.util.ArrayList;
import java.util.List;
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
        else if(action.type.equals("@GAME_START_GAME")){
            this.startGame(action);
        }
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
        for (Player player : game.players) {
            if (player.getPlayerType().equals(PlayerType.HUMAN)) {
                player.setSector(gameMap.getHumanSector());
                gameMap.getHumanSector().addPlayer(player);
            } else {
                player.setSector(gameMap.getAlienSector());
                gameMap.getAlienSector().addPlayer(player);
            }
        }
        // Init of the first game turn
        if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
            turn = new HumanTurn(this);
        } else {
            turn = new AlienTurn(this);
        }
        nextActions = turn.getInitialActions();
        this.gamePublicData.setStatus(GameStatus.CLOSED);

        timer = new Timer();
        timeout = new TurnTimeout(this, timer);
        timer.schedule(timeout, TURN_TIMEOUT);
        // Notification to the subscribers
        this.notifyListeners(new RemoteMethodCall("sendMap", parameters));
    }


    private ServerState makeAction(GameMakeAction gameMakeAction, ServerState state){
        //Prendiamoci il player e verifichiamo se è lui il player corrente
        Player actualPlayer = playerTokenToPlayerMap.get(playerToken);
        // if(turn.getInitialAction().contains(action.class)) &&
        if (!currentPlayer.equals(actualPlayer)) {
            clientNotification.setActionResult(false);
        } else {
            // If the player is ok then checks if the action is ok
            if (nextActions.contains(action.getClass()) || forced) {
                // Retrieve the related effect
                ActionEffect effect = actionMapper.getEffect(action);

                // Executes the effect and get the result
                boolean actionResult = effect.executeEffect(this,
                        clientNotification, psNotification);

                if (actionResult) {
					/*
					 * If the last action has been and an end turn action the
					 * there is no need to update the nextAction field
					 */
                    if (!lastAction.getClass().equals(EndTurnAction.class)) {
                        nextActions = turn.getNextActions(lastAction);
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
}

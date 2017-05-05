package store_effects;

import common.RemoteMethodCall;
import server.ClientMethodsNamesProvider;
import server.Game;
import server.PubSubHandler;
import server.TurnTimeout;
import server_store.*;
import store_actions.GameStartGameAction;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * Handles the side-effects related to the start of a game
 */
public class GameStartGameEffect implements Effect {


    @Override
    public void apply(StoreAction action, State state) {
        /*
            This method notifies the clients with the token of the player who will start
            to act as first, and with the name of the game's map.
         */
        ServerState castedState = (ServerState) state;
        GameStartGameAction castedAction = (GameStartGameAction) action;
        Game game = castedAction.getGame();
        game.getCurrentTimer().schedule(new TurnTimeout(game),castedState.getTurnTimeout());
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.getGameMap().getName());
        for (PubSubHandler handler : this.getHandlersByGameId(game.getGamePublicData().getId(),castedState.getPubSubHandlers())){
            handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().sendMapAndStartGame(),parameters));
            if (handler.getPlayerToken().equals(game.getCurrentPlayer().getPlayerToken())){
                handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().startTurn(), new ArrayList<>()));

            }
        }

    }
    private List<PubSubHandler> getHandlersByGameId(int gameId, List<PubSubHandler> handlers){
        List<PubSubHandler> handlersToReturn = new ArrayList<>();
        for (PubSubHandler handler : handlers){
            if (handler.getPlayerToken().getGameId() == gameId){
                handlersToReturn.add(handler);
            }
        }
        return handlersToReturn;
    }
}

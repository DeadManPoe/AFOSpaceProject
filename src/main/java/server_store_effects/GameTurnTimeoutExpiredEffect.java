package server_store_effects;


import common.RemoteMethodCall;
import server.ClientMethodsNamesProvider;
import server.Game;
import server.PubSubHandler;
import server_store.*;
import server_store_actions.GameTurnTimeoutExpiredAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the side-effects related to the expiration of the game turn timeout.
 */
public class GameTurnTimeoutExpiredEffect implements Effect {

    @Override
    public void apply(StoreAction action, State state) {
        /*
            This method notifies to the clients that the current's turn timeout has expired and
            so a turn shift will happen; a new timeout for the new turn is set as well.
         */
        ServerState castedState = (ServerState) state;
        GameTurnTimeoutExpiredAction castedAction = (GameTurnTimeoutExpiredAction) action;
        Game game = castedAction.getGame();
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(game.getLastPSclientNotification());
        //Notification sending
        for (PubSubHandler handler : this.getHandlersByGameId(game.getGamePublicData().getId(), castedState.getPubSubHandlers())) {

            if (handler.getPlayerToken().equals(game.getCurrentPlayer().getPlayerToken())) {
                handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().startTurn(), new ArrayList<>()));
            }
            if (handler.getPlayerToken().equals(game.getPreviousPlayer().getPlayerToken())) {
                handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().forceEndTurn(), new ArrayList<>()));
            }
            handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().asyncNotification(), parameters));

        }

    }

    private List<PubSubHandler> getHandlersByGameId(int gameId, List<PubSubHandler> handlers) {
        List<PubSubHandler> handlersToReturn = new ArrayList<>();
        for (PubSubHandler handler : handlers) {
            if (handler.getPlayerToken().getGameId() == gameId) {
                handlersToReturn.add(handler);
            }
        }
        return handlersToReturn;
    }
}

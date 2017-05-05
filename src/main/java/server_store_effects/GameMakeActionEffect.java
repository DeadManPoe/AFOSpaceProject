package server_store_effects;

import common.RemoteMethodCall;
import server.*;
import server_store.*;
import server_store_actions.GameMakeActionAction;
import server_store_actions.GamesEndGameAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 20/03/17.
 * <p>
 * Handles the side-effects related to an in-game action
 */
public class GameMakeActionEffect implements Effect {

    @Override
    public void apply(StoreAction action, State state) {
        /*
            This method will check if some faction has won the game, will notify the clients with the
            messages produces by the in-game action, will schedule a new turn's timeout and will end the turn
            if a faction has won
         */
        ServerState castedState = (ServerState) state;
        GameMakeActionAction castedAction = (GameMakeActionAction) action;
        Game game = castedAction.getGame();
        for (PubSubHandler handler : this.getHandlersByGameId(game.getGamePublicData().getId(), castedState.getPubSubHandlers())) {
            ArrayList<Object> parameters = new ArrayList<>();
            parameters.add(game.getLastPSclientNotification());
            handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().asyncNotification(), parameters));

            if (castedAction.getAction().type.equals("@GAMEACTION_END_TURN") && game.getCurrentPlayer().getPlayerToken().equals(handler.getPlayerToken())) {
                handler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().startTurn(), new ArrayList<>()));
            }

        }
        if (!game.isDidHumansWin() && !game.isDidAlienWin()) {
            game.getCurrentTimer().schedule(new TurnTimeout(game), castedState.getTurnTimeout());
        } else {
            ServerStore.getInstance().dispatchAction(new GamesEndGameAction(game));
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

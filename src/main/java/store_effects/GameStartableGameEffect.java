package store_effects;

import common.PlayerToken;
import common.RemoteMethodCall;
import server.ClientMethodsNamesProvider;
import server.PubSubHandler;
import server_store.Effect;
import server_store.ServerState;
import server_store.State;
import server_store.StoreAction;
import store_actions.GameStartableGameAction;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by giorgiopea on 05/05/17.
 */
public class GameStartableGameEffect implements Effect {
    @Override
    public void apply(StoreAction action, State state) {
        ServerState castedState = (ServerState) state;
        GameStartableGameAction castedAction = (GameStartableGameAction) action;
        if (castedAction.isStartable()){
            PubSubHandler pubSubHandler = this.getPubSubHandlerByPlayerToken(castedAction.getGame().getCurrentPlayer().getPlayerToken(), castedState.getPubSubHandlers());
            pubSubHandler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().signalStartableGame(), new ArrayList<>()));
        }
    }

    private PubSubHandler getPubSubHandlerByPlayerToken(PlayerToken playerToken, List<PubSubHandler> handlers) throws NoSuchElementException{
        for (PubSubHandler handler : handlers){
            if (handler.getPlayerToken().equals(playerToken)){
                return handler;
            }
        }
        throw new NoSuchElementException("No pubsubhandler matches the given player token");
    }
}

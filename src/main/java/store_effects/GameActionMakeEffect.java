package store_effects;

import common.EndTurnAction;
import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameActionAction;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 22/03/17.
 */
public class GameActionMakeEffect extends Effect {
    @Override
    public void apply(StoreAction action, ServerState state) {
        GameActionAction castedAction = (GameActionAction) action;
        for (PubSubHandler handler : state.getPubSubHandlers()){
            if (handler.getPlayerToken().equals(castedAction.game.currentPlayer.playerToken)){
                handler.queueNotification(new RemoteMethodCall("allowTurn",new ArrayList<Object>()));
                break;
            }
        }
    }
}

package sts_effects;

import common.RemoteMethodCall;
import server_store.Game;
import server_store.GameActionPlayerHandler;
import server_store.PubSubHandler;
import server_store.ServerStore;
import store_actions.GameAddPlayerAction;
import store_actions.GameMakeActionAction;
import sts.Action;
import sts.ActionFactory;
import sts.Effect;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class ClientResponseEffect extends Effect {

    public ClientResponseEffect(ActionFactory actionFactory) {
        super(actionFactory);
    }

    @Override
    public void apply(Action action) {
        GameAddPlayerAction gameAddPlayerAction = (GameAddPlayerAction) action;
        Game game = ServerStore.serverStore.getState().GAMES_BY_ID.get(gameAddPlayerAction.payload.gameId);
        for(PubSubHandler handler : game.pubSubHandlers){
            ArrayList<Object> parameters = new ArrayList<Object>();
            parameters.add(game.lastNotificationToClient);
            handler.queueNotification(new RemoteMethodCall("pubSubNotification",parameters));
        }
    }
}

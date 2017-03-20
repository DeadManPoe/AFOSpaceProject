package store_effects;


import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameTurnTimeoutExpiredAction;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameTurnTimeoutExpiredEffect extends Effect {

    @Override
    public void apply(StoreAction action, ServerState state) {
        GameTurnTimeoutExpiredAction castedAction = (GameTurnTimeoutExpiredAction) action;
        ArrayList<Object> parameters = new ArrayList<>();
        for (Game game : state.getGames()){
            if (game.gamePublicData.getId() == castedAction.getPayload()){
                parameters.add(game.lastPSclientNotification);
                game.currentTimer.schedule(new TurnTimeout(castedAction.getPayload()),state.getTurnTimeout());
            }
        }
        for (PubSubHandler handler : state.getPubSubHandlers()){
            if (handler.getGameId().equals(castedAction.getPayload())){
                handler.queueNotification(new RemoteMethodCall("sendPubNotification",parameters));
            }
        }
    }
}

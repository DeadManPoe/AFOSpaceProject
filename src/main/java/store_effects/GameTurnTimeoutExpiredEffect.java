package store_effects;


import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameTurnTimeoutExpiredAction;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by giorgiopea on 20/03/17.
 *
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
        ArrayList<Object> parameters = new ArrayList<>();
        for (Game game : castedState.getGames()){
            if (game.gamePublicData.getId() == castedAction.getPayload()){
                parameters.add(game.lastPSclientNotification);
                //The new timeout is set
                game.currentTimer.schedule(new TurnTimeout(castedAction.getPayload()),castedState.getTurnTimeout());
            }
        }
        //Notification sending
        for (PubSubHandler handler : castedState.getPubSubHandlers()){
            if (handler.getPlayerToken().getGameId().equals(castedAction.getPayload())){
                handler.queueNotification(new RemoteMethodCall("sendPubNotification",parameters));
            }
        }
    }
}

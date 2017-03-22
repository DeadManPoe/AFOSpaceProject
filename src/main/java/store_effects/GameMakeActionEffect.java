package store_effects;

import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameMakeActionAction;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameMakeActionEffect extends Effect {

    @Override
    public void apply(StoreAction action, ServerState state) {
        GameMakeActionAction castedAction = (GameMakeActionAction) action;
        Game game = null;
        for (Game c_game : state.getGames()){
            if (c_game.gamePublicData.getId() == castedAction.payload.playerToken.getGameId()){
                game = c_game;
                break;
            }
        }
        if (game != null){
            game.currentTimer.schedule(new TurnTimeout(castedAction.payload.playerToken.getGameId()), state.getTurnTimeout());
            for (ReqRespHandler handler : state.getReqRespHandlers()) {
                if (handler.getUuid().equals(castedAction.payload.reqRespHandlerUUID)) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(game.lastRRclientNotification);
                    handler.addRemoteMethodCallToQueue(new RemoteMethodCall("sendNotification", parameters));
                    break;
                }
            }
            for (PubSubHandler handler : state.getPubSubHandlers()) {
                if (handler.getGameId() == game.gamePublicData.getId()) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(game.lastPSclientNotification);
                    handler.queueNotification(new RemoteMethodCall("sendPubNotification", parameters));

                }
            }
        }


    }
}

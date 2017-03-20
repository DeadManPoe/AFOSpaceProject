package store_effects;

import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameAddPlayerAction;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameAddPlayerEffect extends Effect {

    @Override
    public void apply(StoreAction action, ServerState state) {
        GameAddPlayerAction castedAction = (GameAddPlayerAction) action;
        Game game = null;
        for (Game c_game : state.getGames()){
            if (c_game.gamePublicData.getId() == castedAction.getPayload().getGameId()){
                game = c_game;
                break;
            }
        }
        if (game != null){
            Player player = game.players.get(game.players.size() - 1);
            for (ReqRespHandler handler : state.getReqRespHandlers()) {
                if (handler.getUuid().equals(castedAction.getPayload().getUuid())) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(player.playerToken);
                    handler.addRemoteMethodCallToQueue(new RemoteMethodCall("sendToken", parameters));
                    break;
                }
            }
        }
        else {
            throw  new NoSuchElementException("No game matches with the given id");
        }


    }
}

package store_effects;

import common.Player;
import common.RemoteMethodCall;
import server.Game;
import server.ReqRespHandler;
import server_store.*;
import store_actions.GameAddPlayerAction;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by giorgiopea on 20/03/17.
 *
 * Handles the side-effects related to the addition of a player into a game
 */
public class GameAddPlayerEffect implements Effect {

    @Override
    public void apply(StoreAction action, State state) {
        /*
            This method sends to the player, who wants to join a game, its identification token
         */
        ServerState castedState = (ServerState) state;
        GameAddPlayerAction castedAction = (GameAddPlayerAction) action;
        Game game = null;
        for (Game c_game : castedState.getGames()){
            if (c_game.gamePublicData.getId() == castedAction.getPayload().getGameId()){
                game = c_game;
                break;
            }
        }
        if (game != null){
            Player player = game.players.get(game.players.size() - 1);
            for (ReqRespHandler handler : castedState.getReqRespHandlers()) {
                if (handler.getUuid().equals(castedAction.getPayload().getUuid())) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(player.playerToken);
                    handler.addRemoteMethodCallToQueue(new RemoteMethodCall("setPlayerTokenAndSubscribe", parameters));
                    break;
                }
            }
        }
        else {
            throw  new NoSuchElementException("No game matches with the given id");
        }


    }
}

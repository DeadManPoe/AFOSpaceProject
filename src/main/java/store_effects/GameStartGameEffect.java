package store_effects;

import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameStartGameAction;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.TimerTask;

/**
 * Created by giorgiopea on 20/03/17.
 */
public class GameStartGameEffect extends Effect {


    @Override
    public void apply(StoreAction action, ServerState state) {
        GameStartGameAction castedAction = (GameStartGameAction) action;
        Integer gameId = castedAction.getPayload();
        Game game = null;
        ArrayList<Object> parameters = new ArrayList<>();
        for (Game c_game : state.getGames()){
            if (c_game.gamePublicData.getId() == gameId){
                game = c_game;
                break;
            }
        }
        if (game != null){

            parameters.add(game.gameMap.getName());
            parameters.add(game.currentPlayer.playerToken);
            for (PubSubHandler handler : state.getPubSubHandlers()){
                if (handler.getGameId().equals(gameId)){
                    handler.queueNotification(new RemoteMethodCall("sendMap",parameters));
                }
            }
        }
        else {
            throw new NoSuchElementException("No game matches the given id");
        }

    }
}

package store_effects;

import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameStartGameAction;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.TimerTask;

/**
 * Created by giorgiopea on 20/03/17.
 *
 * Handles the side-effects related to the start of a game
 */
public class GameStartGameEffect implements Effect {


    @Override
    public void apply(StoreAction action, ServerState state) {
        /*
            This method notifies the clients with the token of the player who will start
            to act as first, and with the name of the game's map.
         */
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
            game.currentTimer.schedule(new TurnTimeout(gameId),state.getTurnTimeout());
            parameters.add(game.gameMap.getName());
            parameters.add(game.currentPlayer.playerToken);
            for (PubSubHandler handler : state.getPubSubHandlers()){
                if (handler.getPlayerToken().getGameId().equals(gameId)){
                    handler.queueNotification(new RemoteMethodCall("sendMap",parameters));
                }
            }
        }
        else {
            throw new NoSuchElementException("No game matches the given id");
        }

    }
}

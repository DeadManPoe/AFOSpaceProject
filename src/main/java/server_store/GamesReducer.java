package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import org.apache.commons.lang.SerializationUtils;
import sts.Action;
import sts.Reducer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 11/03/17.
 */
public class GamesReducer extends Reducer {

    public GamesReducer(List<String> writableStateKeys) {
        super(writableStateKeys);
    }

    @Override
    public ServerState reduce(Action action, ServerState state) {

        if(action.type.equals("@GAMES_ADD_PLAYER_TO_GAME")){
            Map<String,Object> actionPayload = (Map<String, Object>) action.payload;
            PlayerToken token = (PlayerToken) actionPayload.get("player_token");
            Integer gameId = (Integer) actionPayload.get("game_id");
            /**
             * @TODO Implementing deep copy of hashmaps
             */
            //ServerState newState = (ServerState) SerializationUtils.clone(state);
            Map<Integer, server_store.Game> idGameMap = state.GAMES_BY_ID;
            Map<PlayerToken,Game> playerTokenGameMap = state.GAMES_BY_PLAYERTOKEN;
            playerTokenGameMap.put(token,idGameMap.get(gameId));
            newState.GAMES_BY_PLAYERTOKEN = playerTokenGameMap;
            return newState;
        }
        else if(action.type.equals("@GAMES_ADD_GAME")){
            Game game = (Game) action.payload;
            /**
             * @TODO Implementing deep copy of hashmaps
             */
            //ServerState newState = (ServerState) SerializationUtils.clone(state);
            ServerState newState = new ServerState(state);
            Map<Integer, Game> idGameMap = state.GAMES_BY_ID;
            idGameMap.put(game.getId(),game);
            newState.GAMES_BY_ID = idGameMap;
            return newState;
        }
        else if(action.type.equals("@GAMES_REMOVE_GAME")){
            Integer gameId = (Integer) action.payload;
            /**
             * @TODO Implementing deep copy of hashmaps
             */
            //ServerState newState = (ServerState) SerializationUtils.clone(state);
            ServerState newState = new ServerState(state);
            Map<Integer, Game> idGameMap = state.GAMES_BY_ID;
            idGameMap.remove(gameId);
            newState.GAMES_BY_ID = idGameMap;
            return newState;
        }
        else if(action.type.equals("@GAMES_REMOVE_PLAYER")){
            PlayerToken playerToken = (PlayerToken) action.payload;
            /**
             * @TODO Implementing deep copy of hashmaps
             */
            //ServerState newState = (ServerState) SerializationUtils.clone(state);
            ServerState newState = new ServerState(state);
            Map<PlayerToken, Game> playerTokenGameMap = state.GAMES_BY_PLAYERTOKEN;
            playerTokenGameMap.remove(playerToken);
            newState.GAMES_BY_PLAYERTOKEN = playerTokenGameMap;
            return newState;
        }
        return state;
    }
}

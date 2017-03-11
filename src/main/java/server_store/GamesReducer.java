package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
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
    public Map<String, Object> reduce(Action action, Map<String, Object> state) {

        if(action.type.equals("@GAMES_ADD_PLAYER_TO_GAME")){
            Map<String,Object> actionPayload = (Map<String, Object>) action.payload;
            PlayerToken token = (PlayerToken) actionPayload.get("player_token");
            Integer gameId = (Integer) actionPayload.get("game_id");
            Map<String,Object> newState = new HashMap<String,Object>(state);
            Map<Integer, Game> idGameMap = (Map<Integer, Game>) state.get("games_by_id");
            Map<PlayerToken,Game> playerTokenGameMap = (Map<PlayerToken, Game>) state.get("games_by_player");
            playerTokenGameMap.put(token,idGameMap.get(gameId));
            newState.put("games_by_player", new HashMap<PlayerToken,Game>(playerTokenGameMap));
            return newState;
        }
        else if(action.type.equals("@GAMES_ADD_GAME")){
            Game game = (Game) action.payload;
            Map<String,Object> newState = new HashMap<String,Object>(state);
            Map<Integer, Game> idGameMap = (Map<Integer, Game>) state.get("games_by_id");
            idGameMap.put(game.getId(),game);
            newState.put("games_by_id", new HashMap<Integer,Game>(idGameMap));
            return newState;
        }
        else if(action.type.equals("@GAMES_REMOVE_GAME")){
            Integer gameId = (Integer) action.payload;
            Map<String,Object> newState = new HashMap<String,Object>(state);
            Map<Integer, Game> idGameMap = (Map<Integer, Game>) state.get("games_by_id");
            idGameMap.remove(gameId);
            newState.put("games_by_id", new HashMap<Integer,Game>(idGameMap));
            return newState;
        }
        else if(action.type.equals("@GAMES_REMOVE_PLAYER")){
            PlayerToken playerToken = (PlayerToken) action.payload;
            Map<String,Object> newState = new HashMap<String,Object>(state);
            Map<PlayerToken, Game> playerTokenGameMap = (Map<PlayerToken, Game>) state.get("games_by_player");
            playerTokenGameMap.remove(playerToken);
            newState.put("games_by_player", new HashMap<PlayerToken,Game>(playerTokenGameMap));
            return newState;
        }
        return state;
    }
}

package server_store;

import common.PlayerToken;
import it.polimi.ingsw.cg_19.Game;
import sts.ActionFactory;
import sts.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 09/03/17.
 *
 */
public class ServerStore {

    private Store serverStore = Store.getInstance();
    private ActionFactory actionFactory = ActionFactory.getInstance();

    public ServerStore(Store serverStore) {
        this.produceInitialState();
    }

    private void produceInitialState(){
        Map<String,Object> initialState = new HashMap<String,Object>();
        //Games by Id
        Map<String, Game> gamesById = new HashMap<String, Game>();
        //Games by PlayerToken
        Map<PlayerToken, Game> gamesByPlayerToken = new HashMap<PlayerToken, Game>();

        initialState.put("games_by_id",gamesById);
        initialState.put("games_by_player", gamesByPlayerToken);
        this.serverStore.init(initialState);
    }
    private void produceActions(){
        List<String> actions = new ArrayList<String>();
        //Games level actions
        actions.add("@GLOBAL_ADD_GAME");
        actions.add("@GLOBAL_REMOVE_GAME");
        //Debatable
        actions.add("@GLOBAL_ADD_PLAYER_TO_GAME");
        //
        //Game level actions
        actions.add("@GAME_ADD_PLAYER");
        actions.add("@GAME_START_GAME");
        actions.add("@GAME_MAKE_ACTION");

        actionFactory.init(actions);
    }

    public Store getServerStore() {
        return serverStore;
    }
}


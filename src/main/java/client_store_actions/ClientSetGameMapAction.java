package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientSetGameMapAction extends StoreAction {

    public String payload;

    public ClientSetGameMapAction(String mapName) {
        this.type = "@CLIENT_SET_GAME_MAP";
        this.payload = mapName;
    }
}

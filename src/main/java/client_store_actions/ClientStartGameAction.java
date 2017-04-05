package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientStartGameAction extends StoreAction {

    public final String gameMapName;

    public ClientStartGameAction(String gameMapName) {
        this.type = "@CLIENT_START_GAME";
        this.gameMapName = gameMapName;
    }
}

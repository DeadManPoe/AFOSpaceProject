package client_store_actions;

import common.PlayerToken;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientSetPlayerTokenAction extends StoreAction {

    public PlayerToken payload;

    public ClientSetPlayerTokenAction(PlayerToken token) {
        this.type = "@CLIENT_SET_PLAYER_TOKEN";
        this.payload = token;
    }
}

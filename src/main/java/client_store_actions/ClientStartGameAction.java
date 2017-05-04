package client_store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientStartGameAction extends StoreAction {

    private final String gameMapName;

    public ClientStartGameAction(String gameMapName) {
        super("@CLIENT_START_GAME");
        this.gameMapName = gameMapName;
    }


    public String getGameMapName() {
        return gameMapName;
    }
}

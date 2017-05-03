package client_store_actions;

import common.PlayerToken;
import server_store.StoreAction;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientSetPlayerAction extends StoreAction {

    private final String playerName;
    private final PlayerToken playerToken;

    public ClientSetPlayerAction(String playerName, PlayerToken playerToken) {
        super("@CLIENT_SET_PLAYER");
        this.playerName = playerName;
        this.playerToken = playerToken;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }
}

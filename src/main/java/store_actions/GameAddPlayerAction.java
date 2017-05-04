package store_actions;

import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameAddPlayerAction extends StoreAction {

    private final int gameId;
    private final String playerName;

    public GameAddPlayerAction(int gameId, String playerName) {
        super("@GAME_ADD_PLAYER");
        this.gameId = gameId;
        this.playerName = playerName;
    }

    public int getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
}

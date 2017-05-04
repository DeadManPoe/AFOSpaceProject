package store_actions;

import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameAddPlayerAction extends StoreAction {

    private GameAddPlayerActionPayload payload;

    public GameAddPlayerAction(UUID reqRespHandlerUUID, Integer gameId, String playerName) {
        super("@GAME_ADD_PLAYER");
        this.payload = new GameAddPlayerActionPayload(reqRespHandlerUUID, gameId,playerName);
    }

    public GameAddPlayerActionPayload getPayload() {
        return payload;
    }
}
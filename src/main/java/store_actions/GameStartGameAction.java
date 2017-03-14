package store_actions;

import java.util.UUID;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameStartGameAction extends StoreAction {

    private GameStartGameActionPayload payload;

    public GameStartGameAction(Integer gameId, UUID handlerUUID) {
        this.type = "@GAME_START_GAME";
        this.payload = new GameStartGameActionPayload(gameId,handlerUUID);
    }

    public GameStartGameActionPayload getPayload() {
        return payload;
    }
}

package store_actions;

import java.util.UUID;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameStartGameActionPayload {
    private Integer gameId;
    private UUID handlerUUID;

    public GameStartGameActionPayload(Integer gameId, UUID handlerUUID) {
        this.gameId = gameId;
        this.handlerUUID = handlerUUID;
    }

    public Integer getGameId() {
        return gameId;
    }

    public UUID getHandlerUUID() {
        return handlerUUID;
    }
}

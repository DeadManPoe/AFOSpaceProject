package store_actions;

import common.Action;
import common.PlayerToken;

import java.util.UUID;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameMakeActionActionPayload {

    private Integer gameId;
    private PlayerToken playerToken;
    private UUID reqRespHandlerUUID;
    private Action action;

    public GameMakeActionActionPayload(Integer gameId, PlayerToken playerToken, UUID reqRespHandlerUUID, Action action) {
        this.gameId = gameId;
        this.playerToken = playerToken;
        this.reqRespHandlerUUID = reqRespHandlerUUID;
        this.action = action;

    }
    public Action getAction() {
        return action;
    }

    public Integer getGameId() {
        return gameId;
    }

    public PlayerToken getPlayerToken() {
        return playerToken;
    }

    public UUID getReqRespHandlerUUID() {
        return reqRespHandlerUUID;
    }
}

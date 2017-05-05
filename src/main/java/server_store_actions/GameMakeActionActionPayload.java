package server_store_actions;

import common.PlayerToken;
import server_store.StoreAction;

import java.util.UUID;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameMakeActionActionPayload {

    public PlayerToken playerToken;
    public UUID reqRespHandlerUUID;
    public StoreAction action;

    public GameMakeActionActionPayload(PlayerToken playerToken, UUID reqRespHandlerUUID, StoreAction action) {
        this.playerToken = playerToken;
        this.reqRespHandlerUUID = reqRespHandlerUUID;
        this.action = action;
    }
}

package store_actions;

import server_store.CommunicationHandler;
import server_store.GamePlayerCommunicationSocket;
import sts.Action;

import java.net.Socket;
import java.util.UUID;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameAddPlayerAction extends StoreAction {

    private GameAddPlayerActionPayload payload;

    public GameAddPlayerAction(UUID reqRespHandlerUUID, Integer gameId, String playerName) {
        this.type = "@GAME_ADD_PLAYER";
        this.payload = new GameAddPlayerActionPayload(reqRespHandlerUUID, gameId,playerName);
    }

    public GameAddPlayerActionPayload getPayload() {
        return payload;
    }
}

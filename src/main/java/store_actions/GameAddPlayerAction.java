package store_actions;

import server_store.CommunicationHandler;
import server_store.GameIdPlayerNameHandler;
import sts.Action;

import java.net.Socket;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GameAddPlayerAction extends Action {
    public GameIdPlayerNameHandler payload;

    public GameAddPlayerAction(String playerName, Integer gameId, CommunicationHandler communicationHandler, Socket socket) {
        super("@GAME_ADD_PLAYER");
        this.payload = new GameIdPlayerNameHandler(gameId, playerName, communicationHandler, socket);
    }

}

package server_store;

import java.net.Socket;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class GamePlayerCommunicationSocket {
    public Integer gameId;
    public String playerName;
    public CommunicationHandler communicationHandler;
    public Socket socket;

    public GamePlayerCommunicationSocket(Integer gameId, String playerName, CommunicationHandler communicationHandler, Socket socket) {
        this.gameId = gameId;
        this.playerName = playerName;
        this.communicationHandler = communicationHandler;
        this.socket = socket;
    }
}

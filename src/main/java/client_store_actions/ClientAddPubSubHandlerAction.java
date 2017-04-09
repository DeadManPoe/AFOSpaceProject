package client_store_actions;

import server_store.StoreAction;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientAddPubSubHandlerAction extends StoreAction {

    public final Socket socket;
    public final ObjectInputStream inputStream;

    public ClientAddPubSubHandlerAction(Socket socket, ObjectInputStream inputStream) {
        super("@COMMUNICATION_ADD_PUB_SUB_HANDLER");
        this.socket = socket;
        this.inputStream = inputStream;
    }
}

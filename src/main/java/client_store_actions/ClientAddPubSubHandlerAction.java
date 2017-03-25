package client_store_actions;

import server_store.StoreAction;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientAddPubSubHandlerAction extends StoreAction {

    public final ClientAddPubSubHandlerPayload payload;

    public ClientAddPubSubHandlerAction(Socket socket, ObjectInputStream inputStream) {
        this.type = "@COMMUNICATION_ADD_PUB_SUB_HANDLER";
        this.payload = new ClientAddPubSubHandlerPayload(socket,inputStream);
    }
}

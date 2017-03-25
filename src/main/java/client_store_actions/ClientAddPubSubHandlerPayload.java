package client_store_actions;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class ClientAddPubSubHandlerPayload {

    public Socket socket;
    public ObjectInputStream inputStream;

    public ClientAddPubSubHandlerPayload(Socket socket, ObjectInputStream inputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
    }
}

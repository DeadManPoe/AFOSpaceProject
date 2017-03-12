package server_store;

import java.io.IOException;

/**
 * Created by giorgiopea on 10/03/17.
 */
public class ServerMain {

    private static int TCP_PORT = 29999;

    public static void main(String[] args) {
        ServerStore.produceInitialState();
        ServerStore.produceActions();
        ServerStore.registerReducers();
        CommunicationHandler communicationHandler = new CommunicationHandler(TCP_PORT);
        ServerStore.serverStore.observeState(communicationHandler);
        try {
            communicationHandler.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

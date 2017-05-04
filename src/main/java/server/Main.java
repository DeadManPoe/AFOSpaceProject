package server;

import server.CommunicationHandler;

import java.io.IOException;

/**
 * Created by giorgiopea on 10/03/17.
 */
public class Main {

    private static int TCP_PORT = 29999;

    public static void main(String[] args) {
        CommunicationHandler communicationHandler = new CommunicationHandler(TCP_PORT);
        //ServerStore.serverStore.observeState(communicationHandler);
        try {
            communicationHandler.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

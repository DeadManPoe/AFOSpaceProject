package client;

import common.RemoteMethodCall;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Represents a thread that handles async messages from the server in the logic
 * of the pub/sub pattern(the client is the subscriber)
 *
 */
public class PubSubHandler extends Thread {

    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ClientServices clientServices;
    private boolean listeningFlag;

    public PubSubHandler(Socket socket, ObjectInputStream inputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.listeningFlag = true;
        this.clientServices = ClientServices.getInstance();
    }

    public void setListeningFlag(boolean listeningFlag) {
        this.listeningFlag = listeningFlag;
    }

    @Override
    public void run() {
        while (this.listeningFlag) {
            try {
                RemoteMethodCall remoteMethodCall = (RemoteMethodCall) this.inputStream.readObject();
                this.clientServices.processRemoteInvocation(remoteMethodCall);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException |
                    IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                try {
                    this.inputStream.close();
                    this.socket.close();
                    this.listeningFlag = false;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            this.inputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

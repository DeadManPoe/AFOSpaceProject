package client;

import client_store.ClientStore;
import client_store_actions.ClientRemovePubSubHandlerAction;
import client_store_actions.ClientSetConnectionActiveAction;
import common.RemoteMethodCall;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Represents a thread that handles async messages from the server, in the logic
 * of the pub/sub pattern(the client is the subscriber).
 *
 */
public class PubSubHandler extends Thread {

    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ClientStore clientStore;
    private final ClientServices clientServices;

    public PubSubHandler(Socket socket, ObjectInputStream inputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.clientStore = ClientStore.getInstance();
        this.clientServices = ClientServices.getInstance();
    }
    @Override
    public void run() {
        while (this.clientStore.getState().getCurrentPubSubHandler() != null) {
            try {
                RemoteMethodCall methodCall = (RemoteMethodCall) this.inputStream.readObject();
                this.clientServices.processRemoteInvocation(methodCall);
            }
            catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e){
                try {
                    this.inputStream.close();
                    this.socket.close();
                    this.clientStore.dispatchAction(new ClientRemovePubSubHandlerAction());
                    this.clientStore.dispatchAction(new ClientSetConnectionActiveAction(false));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }


}

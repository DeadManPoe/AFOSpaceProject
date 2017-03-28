package client_store;

import common.RemoteMethodCall;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Created by giorgiopea on 25/03/17.
 *
 */
public class PubSubHandler extends Thread {

    public Socket socket;
    public ObjectInputStream inputStream;

    public PubSubHandler(Socket socket, ObjectInputStream inputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
    }
    @Override
    public void run() {
        while (ClientStore.getInstance().getState().currentPubSubHandler != null) {
            try {
                RemoteMethodCall methodCall = (RemoteMethodCall) this.inputStream.readObject();
                InteractionManager.getInstance().processRemoteInvocation(methodCall);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


}

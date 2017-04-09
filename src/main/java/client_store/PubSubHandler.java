package client_store;

import client_store_actions.ClientSetConnectionActiveAction;
import common.RemoteMethodCall;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

/**
 * Created by giorgiopea on 25/03/17.
 * A thread that listens for incoming data from a given socket input stream.
 * This data is always supposed to be a {@link RemoteMethodCall} object, which embeds
 * a method name and a list of arguments, so that a method that matches them can be invoked
 * in the context of the {@link InteractionManager}.
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
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (IOException e){
                //The connection is down
                ClientStore.getInstance().dispatchAction(new ClientSetConnectionActiveAction(false));
            }
        }
    }


}

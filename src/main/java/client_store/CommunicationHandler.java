package client_store;

import client_store_actions.ClientAddPubSubHandlerAction;
import common.RemoteMethodCall;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.concurrent.ExecutorService;

/**
 * Created by giorgiopea on 25/03/17.
 */
public class CommunicationHandler {
    private static CommunicationHandler instance = new CommunicationHandler();
    private final ClientStore clientStore;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public static CommunicationHandler getInstance(){
        return instance;
    }

    private CommunicationHandler(){
        clientStore = ClientStore.getInstance();
    }

    public RemoteMethodCall newComSession(RemoteMethodCall remoteMethodCall) throws IOException, ClassNotFoundException {
        RemoteMethodCall methodCallToExecute = null;
        this.socket = new Socket(clientStore.getState().host, clientStore.getState().tcpPort);
        this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        this.sendData(remoteMethodCall);
        if ( !remoteMethodCall.getMethodName().equals("subscribe")){
            if ( !remoteMethodCall.getMethodName().equals("publishGlobalMessage")){
                methodCallToExecute = this.receiveData(inputStream);
            }
            this.closeDataFlow();
            return methodCallToExecute;
        }
        else {
            this.clientStore.dispatchAction(new ClientAddPubSubHandlerAction(socket,inputStream));
        }
        return null;
    }

    private void sendData(RemoteMethodCall remoteCall) throws IOException {
        this.outputStream.writeObject(remoteCall);
        this.outputStream.flush();
    }

    /**
     * Closes the data flow relative to the data exchange
     *
     * @throws IOException
     *             signals an error in closing the remote data exchange's
     *             associated socket
     */
    private void closeDataFlow() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }



    private RemoteMethodCall receiveData(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        return (RemoteMethodCall) inputStream
                .readObject();
    }
}

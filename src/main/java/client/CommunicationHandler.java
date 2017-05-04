package client;

import client_store.ClientStore;
import client_store_actions.ClientAddPubSubHandlerAction;
import common.RemoteMethodCall;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by giorgiopea on 25/03/17.
 * <p>
 * A class that handles the communication between the client and the server
 */
public class CommunicationHandler {
    private static CommunicationHandler instance = new CommunicationHandler();
    private final ClientStore clientStore;
    private final ServerMethodsNameProvider serverMethodsNameProvider;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public static CommunicationHandler getInstance() {
        return instance;
    }

    private CommunicationHandler() {
        this.clientStore = ClientStore.getInstance();
        this.serverMethodsNameProvider = ServerMethodsNameProvider.getInstance();
    }

    /**
     * Opens a connection with the server and sends the given {@link RemoteMethodCall} object to
     * the server, it then waits for an answer and closes the connection. An exception to this behavior
     * is made for a particular {@link RemoteMethodCall} object that signals a subscription in the logic
     * of a publisher-subscriber pattern, so that the connection with the server must be preserved.
     *
     * @param remoteMethodCall The object to be sent to the server.
     * @return An object that represent a method to be invoked on the client.
     * @throws IOException            Connection problems.
     * @throws ClassNotFoundException Reflection problems.
     */
    public RemoteMethodCall newComSession(RemoteMethodCall remoteMethodCall) throws IOException, ClassNotFoundException {
        RemoteMethodCall receivedRemoteMethodCall = null;
        this.socket = new Socket(clientStore.getState().getHost(), clientStore.getState().getTcpPort());
        this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(this.socket.getInputStream());
        this.sendData(remoteMethodCall);
        if (remoteMethodCall.getMethodName().equals(this.serverMethodsNameProvider.subscribe())) {
            this.clientStore.dispatchAction(new ClientAddPubSubHandlerAction(socket, inputStream));
        } else {
            receivedRemoteMethodCall = this.receiveData(inputStream);
            this.closeDataFlow();
        }
        return receivedRemoteMethodCall;
    }

    /**
     * Writes the given {@link RemoteMethodCall} object onto the output stream of the current socket
     *
     * @param remoteCall The object to be written onto the output stream of the current socket
     * @throws IOException Connection problems
     */
    private void sendData(RemoteMethodCall remoteCall) throws IOException {
        this.outputStream.writeObject(remoteCall);
        this.outputStream.flush();
    }

    /**
     * Closes the data flow relative to the data exchange
     *
     * @throws IOException Connection problems
     */
    private void closeDataFlow() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }


    /**
     * Reads an {@link RemoteMethodCall} object from the given input stream
     *
     * @param inputStream An input stream
     * @return The object that has been read
     * @throws IOException            Connection problems
     * @throws ClassNotFoundException Reflection problems
     */
    private RemoteMethodCall receiveData(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        return (RemoteMethodCall) inputStream
                .readObject();
    }
}

package server_store;

import server.ServerConnection;
import server.ServerLogger;
import server.SocketThread;
import sts.ActionFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by giorgiopea on 10/03/17.
 */
public class ServerMain {

    private final ServerSocket serverSocket;
    private final ServerStore serverStore;
    private final ActionFactory actionFactory;
    /**
     * Starts a socket based communication
     *
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public void startSocketServer() throws IOException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, ClassNotFoundException {
        Socket socket;
        while (true) {
            // Accepts connections
            socket = serverSocket.accept();
            // Handles the request using a new thread
            serverStore.getServerStore().dispatchAction(actionFactory.
                    getAction("@COMMUNICATION_ADD_SOCKET", socket));
        }
    }

    public ServerMain(ServerStore serverStore) throws IOException {
        this.serverStore = serverStore;
        this.actionFactory = ActionFactory.getInstance();
        ServerConnection serverConnection = (ServerConnection) serverStore.getServerStore().getState().get("@COMMUNICATION_CONNECTION_INFO");
        this.serverSocket = new ServerSocket(serverConnection.getSocketPort());

    }

    public static void main(String[] args) {
        ServerStore serverStore = new ServerStore();
        ActionFactory actionFactory = ActionFactory.getInstance();
        serverStore.getServerStore().dispatchAction(actionFactory.
                getAction("@COMMUNICATION_SET_CONNECTION", new ServerConnection(1099, 29999, "AFOSpace")));
    }
}

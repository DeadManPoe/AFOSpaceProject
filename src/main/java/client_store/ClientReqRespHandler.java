package client_store;

import client.Client;
import client.ClientConnection;
import client.ClientRemoteServicesInterface;
import client.RemoteDataExchange;
import common.*;
import factories.FermiGameMapFactory;
import factories.GalileiGameMapFactory;
import factories.GalvaniGameMapFactory;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by giorgiopea on 24/03/17.
 */
public class ClientReqRespHandler {

    /*
	 * The object output stream associated with the socket used by the remote
	 * data exchange
	 */
    private final ObjectOutputStream outputStream;
    /*
     * The object input stream associated with the socket used by the remote
     * data exchange
     */
    private final ObjectInputStream inputStream;
    // The socket with the remote data exchange
    private final Socket socket;
    private final ClientStore clientStore;

    /**
     * Constructs a data exchange between the client and the server using socket
     * based communication, creating a socket with its associated object
     * streams. This data exchange is constructed from a client.
     *
     * @param client
     *            the client this data exchange refers to
     * @throws IOException
     *             signals an error in the opening of the socket's streams
     *             associated with the remote data exchange
     */
    public ClientReqRespHandler() throws IOException {
        clientStore = ClientStore.getInstance();
        try {
            socket = new Socket(clientStore.getState().host, clientStore.getState().tcpPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            socket.close();
            throw new IOException(e);
        }

    }

    public void sendData(RemoteMethodCall remoteCall) throws IOException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException,
            SecurityException, ClassNotFoundException, NotBoundException {
        try {
            outputStream.writeObject(remoteCall);
            outputStream.flush();
        } catch (IOException e) {
            socket.close();
            throw new IOException(e);
        }

    }

    /**
     * Closes the data flow relative to the data exchange
     *
     * @throws IOException
     *             signals an error in closing the remote data exchange's
     *             associated socket
     */
    public void closeDataFlow() throws IOException {
        socket.close();
    }



    public void receiveData() throws ClassNotFoundException, IOException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        RemoteMethodCall remoteCall = (RemoteMethodCall) inputStream
                .readObject();
        // Data processing: from the remote method call object to the actual
        // method invocation
        clientServices.processRemoteInvocation(remoteCall);

    }


    public void sendToken(PlayerToken token) throws IOException,
            RemoteException {
        client.setToken(token);
        try {
            client.subscribe();
        } catch (IllegalAccessException | NoSuchMethodException | NotBoundException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see ClientRemoteServicesInterface#sendAvailableGames
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    public void sendAvailableGames(ArrayList<GamePublicData> availableGames)
            throws IOException, RemoteException {
        client.setAvailableGames(availableGames);
    }

    /**
     * @see ClientRemoteServicesInterface#publishChatMsg
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    public void publishChatMsg(String msg) throws RemoteException {
        client.displayMessage(msg);
    }

    /**
     * @see ClientRemoteServicesInterface#sendNotification
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    @Override
    public void sendNotification(ClientNotification clientNotification)
            throws RemoteException, IOException {
        RRClientNotification clientNotificationa = (RRClientNotification) clientNotification;
        client.setNotification(clientNotificationa);
    }

    /**
     * @see ClientRemoteServicesInterface#sendPubNotification
     * @throws IOException
     *             signals a com. error
     * @throws RemoteException
     *             signals a com. error
     */
    @Override
    public void sendPubNotification(ClientNotification psNotification) {
        PSClientNotification notification = (PSClientNotification) psNotification;
        client.psNotify(notification);
    }

    /**
     * Processes the details of a remote method call made by the server so that
     * the right method with the specified parameters is invoked on the client
     *
     * @see RemoteMethodCall
     * @param remoteClientInvocation
     *            an object that represents a remote method call. This object
     *            specifies the name and the parameters of the method to be
     *            invoked on the client
     * @throws IllegalAccessException
     *             signals a reflection error
     * @throws InvocationTargetException
     *             signals a reflection error
     * @throws NoSuchMethodException
     *             signals a reflection error
     * @throws SecurityException
     *             signals a reflection error
     */
    public void processRemoteInvocation(RemoteMethodCall remoteClientInvocation)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        String methodName = remoteClientInvocation.getMethodName();
        ArrayList<Object> parameters = remoteClientInvocation
                .getMethodParameters();
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getClass().getName().contains("action")
                    || parameters.get(i).getClass().getName()
                    .contains("ClientNotification")) {
                parametersClasses[i] = parameters.get(i).getClass()
                        .getSuperclass();
            } else {
                parametersClasses[i] = parameters.get(i).getClass();
            }

        }
        this.getClass().getDeclaredMethod(methodName, parametersClasses)
                .invoke(this, parameters.toArray());
    }

    /**
     * @see ClientRemoteServicesInterface#sendMap
     */
    @Override
    public void sendMap(String mapName, PlayerToken playerToken) {
        if (mapName.equals("GALILEI")) {
            GalileiGameMapFactory factory = new GalileiGameMapFactory();
            GameMap map = factory.makeMap();
            client.setGameMap(map);
            client.setPrivateDeck(new PrivateDeck());
            if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
                client.setCurrentSector(map.getAlienSector());
            } else {
                client.setCurrentSector(map.getHumanSector());
            }

        } else if (mapName.equals("FERMI")) {
            FermiGameMapFactory factory = new FermiGameMapFactory();
            GameMap map = factory.makeMap();
            client.setGameMap(map);
            client.setPrivateDeck(new PrivateDeck());
            if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
                client.setCurrentSector(map.getAlienSector());
            } else {
                client.setCurrentSector(map.getHumanSector());
            }
        } else if (mapName.equals("GALVANI")) {
            GalvaniGameMapFactory factory = new GalvaniGameMapFactory();
            GameMap map = factory.makeMap();
            client.setGameMap(map);
            client.setPrivateDeck(new PrivateDeck());
            if (client.getToken().getPlayerType().equals(PlayerType.ALIEN)) {
                client.setCurrentSector(map.getAlienSector());
            } else {
                client.setCurrentSector(map.getHumanSector());
            }
        } else {
            throw new IllegalArgumentException("The type of map is undefined");
        }
        if (playerToken.equals(client.getToken())) {
            client.setIsMyTurn(true);
        }
        // START THE GAME
        synchronized (client) {
            client.setGameStarted(true);
            client.notifyAll();
        }
    }

    /**
     * @see ClientRemoteServicesInterface#kick
     */
    @Override
    public void kick(PlayerToken playerToken) {
        if (playerToken.equals(client.getToken())) {
            client.shutdown();
        }
    }

    /**
     * @see ClientRemoteServicesInterface#allowTurn
     */
    @Override
    public void allowTurn() {
        client.setIsMyTurn(true);
    }

    /**
     * @see ClientRemoteServicesInterface#endGame
     */
    @Override
    public void endGame() {
        synchronized (client) {
            client.setGameEnded(true);
            client.notifyAll();
        }
    }

    /**
     * @see ClientRemoteServicesInterface#ackMessage
     */
    @Override
    public void ackMessage() {
        return;
    }


}

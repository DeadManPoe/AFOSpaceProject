package server_store;

import common.RemoteMethodCall;
import server.MainServer;
import server.ServerLogger;
import server.SocketRemoteDataExchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Represents a thread that handles a request by a client in the logic of the
 * client server pattern
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ReqRespHandler extends Thread {
	// The socket used to handle the client's request
	private Socket socket;

	/**
	 * Constructs a thread that handles a request by a client in the logic of
	 * the client server pattern. This thread is constructed from a server and a
	 * socket that represents the client itself and that will be used to handle
	 * the client's request
	 *
	 *            the server this thread refers to
	 * @param socket
	 *            the socket this thread uses to handle the client's request and
	 *            that represents the client itself
	 */
	public ReqRespHandler(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Runs the thread. The thread handles the client's request by receiving the
	 * client data, processing it and invoking on the client a remote method,
	 * all is done through a {@link SocketRemoteDataExchange}
	 * 
	 * @see SocketRemoteDataExchange
	 */
	public void run() {
		SocketRemoteDataExchange dataExchange;
		try {
			// Client's request handling
			dataExchange = new SocketRemoteDataExchange(null, socket);
			//server.setSocketDataExchange(dataExchange);
			dataExchange.receiveData();
		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			ServerLogger.getLogger().log(Level.SEVERE,
					"Could not perform action | ReqRespHandler", e);
		} catch (IOException e) {
			ServerLogger.getLogger().log(Level.SEVERE,
					"Could not communicate with the client | ReqRespHandler", e);
		}

	}
	private RemoteMethodCall receiveData() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        RemoteMethodCall remoteMethodCall = (RemoteMethodCall) objectInputStream.readObject();
        return remoteMethodCall;
    }
	private void performReceivedMethodCall(RemoteMethodCall remoteMethodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = remoteMethodCall.getMethodName();
        ArrayList<Object> parameters = remoteMethodCall
                .getMethodParameters();
        Class<?>[] parametersClasses = new Class[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getClass().getName().contains("Action")
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
    private void closeDataFlow(){

    }
}

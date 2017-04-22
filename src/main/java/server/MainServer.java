package server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * Represents the server in the logic of the client/server pattern
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 * @see GameManager
 */
public class MainServer {
    public static void main(String[] args) {
        try {
            CommunicationHandler.getInstance().init(29999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

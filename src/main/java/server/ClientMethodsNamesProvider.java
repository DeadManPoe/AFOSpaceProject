package server;

/**
 * Created by giorgiopea on 21/04/17.
 */
public class ClientMethodsNamesProvider {
    private static ClientMethodsNamesProvider instance = new ClientMethodsNamesProvider();

    public static ClientMethodsNamesProvider getInstance() {
        return instance;
    }

    private ClientMethodsNamesProvider() {
    }

    public String sendAvailableGames(){
        return "sendAvailableGames";
    }
    public String syncNotification(){
        return "syncNotification";
    }

    public String signalStartableGame() {
        return "signalStartableGame";
    }
}

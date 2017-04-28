package server;

/**
 * Created by giorgiopea on 21/04/17.
 *
 */
public class ClientMethodsNamesProvider {
    private static ClientMethodsNamesProvider instance = new ClientMethodsNamesProvider();

    public static ClientMethodsNamesProvider getInstance() {
        return instance;
    }

    private ClientMethodsNamesProvider() {
    }

    public String sendAvailableGames(){
        return "setAvailableGames";
    }
    public String syncNotification(){
        return "syncNotification";
    }

    public String signalStartableGame() {
        return "signalStartableGame";
    }

    public String sendMapAndStartGame() {
        return "setMapAndStartGame";
    }

    public String startTurn() {
        return "startTurn";
    }

    public String forceEndTurn() {
        return "forceEndTurn";
    }

    public String asyncNotification() {
        return "asyncNotification";
    }

    public String subscribe() {
        return "setPlayerTokenAndSubscribe";
    }

    public String chatMessage() {
        return "publishChatMsg";
    }
}

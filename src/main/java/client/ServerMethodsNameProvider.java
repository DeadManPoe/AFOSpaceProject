package client;

/**
 * Created by giorgiopea on 25/04/17.
 */
public class ServerMethodsNameProvider {

    private static ServerMethodsNameProvider instance = new ServerMethodsNameProvider();
    public static ServerMethodsNameProvider getInstance(){
        return instance;
    }

    private ServerMethodsNameProvider(){

    }

    public String subscribe(){
        return "subscribe";
    }

    public String onDemandGameStart() {
        return "onDemandGameStart";
    }

    public String makeAction() {
        return "makeAction";
    }
}

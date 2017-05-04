package server;

import java.io.IOException;

/**
 * Created by giorgiopea on 10/03/17.
 *
 */
public class Main {

    public static void main(String[] args) {
        try {
            CommunicationHandler.getInstance().init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

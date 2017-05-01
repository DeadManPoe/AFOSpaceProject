package client;

import java.util.TimerTask;

/**
 * Created by giorgiopea on 01/05/17.
 */
public class EndGameTimeout  extends TimerTask {
    @Override
    public void run() {
        GuiManager.getInstance().returnToGameList();
    }
}

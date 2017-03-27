package client_gui;

import client.GUIGameList;
import client.GUIGamePane;
import client.GUInitialWindow;
import client.GamePollingThread;
import client_store.InteractionManager;
import client_store_actions.ClientSetAvGamesAction;
import server_store.StoreAction;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

/**
 * Created by giorgiopea on 27/03/17.
 */
public class GuiManager implements Observer {

    private static GuiManager instance = new GuiManager();
    private final InteractionManager interactionManager = InteractionManager.getInstance();
    private GUInitialWindow guiInitialWindow;
    private GUIGameList guiGameList;
    private GUIGamePane guiGamePane;
    private JFrame mainFrame;

    public static GuiManager getInstance(){
        return instance;
    }

    private GuiManager(){
    }

    public void initGuiComponents(){
        this.mainFrame = new JFrame("Escape from aliens in the outer space");
        this.guiInitialWindow = new GUInitialWindow();
        this.guiGameList = new GUIGameList();
        this.guiGameList
                .setLayout(new BoxLayout(this.guiGameList, BoxLayout.Y_AXIS));
        this.guiGameList.setBackground(Color.BLACK);

        this.guiGamePane = new GUIGamePane();
        this.guiGamePane.setLayout(new GridBagLayout());

        this.guiInitialWindow.setVisible(true);
        this.guiInitialWindow.Load();
        mainFrame.add(this.guiInitialWindow);
    }

    public void connectAndDisplayGames() {
        interactionManager.getGames();
        this.guiInitialWindow.setVisible(false);
        this.guiGameList.setVisible(true);
        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new GamePollingThread(), 2000,2000);
    }
    public JFrame getFrame(){
        return this.mainFrame;
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction action = (StoreAction) arg;
        if (action.getType().equals("@CLIENT_SET_AV_GAMES")){
            ClientSetAvGamesAction castedAction = (ClientSetAvGamesAction) action;
            this.guiGameList.setGameListContent(castedAction.payload);
        }
    }
}

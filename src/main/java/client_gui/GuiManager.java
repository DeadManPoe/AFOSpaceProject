package client_gui;

import client.*;
import client_store.ClientStore;
import client_store.InteractionManager;
import client_store_actions.ClientSetAvGamesAction;
import server_store.StoreAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
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
        ClientStore.getInstance().observeState(this);
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

        this.guiInitialWindow.load();
        mainFrame.getContentPane().add(this.guiInitialWindow);
        this.guiInitialWindow.setVisible(true);
    }

    public void connectAndDisplayGames() {
        try {
            this.guiInitialWindow.alertConnectionProblem(false);
            interactionManager.getGames();
            this.guiInitialWindow.setVisible(false);
            this.guiGameList
                    .setLayout(new BoxLayout(this.guiGameList, BoxLayout.Y_AXIS));
            this.guiGameList.setBackground(Color.BLACK);
            this.guiGameList.load();
            mainFrame.getContentPane().add(this.guiGameList);
            this.guiGameList.setVisible(true);
            ClientStore.getInstance().getState().gamePollingTimer = new Timer();
            ClientStore.getInstance().getState().gamePollingTimer.scheduleAtFixedRate(new GamePollingThread(), 2000,10000);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            this.guiInitialWindow.alertConnectionProblem(true);
        }

    }
    public void forwardMethod(String methodName, List<Object> parameters){
        try {
            this.interactionManager.executeMethod(methodName, parameters);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
        else if (action.getType().equals("@CLIENT_START_GAME")){
            this.guiGameList.setVisible(false);
            this.guiGamePane.load("GALILEI");
            this.mainFrame.add(this.guiGamePane);
            this.guiGamePane.setVisible(true);
        }
    }

    public void joinNewGame(String mapName, String playerName) {
        interactionManager.joinNewGame(mapName,playerName);
    }

}

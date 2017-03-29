package client_gui;

import client.*;
import client_store.ClientStore;
import client_store.InteractionManager;
import client_store_actions.ClientSetAvGamesAction;
import client_store_actions.ClientSetCurrentMessage;
import common.*;
import it.polimi.ingsw.cg_19.PlayerType;
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

    public static GuiManager getInstance() {
        return instance;
    }

    private GuiManager() {
        ClientStore.getInstance().observeState(this);
    }

    public void initGuiComponents() {
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
            ClientStore.getInstance().getState().gamePollingTimer.scheduleAtFixedRate(new GamePollingThread(), 2000, 10000);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            this.guiInitialWindow.alertConnectionProblem(true);
        }

    }

    public void forwardMethod(String methodName, List<Object> parameters) {
        try {
            this.interactionManager.executeMethod(methodName, parameters);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private void updatePosition() {
        Coordinate newCoordinates = ClientStore.getInstance().getState().player.currentSector.getCoordinate();
        String playerName = ClientStore.getInstance().getState().player.name;
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(newCoordinates, "Y",
                playerName);
    }

    public void move(Coordinate coordinate) {
        this.interactionManager.move(coordinate);
        boolean hasMoved = ClientStore.getInstance().getState().player.hasMoved;

        if (hasMoved) {
            this.updatePosition();
            try {
                this.handleAction();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            this.guiGamePane.setStateMessage("You cannot move in that sector!");
        }
    }

    public JFrame getFrame() {
        return this.mainFrame;
    }


    private void handleAction() throws ClassNotFoundException {
        Card firstCard = null, secondCard = null;
        boolean isAskingLights = ClientStore.getInstance().getState().askLights;
        boolean isAskingAttack = ClientStore.getInstance().getState().askAttack;
        boolean hasMoved = ClientStore.getInstance().getState().player.hasMoved;
        PlayerType playerType = ClientStore.getInstance().getState().player.playerType;
        RRClientNotification currentReqRespNotification = ClientStore.getInstance().getState().currentReqRespNotification;
        PrivateDeck clientPrivateDeck = ClientStore.getInstance().getState().player.privateDeck;

        //this.setGUIState(true);
        this.guiGamePane.appendMsg(currentReqRespNotification.getMessage());
        List<Card> drawedCards = currentReqRespNotification.getDrawnCards();

        if (isAskingLights) {
            this.guiGamePane
                    .setStateMessage("Select a sector for the light cards!");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.LIGHT_MENU);
        } else if (isAskingAttack) {
            this.guiGamePane
                    .setStateMessage("Select a sector fot the attack card!");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.ATTACK_MENU);
        } else if (!drawedCards.isEmpty()) {
            firstCard = drawedCards.get(0);
            if (drawedCards.size() > 1){
                secondCard = drawedCards.get(1);
                clientPrivateDeck.addCard(
                        (ObjectCard) secondCard);
                this.guiGamePane
                        .addCardToPanel((ObjectCard) secondCard);
            }

            CardSplashScreen splash = new CardSplashScreen(firstCard,
                  secondCard, mainFrame);

            if (firstCard instanceof GlobalNoiseSectorCard) {
                this.guiGamePane
                        .setStateMessage("Select a sector for the global noise card!");
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.NOISE_MENU);
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                // I've drawn a object card
            }
            else if (clientPrivateDeck.getSize() > 3){
                this.manyCardHandler(playerType);
            }
            else {
                this.guiGamePane
                        .setStateMessage("Continue your turn...");
                this.guiGamePane.showEndTurnButton(true);
            }
        } else {
            if (clientPrivateDeck.getSize() > 3){
                this.manyCardHandler(playerType);
            }
            else if (playerType.equals(PlayerType.HUMAN)) {
                this.guiGamePane
                        .setStateMessage("Continue your turn...");
                if (hasMoved){
                    this.guiGamePane.showEndTurnButton(true);
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.EMPTY);
                }
                else{
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.HUMAN_INITIAL);
                    this.guiGamePane.showEndTurnButton(false);
                }

            } else {
                if (hasMoved){
                    this.guiGamePane.showEndTurnButton(true);
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.EMPTY);
                }
                else{
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.ALIEN_INITIAL);
                    this.guiGamePane.showEndTurnButton(false);
                }
            }
        }
    }
    private void manyCardHandler(PlayerType playerType) {

        if (playerType.equals(PlayerType.HUMAN)) {
            this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_DISC_MENU);
        } else {
            this.guiGamePane.changeCardMenu(MenuType.ALIEN_CARD_MENU);
        }

        this.guiGamePane.setStateMessage("Use or discard an object card");
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        this.guiGamePane.showEndTurnButton(false);
        this.guiGamePane.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction action = (StoreAction) arg;
        if (action.getType().equals("@CLIENT_SET_AV_GAMES")) {
            ClientSetAvGamesAction castedAction = (ClientSetAvGamesAction) action;
            this.guiGameList.setGameListContent(castedAction.payload);
        } else if (action.getType().equals("@CLIENT_START_GAME")) {
            String welcomeMsg = "Welcome, " + ClientStore.getInstance().getState().player.name + " you're "
                    + ClientStore.getInstance().getState().player.playerType;
            if (ClientStore.getInstance().getState().isMyTurn) {
                welcomeMsg += " - It's your turn!";
            } else {
                welcomeMsg += " - Waiting your turn!";
            }
            this.guiGameList.setVisible(false);
            this.guiGamePane.setVisible(true);
            this.guiGamePane.load(ClientStore.getInstance().getState().gameMap.getName());
            this.guiGamePane.setStateMessage(welcomeMsg);
            this.guiGamePane.getMapPane().lightSector(
                    ClientStore.getInstance().getState().player.currentSector.getCoordinate(), "Y", ClientStore.getInstance().getState().player.name);
            this.mainFrame.add(this.guiGamePane);
            if (ClientStore.getInstance().getState().player.playerType.equals(PlayerType.ALIEN)) {
                this.guiGamePane.setSectorMenu(MenuType.ALIEN_INITIAL);
            } else {
                this.guiGamePane.setSectorMenu(MenuType.HUMAN_INITIAL);
            }

        } else if (action.getType().equals("@CLIENT_PUBLISH_MSG")) {
            ClientSetCurrentMessage castedAction = (ClientSetCurrentMessage) action;
            this.guiGamePane.appendMsg(castedAction.payload);
        } else if (action.getType().equals("@CLIENT_MOVE_TO_SECTOR")) {

        }
    }

    public void joinNewGame(String mapName, String playerName) {
        interactionManager.joinNewGame(mapName, playerName);
    }

}

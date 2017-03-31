package client_gui;

import client.*;
import client_store.ClientStore;
import client_store.InteractionManager;
import client_store_actions.ClientSetAvGamesAction;
import client_store_actions.ClientSetCurrentMessage;
import client_store_actions.ClientSetCurrentPubSubNotificationAction;
import client_store_actions.ClientSetCurrentReqRespNotificationAction;
import common.*;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.StoreAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
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

    private void updateGuiState() {
        boolean isMyTurn = ClientStore.getInstance().getState().isMyTurn;
        PlayerType playerType = ClientStore.getInstance().getState().player.playerType;
        if (isMyTurn) {
            this.guiGamePane.setStateMessage("It's your turn!");
            if (playerType.equals(PlayerType.HUMAN)) {
                this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.HUMAN_INITIAL);
            } else {
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.ALIEN_INITIAL);
            }
        } else {
            this.guiGamePane.setStateMessage("Waiting your turn!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        }
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

    private void updateCardsPanel() {
        PrivateDeck privateDeck = ClientStore.getInstance().getState().player.privateDeck;
        for (ObjectCard card : privateDeck.getContent()) {
            this.guiGamePane.addCardToPanel(card);
        }
        this.guiGamePane.repaint();
    }

    public void attack(Coordinate coords) {
        this.interactionManager.attack(coords);
        this.updatePosition();
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void move(Coordinate coordinate) {
        this.interactionManager.move(coordinate);
        boolean hasMoved = ClientStore.getInstance().getState().player.hasMoved;

        if (hasMoved) {
            this.updatePosition();
            this.guiGamePane.setStateMessage("You have moved to sector "+ClientStore.getInstance().getState().player.currentSector.getCoordinate());
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
        Card firstCard, secondCard = null;
        boolean isAskingLights = ClientStore.getInstance().getState().askLights;
        boolean isAskingAttack = ClientStore.getInstance().getState().askAttack;
        boolean hasMoved = ClientStore.getInstance().getState().player.hasMoved;
        PlayerType playerType = ClientStore.getInstance().getState().player.playerType;
        RRClientNotification currentReqRespNotification = ClientStore.getInstance().getState().currentReqRespNotification;
        PrivateDeck clientPrivateDeck = ClientStore.getInstance().getState().player.privateDeck;

        List<Card> drawedCards = currentReqRespNotification.getDrawnCards();

        if (isAskingLights) {
            this.guiGamePane
                    .setStateMessage("Select a sector for the light card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.LIGHT_MENU);
        } else if (isAskingAttack) {
            this.guiGamePane
                    .setStateMessage("Select a sector fot the attack card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.ATTACK_MENU);
        } else if (!drawedCards.isEmpty()) {
            firstCard = drawedCards.get(0);
            if (drawedCards.size() > 1) {
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
                        .setStateMessage("Select a sector for the global noise card");
                this.guiGamePane.getMapPane().changeMapMenu(
                        MenuType.NOISE_MENU);
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                // I've drawn a object card
            }
            else {
                if (firstCard instanceof SilenceSectorCard){
                    this.guiGamePane
                            .setStateMessage("You will not reveal any clue about your position");
                    this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
                }
                if (firstCard instanceof LocalNoiseSectorCard){
                    this.guiGamePane.setStateMessage("You have revealed your position");
                    this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
                }
                if (clientPrivateDeck.getSize() > 3){
                    this.manyCardHandler(playerType);
                }
                else {
                    this.guiGamePane.showEndTurnButton(true);
                }

            }
        } else {
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler(playerType);
            } else if (playerType.equals(PlayerType.HUMAN)) {
                if (hasMoved) {
                    this.guiGamePane.showEndTurnButton(true);
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.EMPTY);
                    this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
                } else {
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.HUMAN_INITIAL);
                    this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
                }

            } else {
                if (hasMoved) {
                    this.guiGamePane.showEndTurnButton(true);
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.EMPTY);
                } else {
                    this.guiGamePane.getMapPane().changeMapMenu(
                            MenuType.ALIEN_INITIAL);
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
    }

    private void showMessageBox(String message) {
        JOptionPane.showMessageDialog(mainFrame, message);
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
            this.mainFrame.repaint();

        } else if (action.getType().equals("@CLIENT_PUBLISH_MSG")) {
            ClientSetCurrentMessage castedAction = (ClientSetCurrentMessage) action;
            this.guiGamePane.appendMsg(castedAction.payload);
        } else if (action.getType().equals("@CLIENT_ALLOW_TURN")) {
            updateGuiState();
        } else if (action.getType().equals("@CLIENT_SET_CURRENT_PUBSUB_NOTIFICATION")) {
            ClientSetCurrentPubSubNotificationAction castedAction = (ClientSetCurrentPubSubNotificationAction) action;
            this.guiGamePane.appendMsg(castedAction.payload.getMessage());
            this.processPSNotification(castedAction.payload);
        }
        else if (action.getType().equals("@CLIENT_SET_CURRENT_REQRESP_NOTIFICATION")){
            ClientSetCurrentReqRespNotificationAction castedAction = (ClientSetCurrentReqRespNotificationAction) action;
            this.guiGamePane.setStateMessage(castedAction.payload.getMessage());
        }
        else if (action.getType().equals("@CLIENT_DENY_TURN")){
            this.updateGuiState();
        }
    }

    private void processPSNotification(PSClientNotification notification) {
        PlayerToken playerToken = ClientStore.getInstance().getState().player.playerToken;
        if (notification.getEscapedPlayer() != null) {
            if (notification.getEscapedPlayer().equals(playerToken)) {
                this.guiGamePane.setStateMessage("You're ESCAPED!");
                this.guiGamePane.changeCardMenu(MenuType.EMPTY);
                this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
                this.guiGamePane.showEndTurnButton(false);
            }
        }

        if (notification.getAlienWins()) {
            if (notification.getHumanWins()) {
                GUIFinalWindow finalWindow = new GUIFinalWindow(this.mainFrame,
                        "HumanAlienW.png", true);
            } else {
                GUIFinalWindow finalWindow = new GUIFinalWindow(this.mainFrame,
                        "AlienW.png", true);
            }
        } else if (notification.getHumanWins()) {
            GUIFinalWindow finalWindow;
            if (notification.getAlienWins()) {
                finalWindow = new GUIFinalWindow(this.mainFrame,
                        "HumanAlienW.png", true);
            } else {
                finalWindow = new GUIFinalWindow(this.mainFrame, "HumanW.png",
                        true);
            }
        }
        if (notification.getDeadPlayers().contains(playerToken)) {
            GUIFinalWindow deadWindow = new GUIFinalWindow(this.mainFrame,
                    "dead.jpg", false);
            this.guiGamePane.setStateMessage("You're DEAD!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.showEndTurnButton(false);
        } else if (notification.getAttackedPlayers()
                .contains(playerToken)) {
            this.showMessageBox("You've used your Defence Object card, You're still alive!");
            ObjectCard toRemove = null;
            for (ObjectCard c : ClientStore.getInstance().getState().player.privateDeck.getContent()) {
                if (c instanceof DefenseObjectCard) {
                    toRemove = c;
                    break;
                }
            }
            ClientStore.getInstance().getState().player.privateDeck.getContent().remove(toRemove);
            this.guiGamePane.removeAllCardsFromPanel();
            this.updateCardsPanel();
            this.guiGamePane.setStateMessage("You're safe!");
        }
    }

    private void endGame(boolean humansHaveWon, boolean aliensHaveWon) {
        //Go to final window
    }

    public void joinNewGame(String mapName, String playerName) {
        interactionManager.joinNewGame(mapName, playerName);
    }

    /**
     * Produces a noise effect to the given coordinates
     */
    public void noise(Coordinate coords) {
        this.interactionManager.globalNoise(coords, true);
        this.guiGamePane.setStateMessage("You've made noise on sector "
                + coords.getX() + "" + coords.getY());
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates the map panel after using a light object card It displays the
     * lighted sector and evidence the player in that sector
     *
     * @param sectors The list of sectord to light
     */
    private void updateLightedSector(List<Sector> sectors) {
        String nameList = "";
        for (Sector s : sectors) {
            nameList = "";
            for (server_store.Player p : s.getPlayers()) {
                nameList += " " + p.name;
            }
            this.guiGamePane.getMapPane().lightSector(s.getCoordinate(), "P",
                    nameList);
        }
    }

    /**
     * Produces a light effect to the given coordinates
     */
    public void lights(Coordinate coords) {
        this.interactionManager.lights(coords);
        this.updateLightedSector(ClientStore.getInstance().getState().currentReqRespNotification
                .getLightedSectors());
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        this.guiGamePane.setStateMessage("You've lighted sector "
                + coords.getX() + "" + coords.getY());
    }

    /**
     * Allows the client to use an object card
     */
    public void useObjectCard(ObjectCard card) {
        int index = ClientStore.getInstance().getState().player.privateDeck.getContent().indexOf(card);
        try {
            this.interactionManager.useObjCard(index + 1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.updatePosition();
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!ClientStore.getInstance().getState().currentReqRespNotification.getActionResult())
            this.guiGamePane.setStateMessage("You cannot use this card!");
        else {
            if (!(card instanceof LightsObjectCard) && !(card instanceof AttackObjectCard))
                this.guiGamePane.setStateMessage("You've used an object card!");
        }
    }

    /**
     * Allows the client to discard an object card
     */
    public void discard(ObjectCard card) {
        int index = ClientStore.getInstance().getState().player.privateDeck.getContent().indexOf(card);
        this.interactionManager.discardCard(index + 1);
        this.guiGamePane.removeAllCardsFromPanel();
        this.updateCardsPanel();
        try {
            this.handleAction();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.guiGamePane.setStateMessage("You've discarded an object card");
    }

    /**
     * Allows the user to terminate its turn
     */
    public void endTurn() {
        this.interactionManager.endTurn();
        this.updateGuiState();
        this.guiGamePane.showEndTurnButton(false);
    }

}

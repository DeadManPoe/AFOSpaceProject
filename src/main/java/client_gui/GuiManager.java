package client_gui;

import client.*;
import client_store.ClientStore;
import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
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
    private final ClientStore clientStore = ClientStore.getInstance();
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


    private void updatePosition() {
        Coordinate newCoordinates = this.clientStore.getState().player.currentSector.getCoordinate();
        String playerName = this.clientStore.getState().player.name;
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(newCoordinates, "Y",
                playerName);
    }


    public JFrame getFrame() {
        return this.mainFrame;
    }



    private void manyCardHandler() {
        PlayerType playerType = this.clientStore.getState().player.playerToken.playerType;
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
            this.guiGamePane.getMapPane().lightSector(s.getCoordinate(), "",
                    nameList);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        StoreAction action = (StoreAction) arg;
        switch (action.type) {
            case "@CLIENT_SET_AVAILABLE_GAMES":
                this.setAvailableGamesReaction(action);
                break;
            case "@CLIENT_SET_CONNECTION_ACTIVE":
                this.setConnectionActiveReaction(action);
                break;
            case "@CLIENT_START_GAME":
                this.startGameReaction();
                break;
            case "@CLIENT_MOVE_TO_SECTOR":
                this.moveToSectorReaction(action);
                break;
            case "@CLIENT_SET_DRAWN_SECTOR_OBJECT_CARD":
                this.setDrawnSectorObjectCardReaction(action);
                break;
            case "@CLIENT_USE_OBJECT_CARD":
                this.useObjectCardReaction(action);
                break;
            case "@CLIENT_ASK_FOR_SECTOR_TO_LIGHT":
                this.askForSectorToLightReaction(action);
                break;
            case "@CLIENT_ASK_FOR_SECTOR_TO_ATTACK":
                this.askForSectorToAttackReaction(action);
                break;
            case "@CLIENT_TELEPORT_TO_STARTING_SECTOR":
                this.teleportToStartingSectorReaction();
                break;
            case "@CLIENT_DISCARD_OBJECT_CARD":
                this.discardObjectCardReaction(action);
                break;
            case "@CLIENT_END_TURN":
                this.endTurnReaction(action);
                break;
            case "@CLIENT_START_TURN":
                this.startTurnReaction();
                break;
            case "@CLIENT_PUBLISH_CHAT_MSG": {
                this.publishChatMessageReaction(action);
                break;
            }
            case "@CLIENT_SET_PLAYER_STATE": {
                this.setPlayerStateReaction(action);
                break;
            }
            case "@CLIENT_SET_WINNERS": {
                this.setWinnersReaction(action);
                break;
            }
            case "@CLIENT_STARTABLE_GAME":
                this.guiGameList.startableGame();
                break;
        }
    }
    private void setWinnersReaction(StoreAction action) {
        ClientSetWinnersAction castedAction = (ClientSetWinnersAction) action;
        if (castedAction.humansHaveWon && castedAction.aliensHaveWon){
            this.guiGamePane.setStateMessage("Aliens and Humans have won");
        }
        else if (castedAction.humansHaveWon){
            this.guiGamePane.setStateMessage("Humans have won");
        }
        else {
            this.guiGamePane.setStateMessage("Aliens have won");
        }
        this.guiGamePane.changeCardMenu(MenuType.EMPTY);
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
        this.guiGamePane.showEndTurnButton(false);
    }

    private void setPlayerStateReaction(StoreAction action) {
        ClientSetPlayerState castedAction = (ClientSetPlayerState) action;
        if (castedAction.playerState.equals(PlayerState.ESCAPED)){
            this.guiGamePane.setStateMessage("You've ESCAPED!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
            this.guiGamePane.showEndTurnButton(false);
        }
        else if (castedAction.playerState.equals(PlayerState.DEAD)){
            this.guiGamePane.setStateMessage("You're DEAD!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
            this.guiGamePane.showEndTurnButton(false);
        }
    }
    private void publishChatMessageReaction(StoreAction action) {
        ClientSetCurrentChatMessage castedAction = (ClientSetCurrentChatMessage) action;
        this.guiGamePane.appendMsg(castedAction.message);
    }

    private void startTurnReaction() {
        Player player = this.clientStore.getState().player;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (player.playerToken.playerType.equals(PlayerType.ALIEN)){
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.ALIEN_INITIAL);
        }
        else {
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.HUMAN_INITIAL);
            this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
        }
    }

    private void endTurnReaction(StoreAction action) {
        ClientEndTurnAction castedAction = (ClientEndTurnAction) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isActionServerValidated){
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.showEndTurnButton(false);
        }
    }

    private void discardObjectCardReaction(StoreAction action) {
        ClientDiscardObjectCardAction castedAction = (ClientDiscardObjectCardAction) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isActionServerValidated){
            this.guiGamePane.refreshCardPanel(this.clientStore.getState().player.privateDeck.getContent());
        }
    }

    private void teleportToStartingSectorReaction() {
        Player player = this.clientStore.getState().player;
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(player.currentSector.getCoordinate(),"Y",player.name);
    }

    private void askForSectorToAttackReaction(StoreAction action) {
        ClientAskAttackAction castedAction = (ClientAskAttackAction) action;
        if (castedAction.toBeAsked){
            this.guiGamePane.setStateMessage("Indicate the sector to attack");
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.ATTACK_MENU);
        }
    }

    private void askForSectorToLightReaction(StoreAction action) {
        ClientAskSectorToLightAction castedAction = (ClientAskSectorToLightAction) action;
        if (castedAction.toBeAsked){
            this.guiGamePane.setStateMessage("Indicate the sector to light");
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.LIGHT_MENU);
        }
    }

    private void useObjectCardReaction(StoreAction action) {
        ClientUseObjectCard castedAction = (ClientUseObjectCard) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isServerValidated){
            if (castedAction.objectCard instanceof DefenseObjectCard){
                this.guiGamePane.setStateMessage("You have succesfully defended from an attack");
            }
            if (castedAction.objectCard instanceof LightsObjectCard){
                for (Sector sector : this.clientStore.getState().currentReqRespNotification.getLightedSectors()){
                    this.guiGamePane.getMapPane().lightSector(sector.getCoordinate(),"A","Here there's an alien");
                }
            }
            this.guiGamePane.refreshCardPanel(this.clientStore.getState().player.privateDeck.getContent());
        }
    }


    private void moveToSectorReaction(StoreAction action) {
        ClientMoveToSectorAction castedAction = (ClientMoveToSectorAction) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isServerValidated){
            this.updatePosition();
            this.guiGamePane.showEndTurnButton(true);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        }
    }

    private void setDrawnSectorObjectCardReaction(StoreAction action) {
        ClientSetDrawnSectorObjectCard castedAction = (ClientSetDrawnSectorObjectCard) action;
        CardSplashScreen cardSplashScreen = new CardSplashScreen(this.mainFrame);
        cardSplashScreen.showCards(castedAction.drawnSectorCard, castedAction.drawnObjectCard);
        PrivateDeck clientPrivateDeck = this.clientStore.getState().player.privateDeck;

        if (!castedAction.isActionServerValidated){
            this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
            return;
        }

        if (castedAction.drawnSectorCard instanceof GlobalNoiseSectorCard){
            this.guiGamePane
                    .setStateMessage("Select a sector for the global noise card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.NOISE_MENU);
            this.guiGamePane.showEndTurnButton(false);
        }
        else if ( castedAction.drawnSectorCard instanceof LocalNoiseSectorCard){
            this.guiGamePane.setStateMessage("You will make noise in your sector");
            if (clientPrivateDeck.getSize() > 3){
                this.manyCardHandler();
            }
            else {
                this.guiGamePane.showEndTurnButton(true);
            }
        }
        else if ( castedAction.drawnSectorCard instanceof SilenceSectorCard){
            this.guiGamePane.setStateMessage("You will make no noise");
            if (clientPrivateDeck.getSize() > 3){
                this.manyCardHandler();
            }
            else {
                this.guiGamePane.showEndTurnButton(true);
            }
        }
        else {
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            if (clientPrivateDeck.getSize() > 3){
                this.manyCardHandler();
            }
            else {
                this.guiGamePane.showEndTurnButton(true);
            }
        }
        if (castedAction.drawnObjectCard != null){
            this.guiGamePane.addCardToPanel(castedAction.drawnObjectCard);
        }

    }

    /**
     * Switches to the {@link client.GUIGamePane} view from the {@link client.GUIGameList} and
     * acts on this view in response to the starting of the game
     */
    private void startGameReaction() {
        Player player = this.clientStore.getState().player;
        String characterInfoMsg = "";
        if (player.playerToken.playerType.equals(PlayerType.ALIEN)){
            characterInfoMsg = player.name + " | ALIEN";
            this.guiGamePane.setSectorMenu(MenuType.ALIEN_INITIAL);
        }
        else {
            characterInfoMsg = player.name + " | HUMAN";
            this.guiGamePane.setSectorMenu(MenuType.HUMAN_INITIAL);
        }
        this.mainFrame.remove(this.guiGameList);
        this.guiGamePane.load("dasd");
        this.mainFrame.add(this.guiGamePane);
        this.guiGamePane.setVisible(true);
        this.guiGamePane.setInfoMsg(characterInfoMsg);
        this.guiGamePane.getMapPane().lightSector(
                player.currentSector.getCoordinate(), "Y", player.name);
        this.mainFrame.validate();
        this.mainFrame.repaint();
    }

    /**
     * Make the different gui components of the app signal the absence of a connection
     * with the server
     * @param action The {@link server_store.StoreAction} that has caused this behavior
     */
    private void setConnectionActiveReaction(StoreAction action) {
        ClientSetConnectionActiveAction castedAction = (ClientSetConnectionActiveAction) action;
        this.guiInitialWindow.alertConnectionProblem(castedAction.isConnectionActive);
        this.guiGameList.alertConnectionProblem(castedAction.isConnectionActive);
        this.guiGamePane.alertConnectionProblem(castedAction.isConnectionActive);
    }

    /**
     * Switches to the {@link client.GUIGameList} view from the {@link client.GUInitialWindow}
     * @param action The {@link server_store.StoreAction} that has caused this behavior
     */
    private void setAvailableGamesReaction(StoreAction action) {
        ClientSetAvailableGamesAction castedAction = (ClientSetAvailableGamesAction) action;
        this.mainFrame.remove(this.guiInitialWindow);
        this.guiGameList.load();
        this.guiGameList.setGameListContent(castedAction.availableGames);
        mainFrame.getContentPane().add(this.guiGameList);
        this.guiGameList.setVisible(true);
        this.mainFrame.validate();
        this.mainFrame.repaint();
    }

}

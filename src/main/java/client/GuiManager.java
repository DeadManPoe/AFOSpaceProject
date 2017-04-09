package client;

import client_store.ClientStore;
import client_store_actions.*;
import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Player;
import server_store.StoreAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by giorgiopea on 27/03/17.
 * A manager that orchestrates all the gui components of this application.
 * It observes the state of this application and implements view business logic in response to state changes.
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

    /**
     * Inits all the gui components of the application
     */
    public void initGuiComponents() {
        this.mainFrame = new JFrame("Escape from aliens in the outer space");
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setLocationByPlatform(true);
        this.mainFrame.pack();
        this.mainFrame.setExtendedState(this.mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.guiInitialWindow = new GUInitialWindow();
        this.guiGameList = new GUIGameList();
        this.guiGamePane = new GUIGamePane();
        this.guiGamePane.setLayout(new GridBagLayout());
        this.guiInitialWindow.load();
        this.mainFrame.setVisible(true);
        mainFrame.getContentPane().add(this.guiInitialWindow);
        this.guiInitialWindow.setVisible(true);
    }

    /**
     * Shows on the {@link GUIMap} the new position of the client.
     */
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


    /**
     * Changes the card/map menus as a consequence of the excessive number of object
     * card in the client's private deck, so that the client is forced to discard or use one of those.
     */
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

    /**
     * A reaction to the fact that either humans or aliens or both have won the game.
     * This reaction includes displaying some messages, updating card/map menus and disabling the 'end turn' button.
     *
     * @param action The action that has triggered this reaction.
     */
    private void setWinnersReaction(StoreAction action) {
        ClientSetWinnersAction castedAction = (ClientSetWinnersAction) action;
        if (castedAction.humansHaveWon && castedAction.aliensHaveWon) {
            this.guiGamePane.setStateMessage("Aliens and Humans have won");
        } else if (castedAction.humansHaveWon) {
            this.guiGamePane.setStateMessage("Humans have won");
        } else {
            this.guiGamePane.setStateMessage("Aliens have won");
        }
        this.guiGamePane.changeCardMenu(MenuType.EMPTY);
        this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
        this.guiGamePane.showEndTurnButton(false);
    }

    /**
     * A reaction to some changes in the game state of the client.
     * This reaction includes updating card/map menus, refreshing the object cards panel and displaying some messages.
     *
     * @param action The action that has triggered this reaction.
     */
    private void setPlayerStateReaction(StoreAction action) {
        ClientSetPlayerState castedAction = (ClientSetPlayerState) action;
        if (castedAction.playerState.equals(PlayerState.ESCAPED)) {
            this.guiGamePane.setStateMessage("You've ESCAPED!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
            this.guiGamePane.showEndTurnButton(false);
        } else if (castedAction.playerState.equals(PlayerState.DEAD)) {
            this.guiGamePane.setStateMessage("You're DEAD!");
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.refreshCardPanel(new ArrayList<ObjectCard>());
            this.guiGamePane.showEndTurnButton(false);
        }
    }

    /**
     * A reaction to publishing a message in the game chat by the client.
     * This reaction includes showing the actual message in the chat panel .
     *
     * @param action The action that has triggered this reaction.
     */
    private void publishChatMessageReaction(StoreAction action) {
        ClientSetCurrentChatMessage castedAction = (ClientSetCurrentChatMessage) action;
        this.guiGamePane.appendMsg(castedAction.message);
    }

    /**
     * A reaction to starting his turn by the client.
     * This reaction includes setting/resetting of cards/map menus.
     */
    private void startTurnReaction() {
        Player player = this.clientStore.getState().player;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (player.playerToken.playerType.equals(PlayerType.ALIEN)) {
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.ALIEN_INITIAL);
        } else {
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.HUMAN_INITIAL);
            this.guiGamePane.changeCardMenu(MenuType.HUMAN_USE_MENU);
        }
    }

    /**
     * A reaction to ending his turn by the client.
     * This reaction includes setting/resetting cards/map menus and disabling the end turn button.
     *
     * @param action The action that has triggered this reaction.
     */
    private void endTurnReaction(StoreAction action) {
        ClientEndTurnAction castedAction = (ClientEndTurnAction) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isActionServerValidated) {
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            this.guiGamePane.changeCardMenu(MenuType.EMPTY);
            this.guiGamePane.showEndTurnButton(false);
        }
    }

    /**
     * A reaction to discarding object card by the client.
     * This reaction includes refreshing the object card panel.
     *
     * @param action The action that has triggered this reaction.
     */
    private void discardObjectCardReaction(StoreAction action) {
        ClientDiscardObjectCardAction castedAction = (ClientDiscardObjectCardAction) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isActionServerValidated) {
            this.guiGamePane.refreshCardPanel(this.clientStore.getState().player.privateDeck.getContent());
        }
    }

    /**
     * A reaction to using a teleport object card by the client.
     * This reaction includes moving the client to his original sector.
     */
    private void teleportToStartingSectorReaction() {
        Player player = this.clientStore.getState().player;
        this.guiGamePane.getMapPane().delightAllSectors();
        this.guiGamePane.getMapPane().lightSector(player.currentSector.getCoordinate(), "Y", player.name);
    }

    /**
     * A reaction to using an attack object card by the client.
     * This reaction includes asking to the client what sector he wants to attack and
     * setting/resetting cards/map menus.
     *
     * @param action The action that has triggered this reaction.
     */
    private void askForSectorToAttackReaction(StoreAction action) {
        ClientAskAttackAction castedAction = (ClientAskAttackAction) action;
        if (castedAction.toBeAsked) {
            this.guiGamePane.setStateMessage("Indicate the sector to attack");
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.ATTACK_MENU);
        }
    }

    /**
     * A reaction to using a lights sector card by the client. This
     * reaction includes asking to the client what sector he wants to light and includes setting/resetting
     * cards/map menus.
     *
     * @param action The action that has triggered this reaction.
     */
    private void askForSectorToLightReaction(StoreAction action) {
        ClientAskSectorToLightAction castedAction = (ClientAskSectorToLightAction) action;
        if (castedAction.toBeAsked) {
            this.guiGamePane.setStateMessage("Indicate the sector to light");
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.LIGHT_MENU);
        }
    }

    /**
     * A reaction to using an object card by the client.
     * This reaction includes updating the object cards shown in the card panel and includes displaying various messages.
     *
     * @param action The action that has triggered this reaction.
     */
    private void useObjectCardReaction(StoreAction action) {
        ClientUseObjectCard castedAction = (ClientUseObjectCard) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isServerValidated) {
            if (castedAction.objectCard instanceof DefenseObjectCard) {
                this.guiGamePane.setStateMessage("You have succesfully defended from an attack");
            }
            if (castedAction.objectCard instanceof LightsObjectCard) {
                for (Sector sector : this.clientStore.getState().currentReqRespNotification.getLightedSectors()) {
                    this.guiGamePane.getMapPane().lightSector(sector.getCoordinate(), "A", "Here there's an alien");
                }
            }
            this.guiGamePane.refreshCardPanel(this.clientStore.getState().player.privateDeck.getContent());
        }
    }

    /**
     * A reaction to moving to a sector by the client. This reaction includes
     * updating gui components so that the new position of the client is shown on the map, and
     * includes setting/resetting map/cards menus
     *
     * @param action The action that has triggered this reaction.
     */
    private void moveToSectorReaction(StoreAction action) {
        ClientMoveToSectorAction castedAction = (ClientMoveToSectorAction) action;
        this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
        if (castedAction.isServerValidated) {
            this.updatePosition();
            this.guiGamePane.showEndTurnButton(true);
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
        }
    }

    /**
     * A reaction to object/sector cards by the client. This reaction includes
     * setting/resetting map/card menus and verifying if there are too many object cards and
     * so one must be discarded
     *
     * @param action The action that has triggered this reaction.
     */
    private void setDrawnSectorObjectCardReaction(StoreAction action) {
        ClientSetDrawnSectorObjectCard castedAction = (ClientSetDrawnSectorObjectCard) action;
        CardSplashScreen cardSplashScreen = new CardSplashScreen(this.mainFrame);
        cardSplashScreen.showCards(castedAction.drawnSectorCard, castedAction.drawnObjectCard);
        PrivateDeck clientPrivateDeck = this.clientStore.getState().player.privateDeck;

        //The action has not been validated by the server so only a message is shown
        if (!castedAction.isActionServerValidated) {
            this.guiGamePane.setStateMessage(this.clientStore.getState().currentReqRespNotification.getMessage());
            return;
        }

        if (castedAction.drawnSectorCard instanceof GlobalNoiseSectorCard) {
            this.guiGamePane
                    .setStateMessage("Select a sector for the global noise card");
            this.guiGamePane.getMapPane().changeMapMenu(
                    MenuType.NOISE_MENU);
            this.guiGamePane.showEndTurnButton(false);
        } else if (castedAction.drawnSectorCard instanceof LocalNoiseSectorCard) {
            this.guiGamePane.setStateMessage("You will make noise in your sector");
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler();
            } else {
                this.guiGamePane.showEndTurnButton(true);
            }
        } else if (castedAction.drawnSectorCard instanceof SilenceSectorCard) {
            this.guiGamePane.setStateMessage("You will make no noise");
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler();
            } else {
                this.guiGamePane.showEndTurnButton(true);
            }
        } else {
            this.guiGamePane.getMapPane().changeMapMenu(MenuType.EMPTY);
            if (clientPrivateDeck.getSize() > 3) {
                this.manyCardHandler();
            } else {
                this.guiGamePane.showEndTurnButton(true);
            }
        }
        if (castedAction.drawnObjectCard != null) {
            this.guiGamePane.addCardToPanel(castedAction.drawnObjectCard);
        }

    }

    /**
     * Switches to the {@link client.GUIGamePane} view from the {@link client.GUIGameList} and
     * acts on this view in response to the starting of the game
     */
    private void startGameReaction() {
        Player player = this.clientStore.getState().player;
        String characterInfoMsg;
        if (player.playerToken.playerType.equals(PlayerType.ALIEN)) {
            characterInfoMsg = player.name + " | ALIEN";
            this.guiGamePane.setSectorMenu(MenuType.ALIEN_INITIAL);
        } else {
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
     * Makes the different gui components of the app signal the absence of a connection
     * with the server.
     *
     * @param action The {@link server_store.StoreAction} that has caused this behavior.
     */
    private void setConnectionActiveReaction(StoreAction action) {
        ClientSetConnectionActiveAction castedAction = (ClientSetConnectionActiveAction) action;
        this.guiInitialWindow.alertConnectionProblem(!castedAction.isConnectionActive);
        this.guiGameList.alertConnectionProblem(!castedAction.isConnectionActive);
        this.guiGamePane.alertConnectionProblem(!castedAction.isConnectionActive);
    }

    /**
     * Switches to the {@link client.GUIGameList} view from the {@link client.GUInitialWindow}.
     *
     * @param action The {@link server_store.StoreAction} that has caused this behavior.
     */
    private void setAvailableGamesReaction(StoreAction action) {
        ClientSetAvailableGamesAction castedAction = (ClientSetAvailableGamesAction) action;
        this.guiGameList.setGameListContent(castedAction.availableGames);
        if (!this.guiGameList.isVisible()){
            this.guiInitialWindow.setVisible(false);
            this.guiGameList
                    .setLayout(new BoxLayout(this.guiGameList, BoxLayout.Y_AXIS));
            this.guiGameList.setBackground(Color.BLACK);
            this.guiGameList.load();
            mainFrame.getContentPane().add(this.guiGameList);
            this.guiGameList.setVisible(true);
            this.mainFrame.repaint();
        }


    }

}

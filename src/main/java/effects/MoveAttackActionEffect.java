package effects;

import common.*;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Game;
import server_store.Player;
import server_store.ServerState;
import server_store.StoreAction;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the effect of moving a player and attacking the sector in which
 * the player has arrived. It is important that after the move the current
 * player doesn't draw any cards
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 * @see ActionEffect
 * @see MoveAttackAction
 */
public class MoveAttackActionEffect extends ActionEffect {

    public static boolean executeEffect(Game game, StoreAction action) {
        MoveAttackAction castedAction = (MoveAttackAction) action;
        RRClientNotification clientNotification = new RRClientNotification();
        PSClientNotification psNotification = new PSClientNotification();

        Sector sourceSector = game.currentPlayer.currentSector;
        Sector targetSector = game.gameMap.getSectorByCoords(
                castedAction.payload.getCoordinate());
        server_store.Player currentPlayer = game.currentPlayer;
        String rrMessage = "";
        String psMessage = "";

        if (!sourceSector.equals(castedAction.payload)) {
            if (game.gameMap.checkSectorAdiacency(sourceSector, targetSector,
                    currentPlayer.speed, 0, currentPlayer.playerType,
                    sourceSector, currentPlayer.isAdrenalined)) {

                // For each player contained in the target sector
                for (Player player : targetSector.getPlayers()) {
                    PrivateDeck playerPrivateDeck = player.privateDeck;
                    ArrayList<ObjectCard> privateDeckContent = new ArrayList<ObjectCard>(
                            playerPrivateDeck.getContent());
                    Iterator<ObjectCard> iterator = privateDeckContent
                            .iterator();
                    boolean defenseFound = false;
                    if (player.playerType == PlayerType.HUMAN) {
                        while (iterator.hasNext()) {
                            ObjectCard objectCard = iterator.next();
                            if (objectCard instanceof DefenseObjectCard) {
                                playerPrivateDeck.removeCard(objectCard);
                                defenseFound = true;
                            }
                        }
                    }
                    // If no defense card has been found for p, then p is dead
                    if (!defenseFound) {
                        targetSector.removePlayer(player);
                        player.playerState = PlayerState.DEAD;
                        player.currentSector = null;
                        // Notify the rest of the players
                        psNotification.addDeadPlayers(player.playerToken);
                        rrMessage += "You have attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.name + " is dead.";
                        psMessage += "[GLOBAL MESSAGE]: "
                                + currentPlayer.name
                                + " has attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.name + " is dead.";
                    } else {
                        if (game.currentPlayer.playerType == PlayerType.ALIEN) {
                            game.currentPlayer.speed = 3;
                        }
                        // Otherwise p has been attacked
                        psNotification.addAttackedPlayers(player.playerToken
                        );
                        rrMessage += "You have attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.name
                                + " has defended.";

                        psMessage += "[GLOBAL MESSAGE]: "
                                + currentPlayer.name
                                + " has attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.name
                                + " has defended.";
                    }
                }
                if (rrMessage.equals("")) {
                    rrMessage += "You have attacked sector "
                            + targetSector.getCoordinate().toString()
                            + " but it contained no players.";
                    psMessage += "[GLOBAL MESSAGE]: " + currentPlayer.name
                            + " has attacked sector "
                            + targetSector.getCoordinate().toString()
                            + " but it contained no players.";
                }
                clientNotification.setMessage(rrMessage);
                psNotification.setMessage(psMessage);
                game.lastRRclientNotification = clientNotification;
                game.lastPSclientNotification = psNotification;
                // Move the player that has attacked to the target sector
                sourceSector.removePlayer(currentPlayer);
                currentPlayer.currentSector = targetSector;
                targetSector.addPlayer(currentPlayer);
                game.lastAction = action;
                currentPlayer.hasMoved = true;
                return true;
            }
        }

        return false;
    }
}

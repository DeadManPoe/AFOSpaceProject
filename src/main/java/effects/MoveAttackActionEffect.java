package effects;

import common.*;
import common.PlayerState;
import common.PlayerType;
import common.SectorLegality;
import server.Game;
import common.Player;
import server_store.StoreAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private static boolean verifyMoveLegality(Sector source, Sector target, PlayerType playerType){
        if (source.equals(target)){
            return false;
        }
        if (playerType.equals(PlayerType.HUMAN) &&
                (target.getSectorLegality().equals(SectorLegality.NONE))){
            return false;
        }
        else if (playerType.equals(PlayerType.ALIEN) &&
                (target.getSectorLegality().equals(SectorLegality.NONE)
                        || target.getSectorLegality().equals(SectorLegality.HUMAN))){
            return false;
        }
        return true;

    }
    public static boolean executeEffect(Game game, StoreAction action) {
        MoveAttackAction castedAction = (MoveAttackAction) action;

        Sector sourceSector = game.getCurrentPlayer().getCurrentSector();
        Sector targetSector = game.getGameMap().getSectorByCoords(
                castedAction.getTargetSector().getCoordinate());
        Player currentPlayer = game.getCurrentPlayer();
        String rrMessage = "";
        String psMessage = "";
        List<Player> deadPlayers = new ArrayList<>();
        int adrenalineBooster = 0;
        if (currentPlayer.isAdrenalined()){
            adrenalineBooster++;
        }

        if (!sourceSector.equals(castedAction.getTargetSector())) {
            if (game.getGameMap().checkSectorAdiacency(sourceSector,targetSector,currentPlayer.getSpeed()+adrenalineBooster,currentPlayer.isAdrenalined())
                    && verifyMoveLegality(sourceSector,targetSector,currentPlayer.getPlayerToken().getPlayerType())) {

                // For each player contained in the target sector
                for (Player player : targetSector.getPlayers()) {
                    PrivateDeck playerPrivateDeck = player.getPrivateDeck();
                    ArrayList<ObjectCard> privateDeckContent = new ArrayList<ObjectCard>(
                            playerPrivateDeck.getContent());
                    Iterator<ObjectCard> iterator = privateDeckContent
                            .iterator();
                    boolean defenseFound = false;
                    if (player.getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)) {
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
                        deadPlayers.add(player);
                        player.setPlayerState(PlayerState.DEAD);
                        player.setCurrentSector(null);
                        // Notify the rest of the players
                        game.getLastPSclientNotification().addDeadPlayers(player.getPlayerToken());
                        rrMessage += "You have attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.getName() + " is dead.";
                        psMessage += "[GLOBAL MESSAGE]: "
                                + currentPlayer.getName()
                                + " has attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.getName() + " is dead.";
                    } else {
                        if (game.getCurrentPlayer().getPlayerToken().getPlayerType().equals(PlayerType.ALIEN) ){
                            game.getCurrentPlayer().setSpeed(3);
                        }
                        // Otherwise p has been attacked
                        game.getLastPSclientNotification().addAttackedPlayers(player.getPlayerToken()
                        );
                        rrMessage += "You have attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.getName()
                                + " has defended.";

                        psMessage += "[GLOBAL MESSAGE]: "
                                + currentPlayer.getName()
                                + " has attacked sector "
                                + targetSector.getCoordinate().toString()
                                + " and so " + player.getName()
                                + " has defended.";
                    }
                }
                if (rrMessage.equals("")) {
                    rrMessage += "You have attacked sector "
                            + targetSector.getCoordinate().toString()
                            + " but it contained no players.";
                    psMessage += "[GLOBAL MESSAGE]: " + currentPlayer.getName()
                            + " has attacked sector "
                            + targetSector.getCoordinate().toString()
                            + " but it contained no players.";
                }
                game.getLastRRclientNotification().setMessage(rrMessage);
                game.getLastPSclientNotification().setMessage(psMessage);
                for (Player player : deadPlayers){
                    targetSector.removePlayer(player);
                }
                // Move the player that has attacked to the target sector
                sourceSector.removePlayer(currentPlayer);
                currentPlayer.setCurrentSector(targetSector);
                targetSector.addPlayer(currentPlayer);
                game.setLastAction(action);
                currentPlayer.setHasMoved(true);
                return true;
            }
        }

        return false;
    }
}

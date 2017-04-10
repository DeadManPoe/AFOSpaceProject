package effects;

import common.*;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.PlayerType;
import it.polimi.ingsw.cg_19.SectorLegality;
import server_store.Game;
import server_store.Player;
import server_store.ServerState;
import server_store.StoreAction;

/**
 * Represents the effect of the moving a player
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 * @see ActionEffect
 * @see MoveAction
 */
public class MoveActionEffect extends ActionEffect {


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
        MoveAction moveAction = (MoveAction) action;
        game.lastAction = moveAction;
        // Retrieve a reference of the map
        GameMap map = game.gameMap;
        Player currentPlayer = game.currentPlayer;
        int adrenalineBooster = 0;
        if (currentPlayer.isAdrenalined){
            adrenalineBooster++;
        }
        // Checks the source != target
        if (!currentPlayer.currentSector.equals(moveAction.payload)) {
            // Retrieve the "true" reference of source and target
            Sector sourceSector = map.getSectorByCoords(currentPlayer
                    .currentSector.getCoordinate());
            Sector targetSector = map.getSectorByCoords(moveAction.payload
                    .getCoordinate());
            // Checks that source and target are adjacent according to the speed
            // of the player
            if (map.checkSectorAdiacency(sourceSector, targetSector,
                    currentPlayer.speed+adrenalineBooster,currentPlayer.isAdrenalined)
                    && verifyMoveLegality(sourceSector,targetSector,currentPlayer.playerToken.playerType) ) {
                // This two lines implements the move
                sourceSector.removePlayer(currentPlayer);
                currentPlayer.currentSector = targetSector;
                targetSector.addPlayer(currentPlayer);
                game.lastRRclientNotification.setMessage("You have moved to sector "
                        + targetSector.getCoordinate().toString());
                game.lastPSclientNotification.setMessage("[GLOBAL MESSAGE]: "
                        + currentPlayer.name + " has moved.");
                // If the target sector is a dangerous sector continue the
                // execution
                // of the action
                if (targetSector.getSectorType() == SectorType.DANGEROUS
                        && !currentPlayer.isSedated) {
                    DrawSectorCardEffect.executeEffect(game, new DrawSectorCardAction());
                } else if (targetSector.getSectorType() == SectorType.OPEN_RESCUE) {
                    DrawRescueCardEffect.executeEffect(game);
                }
                game.currentPlayer.hasMoved = true;
                return true;
            }

        }
        return false;
    }

}

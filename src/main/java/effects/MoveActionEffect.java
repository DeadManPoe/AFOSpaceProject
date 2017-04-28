package effects;

import common.*;
import it.polimi.ingsw.cg_19.*;

/**
 * Represents the effect of the moving a player
 * 
 * @see ActionEffect
 * @see MoveAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
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
	public static boolean executeEffect(Game game,
										RRClientNotification rrClientNotification,
										PSClientNotification psClientNotification, Action action) {
		MoveAction moveAction = (MoveAction) action;
		// Retrieve a reference of the map
		GameMap map = game.getMap();
		Player currentPlayer = game.getCurrentPlayer();
		int adrenalineBooster = 0;
		if (currentPlayer.isAdrenalined()){
			adrenalineBooster++;
		}
		// Checks the source != target
		if (!currentPlayer.getCurrentSector().equals(moveAction.getTarget())) {
			// Retrieve the "true" reference of source and target
			Sector sourceSector = map.getSectorByCoords(currentPlayer
					.getCurrentSector().getCoordinate());
			Sector targetSector = map.getSectorByCoords(moveAction.getTarget()
					.getCoordinate());
			// Checks that source and target are adjacent according to the speed
			// of the player
			if (map.checkSectorAdiacency(sourceSector, targetSector,
					currentPlayer.getSpeed()+adrenalineBooster,currentPlayer.isAdrenalined())
					&& verifyMoveLegality(sourceSector,targetSector,currentPlayer.getPlayerToken().getPlayerType()) ) {
				// This two lines implements the move
				sourceSector.removePlayer(currentPlayer);
				currentPlayer.setCurrentSector(targetSector);
				targetSector.addPlayer(currentPlayer);
				rrClientNotification.setMessage("You have moved to sector "
						+ targetSector.getCoordinate().toString());
				psClientNotification.setMessage("[GLOBAL MESSAGE]: "
						+ currentPlayer.getName() + " has moved.");
				// If the target sector is a dangerous sector continue the
				// execution
				// of the action
				if (targetSector.getSectorType() == SectorType.DANGEROUS
						&& !currentPlayer.isSedated()) {
					DrawSectorCardEffect.executeEffect(game, rrClientNotification, psClientNotification, new DrawSectorCardAction());
				} else if (targetSector.getSectorType() == SectorType.OPEN_RESCUE) {
					DrawRescueCardEffect.executeEffect(game,rrClientNotification,psClientNotification);
				}
				else {
					game.setLastAction(action);
				}
				currentPlayer.setHasMoved(true);
				return true;
			}

		}
		return false;
	}
}

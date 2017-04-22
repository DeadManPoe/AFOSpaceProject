package effects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import common.*;
import it.polimi.ingsw.cg_19.*;

/**
 * Represents the effect of moving a player and attacking the sector in which
 * the player has arrived. It is important that after the move the current
 * player doesn't draw any cards
 * 
 * @see ActionEffect
 * @see MoveAttackAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class MoveAttackActionEffect extends ActionEffect {

	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, Action action) {
		MoveAttackAction castedAction = (MoveAttackAction) action;
		Player currentPlayer = game.getCurrentPlayer();
		Sector sourceSector = currentPlayer.getCurrentSector();
		Sector targetSector = game.getMap().getSectorByCoords(
				castedAction.getTarget().getCoordinate());

		String rrMessage = "";
		String psMessage = "";
		List<Player> deadPlayers = new ArrayList<>();

		if (!sourceSector.equals(castedAction.getTarget())) {
			if (game.getMap().checkSectorAdiacency(sourceSector,targetSector,currentPlayer.getSpeed(),currentPlayer.isAdrenalined())
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
						psNotification.addDeadPlayers(player.getPlayerToken());
						rrMessage += "You have attacked sector "
								+ targetSector.getCoordinate().toString()
								+ " and so " + player.getName() + " is dead.";
						psMessage += "[GLOBAL MESSAGE]: "
								+ currentPlayer.getName()
								+ " has attacked sector "
								+ targetSector.getCoordinate().toString()
								+ " and so " + player.getName() + " is dead.";
					} else {
						if (currentPlayer.getPlayerToken().getPlayerType().equals(PlayerType.ALIEN)) {
							currentPlayer.setSpeed(3);
						}
						// Otherwise p has been attacked
						psNotification.addAttackedPlayers(player.getPlayerToken()
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
				rrNotification.setMessage(rrMessage);
				psNotification.setMessage(psMessage);
				for (Player player : deadPlayers){
					targetSector.removePlayer(player);
				}
				// Move the player that has attacked to the target sector
				sourceSector.removePlayer(currentPlayer);
				currentPlayer.setCurrentSector(targetSector);
				targetSector.addPlayer(currentPlayer);
				game.setLastAction( action);
				currentPlayer.setHasMoved(true);
				return true;
			}
		}

		return false;
	}
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

}

package effects;

import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerState;
import it.polimi.ingsw.cg_19.PlayerType;
import common.DefenseObjectCard;
import common.MoveAttackAction;
import common.ObjectCard;
import common.PSClientNotification;
import common.PrivateDeck;
import common.RRClientNotification;
import common.Sector;

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
	/**
	 * Constructs an effect of moving a player and attacking the sector in which
	 * the player has arrived. This effect is constructed from a
	 * {@link common.MoveAction}
	 * 
	 * @param moveAttackAction
	 *            the {@link common.MoveAction} that needs to be enriched with
	 *            its effect
	 */
	public MoveAttackActionEffect(MoveAttackAction moveAttackAction) {
		super(moveAttackAction);
	}

	/**
	 * Constructs an effect of moving a player and attacking the sector in which
	 * the player has arrived. This effect is constructed from a <<<<<<< HEAD
	 * {@link common.MoveAction} that is null ======= {@link common.MoveAction}
	 * that is null. This constructor is used only for test purposes >>>>>>>
	 * branch 'master' of https://Sessa93@bitbucket.org/Sessa93/progettois.git
	 */
	public MoveAttackActionEffect() {
		super(null);
	}

	/**
	 * @see ActionEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification) {
		MoveAttackAction moveAttackAction = (MoveAttackAction) action;
		Sector sourceSector = game.currentPlayer.currentSector;
		Sector targetSector = game.gameMap.getSectorByCoords(
				moveAttackAction.getTarget().getCoordinate());
		server_store.Player currentPlayer = game.currentPlayer;
		String rrMessage = "";
		String psMessage = "";

		if (!sourceSector.equals(moveAttackAction.getTarget())) {
			if (game.gameMap.checkSectorAdiacency(sourceSector, targetSector,
					currentPlayer.speed, 0, currentPlayer.playerType,
					sourceSector, currentPlayer.isAdrenalined)) {
				
				// For each player contained in the target sector
				for (server_store.Player player : targetSector.getPlayers()) {
					/*
					 * Checks if the player p has an defense card, if so then p
					 * is safe and we removes the defense card from the private
					 * deck of p
					 */
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
						if(game.currentPlayer.playerType == PlayerType.ALIEN) {
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
				rrNotification.setMessage(rrMessage);
				psNotification.setMessage(psMessage);
				// Move the player that has attacked to the target sector
				sourceSector.removePlayer(currentPlayer);
				currentPlayer.currentSector= targetSector;
				targetSector.addPlayer(currentPlayer);

				game.lastAction = moveAttackAction;
				currentPlayer.hasMoved = true;
				return true;
			}
		}

		return false;
	}

}

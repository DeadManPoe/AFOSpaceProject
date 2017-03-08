package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;

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
	/**
	 * Constructs an effect of moving a player. This effect is constructed from
	 * a {@link common.MoveAction}
	 * 
	 * @param moveAction
	 *            the {@link common.MoveAction} that needs to be enriched with
	 *            its effect
	 */
	public MoveActionEffect(MoveAction moveAction) {
		super(moveAction);
	}

	/**
	 * Constructs an effect of moving a player. This effect is constructed from
	 * a {@link common.MoveAction} that is null. This constructor is used only
	 * for test purposes
	 * 
	 */
	public MoveActionEffect() {
		this(null);
	}

	/**
	 * @see ActionEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		MoveAction moveAction = (MoveAction) action;
		game.setLastAction(action);
		// Retrieve a reference of the map
		GameMap map = game.getMap();
		Player currentPlayer = game.getCurrentPlayer();
		// Checks the source != target
		if (!currentPlayer.getSector().equals(moveAction.getTarget())) {
			// Retrieve the "true" reference of source and target
			Sector sourceSector = map.getSectorByCoords(currentPlayer
					.getSector().getCoordinate());
			Sector targetSector = map.getSectorByCoords(moveAction.getTarget()
					.getCoordinate());
			// Checks that source and target are adjacent according to the speed
			// of the player
			if (map.checkSectorAdiacency(sourceSector, targetSector,
					currentPlayer.getSpeed(), 0, currentPlayer.getPlayerType(),
					sourceSector, currentPlayer.isAdrenaline())) {
				// This two lines implements the move
				sourceSector.removePlayer(currentPlayer);
				currentPlayer.setSector(targetSector);
				targetSector.addPlayer(currentPlayer);
				rrNotification.setMessage("You have moved to sector "
						+ targetSector.getCoordinate().toString());
				psNotification.setMessage("[GLOBAL MESSAGE]: "
						+ currentPlayer.getName() + " has moved.");
				// If the target sector is a dangerous sector continue the
				// execution
				// of the action
				if (targetSector.getSectorType() == SectorType.DANGEROUS
						&& !currentPlayer.isSedated()) {
					DrawSectorCardEffect effect = new DrawSectorCardEffect(
							new DrawSectorCardAction());
					effect.executeEffect(game, rrNotification, psNotification);
				} else if (targetSector.getSectorType() == SectorType.OPEN_RESCUE) {
					DrawRescueCardEffect effect = new DrawRescueCardEffect(
							new DrawRescueCardAction());
					effect.executeEffect(game, rrNotification, psNotification);
				}
				game.getCurrentPlayer().setHasMoved(true);
				return true;
			}

		}
		return false;
	}
}

package effects;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;
import common.TeleportObjectCard;

/**
 * Represents the effect of a teleport object card
 * 
 * @see ObjectCardEffect
 * @see TeleportObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class TeleportObjCardEffect extends ObjectCardEffect {

	/**
	 * Constructs an effect of a teleport object card. This effect is
	 * constructed from a {@link common.TeleportObjectCard}
	 * 
	 * @param teleportObjectCard
	 *            the {@link common.TeleportObjectCard} that needs to be
	 *            enriched with its effect
	 */
	public TeleportObjCardEffect(TeleportObjectCard teleportObjectCard) {
		super(teleportObjectCard);
	}

	/**
	 * Constructs an effect of a teleport object card. This effect is
	 * constructed from a {@link common.TeleportObjectCard} that is null
	 * 
	 */
	public TeleportObjCardEffect() {
		this(null);
	}

	/**
	 */
	@Override
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification) {
		GameMap map = game.gameMap;
		server_store.Player curr = game.currentPlayer;
		Sector humanSector = map.getHumanSector();

		// Move the player(can be only human) to the starting sector
		curr.currentSector.removePlayer(curr);
		curr.currentSector = humanSector;
		humanSector.addPlayer(curr);
		rrNotification.setMessage("You've teleported to the human sector");
		psNotification
				.setMessage(psNotification.getMessage()
						+ "\n[GLOBAL MESSAGE]: He/She will be teleported to the human sector");
		return true;
	}
}

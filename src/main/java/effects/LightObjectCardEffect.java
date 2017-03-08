package effects;

import java.util.List;

import it.polimi.ingsw.cg_19.Game;
import it.polimi.ingsw.cg_19.Player;
import common.LightsObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.Sector;

/**
 * This class represents the effect of a lights effect
 * 
 * @see ObjectCardEffect
 * @see LightsObjectCard
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.1
 */
public class LightObjectCardEffect extends ObjectCardEffect {

	/**
	 * Constructs an effect of a lights object card. This effect is constructed
	 * from a {@link common.LightsObjectCard}
	 * 
	 * @param lightObjectCard
	 *            the {@link common.LightsObjectCard} that needs to be enriched
	 *            with its effect
	 */
	public LightObjectCardEffect(LightsObjectCard lightObjectCard) {
		super(lightObjectCard);
	}

	/**
	 * Constructs an effect of a lights object card. This effect is constructed
	 * from a {@link common.LightsObjectCard} that is null. This constructor is
	 * used only for test purposes
	 * 
	 */
	public LightObjectCardEffect() {
		super(null);
	}

	/**
	 * @see ObjectCardEffect#executeEffect
	 */
	@Override
	public boolean executeEffect(Game game,
			RRClientNotification rrNotification,
			PSClientNotification psNotification) {
		LightsObjectCard lightsObjectCard = (LightsObjectCard) objectCard;
		List<Sector> neighboorSectors = game.getMap().getSearchableGraph()
				.neighborListOf(lightsObjectCard.getTarget());
		String playerName;
		String globalMessage = "\n[GLOBAL MESSAGE]: Players spotted:";
		for (Sector sector : neighboorSectors) {
			for (Player player : sector.getPlayers()) {
				playerName = player.getName();
				globalMessage += " " + playerName;
			}
			rrNotification.addSector(sector);
		}
		if (globalMessage.equals("\n[GLOBAL MESSAGE]: Players spotted:"))
			globalMessage += " none";
		psNotification.setMessage(psNotification.getMessage() + globalMessage);
		return true;
	}
}

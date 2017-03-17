package effects;

import it.polimi.ingsw.cg_19.Game;

import java.util.List;
import java.util.logging.Level;

import server.ServerLogger;
import common.PSClientNotification;
import common.RRClientNotification;
import common.UseSectorCardAction;

/**
 * Represents the effect of using a sector card
 * 
 * @see ActionEffect
 * @see UseSectorCardAction
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class UseSectorCardEffect extends ActionEffect {
	/**
	 * Constructs the effect of using a sector card. This effect is constructed
	 * from a {@link common.UseSectorCardAction}
	 * 
	 * @param useSectorAction
	 *            the action that needs to be enriched with its effect
	 */
	public UseSectorCardEffect(UseSectorCardAction useSectorAction) {
		super(useSectorAction);
	}

	/**
	 * Constructs the effect of using a sector card. This effect is constructed
	 * from a {@link common.UseSectorCardAction} that is null
	 * 
	 */
	public UseSectorCardEffect() {
		this(null);
	}

	/**
	 */
	@Override
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification rrNotification,
								 PSClientNotification psNotification) {
		UseSectorCardAction useAction = (UseSectorCardAction) action;
		SectorCardsMapper mapper = new SectorCardsMapper();
		game.lastAction = action;
		try {
			// Resolve and get the result of the sector card effect
			return mapper.getEffect(useAction.getCard()).executeEffect(game,
					rrNotification, psNotification);
		} catch (InstantiationException | IllegalAccessException e) {
			ServerLogger
					.getLogger()
					.log(Level.SEVERE,
							"Error in executing an action effect | UseSectorCardEffect",
							e);
			return false;
		}
	}
}

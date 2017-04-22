package effects;

import common.*;
import it.polimi.ingsw.cg_19.Game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

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

	public static boolean executeEffect(Game game,
										RRClientNotification rrNotification,
										PSClientNotification psNotification, Action action) {
		UseSectorCardAction castedAction = (UseSectorCardAction) action;
		SectorCardsMapper mapper = SectorCardsMapper.getInstance();
		game.setLastAction(action);
		try {
			Method executeMethod = mapper.getEffect(castedAction.getCard().getClass()).getMethod("executeEffect", Game.class, RRClientNotification.class, PSClientNotification.class, SectorCard.class);
			return (boolean)  executeMethod.invoke(null,game, rrNotification, psNotification, castedAction.getCard());
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();

		}
		return false;
	}
}

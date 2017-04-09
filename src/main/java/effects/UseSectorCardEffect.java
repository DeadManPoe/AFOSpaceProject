package effects;

import common.SectorCard;
import common.UseSectorCardAction;
import server_store.Game;
import server_store.StoreAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

	public static boolean executeEffect(Game game, StoreAction action) {
		UseSectorCardAction castedAction = (UseSectorCardAction) action;
		SectorCardsMapper mapper = new SectorCardsMapper();
		game.lastAction = action;
		try {
			Method executeMethod = mapper.getEffect(castedAction.payload).getMethod("executeEffect", Game.class, SectorCard.class);
			return (boolean)  executeMethod.invoke(null,game, castedAction.payload);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();

		}
		return false;
	}
}

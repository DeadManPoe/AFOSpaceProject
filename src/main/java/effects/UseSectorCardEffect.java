package effects;

import common.SectorCard;
import common.UseSectorCardAction;
import server.Game;
import server_store.StoreAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
		game.setLastAction(action);
		try {
			Method executeMethod = SectorCardsMapper.getInstance().getEffect(castedAction.getSectorCard()).getMethod("executeEffect", Game.class, SectorCard.class);
			return (boolean)  executeMethod.invoke(null,game, castedAction.getSectorCard());
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();

		}
		return false;
	}
}

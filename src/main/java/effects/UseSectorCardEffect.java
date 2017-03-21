package effects;

import com.sun.corba.se.spi.activation.Server;
import common.Action;
import common.UseSectorCardAction;
import server.ServerLogger;
import server_store.Game;
import server_store.ServerState;
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

	public static boolean executeEffect(Game game, UseSectorCardAction action) {
		SectorCardsMapper mapper = new SectorCardsMapper();
		game.lastAction = action;
		try {
			Method executeMethod = mapper.getEffect(action.payload).getMethod("execute");
			return (boolean)  executeMethod.invoke(game);
		} catch (InstantiationException | IllegalAccessException e) {
			ServerLogger
					.getLogger()
					.log(Level.SEVERE,
							"Error in executing an action effect | UseSectorCardEffect",
							e);
		} catch (NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();

		}
		return false;
	}
}

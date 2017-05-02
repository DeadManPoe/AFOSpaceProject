package effects;

import java.util.HashMap;
import java.util.Map;

import common.Action;
import common.DiscardAction;
import common.DrawObjectCardAction;
import common.DrawSectorCardAction;
import common.EndTurnAction;
import common.MoveAction;
import common.MoveAttackAction;
import common.UseObjAction;
import common.UseSectorCardAction;

/**
 * Represents the entity that maps an action with its effect. This entity has
 * been defined due to the fact that the client exchanges with the server plain
 * action objects that don't embed any logic, so an association has to be made
 * between these objects and their logic/effects.
 *
 */
public class ActionMapper {

	// The mapper
	private Map<Class<? extends Action>, Class<? extends ActionEffect>> fromActionToActionEffect;

    private static ActionMapper instance = new ActionMapper();

    public static ActionMapper getInstance(){
        return instance;
    }

	/**
	 * Constructs a action-effect mapper. This mapper is implemented as an hash
	 * map that maps action class types to action-effect class types, then,
	 * using reflection, from an action is possible to get an actual
	 * action-effect object.
	 */
	private ActionMapper() {
		// Mapper init
		fromActionToActionEffect = new HashMap<>();
		// Mapper filling
		fromActionToActionEffect.put(MoveAction.class, MoveActionEffect.class);
		fromActionToActionEffect.put(DrawSectorCardAction.class,
				DrawSectorCardEffect.class);
		fromActionToActionEffect.put(DrawObjectCardAction.class,
				DrawObjectCardEffect.class);
		fromActionToActionEffect.put(DiscardAction.class,
				DiscardObjCardEffect.class);
		fromActionToActionEffect
				.put(UseObjAction.class, UseObjCardEffect.class);
		fromActionToActionEffect.put(MoveAttackAction.class,
				MoveAttackActionEffect.class);
		fromActionToActionEffect.put(EndTurnAction.class, EndTurnEffect.class);
		fromActionToActionEffect.put(UseSectorCardAction.class,
				UseSectorCardEffect.class);
	}

	/**
	 * Produces the action effect mapped to an action.
	 * 
	 * @param actionClass
	 *            the action for which retrieve the effect
	 * @return The effect associated with the action.
	 */
	public Class<? extends ActionEffect> getEffect(Class<? extends Action> actionClass){
        return this.fromActionToActionEffect.get(actionClass);
	}

}
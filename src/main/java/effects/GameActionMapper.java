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
 * @see Action
 * @see ActionEffect
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class GameActionMapper {

	private static GameActionMapper instance = new GameActionMapper();

    private Map<String, Class<? extends ActionEffect>> fromActionToActionEffect;

    public static GameActionMapper getInstance(){
        return instance;
    }
	// The mapper


	/**
	 * Constructs a action-effect mapper. This mapper is implemented as an hash
	 * map that maps action class types to action-effect class types, then,
	 * using reflection, from an action is possible to get an actual
	 * action-effect object
	 */
	private GameActionMapper() {
		// Mapper init
		fromActionToActionEffect = new HashMap<>();
		// Mapper filling
		fromActionToActionEffect.put("@GAMEACTION_MOVE", MoveActionEffect.class);
		fromActionToActionEffect.put("@GAMEACTION_DRAW_SECTOR_CARD",
				DrawSectorCardEffect.class);
		fromActionToActionEffect.put("@GAMEACTION_DRAW_OBJ_CARD",
				DrawObjectCardEffect.class);
		fromActionToActionEffect.put("@GAMEACTION_DISCARD_OBJ_CARD",
				DiscardObjCardEffect.class);
		fromActionToActionEffect
				.put("@GAMEACTION_USE_OBJ_CARD", UseObjCardEffect.class);
		fromActionToActionEffect.put("@GAMEACTION_MOVE_ATTACK",
				MoveAttackActionEffect.class);
		fromActionToActionEffect.put("@GAMEACTION_END_TURN", EndTurnEffect.class);
		fromActionToActionEffect.put("@GAMEACTION_USE_SECTOR_CARD",
				UseSectorCardEffect.class);
	}


	public Class<? extends ActionEffect> getEffect(String actionType){
		return fromActionToActionEffect.get(actionType);
	}

}
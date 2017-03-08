package effects;

import java.util.HashMap;
import java.util.Map;

import common.AdrenalineObjectCard;
import common.AttackObjectCard;
import common.DefenseObjectCard;
import common.LightsObjectCard;
import common.ObjectCard;
import common.SuppressorObjectCard;
import common.TeleportObjectCard;

/**
 * Represents the entity that maps an object card with its effect. This entity
 * has been defined due to the fact that the client exchanges with the server
 * plain object cards that don't embed any logic, so an association has to be
 * made between these objects and their logic/effects.
 * 
 * @see ObjectCard
 * @see ObjectCardEffect
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ObjectCardsMapper {
	private Map<Class<? extends ObjectCard>, Class<? extends ObjectCardEffect>> fromObjectCardToObjectCardEffect;

	/**
	 * Constructs a object card - object card effect mapper. This mapper is
	 * implemented as an hash map that maps object cards class types to object
	 * cards effect class types, then, using reflection, from an object card is
	 * possible to get an actual object card effect object
	 */
	public ObjectCardsMapper() {
		fromObjectCardToObjectCardEffect = new HashMap<Class<? extends ObjectCard>, Class<? extends ObjectCardEffect>>();
		fromObjectCardToObjectCardEffect.put(TeleportObjectCard.class,
				TeleportObjCardEffect.class);
		fromObjectCardToObjectCardEffect.put(AttackObjectCard.class,
				AttackObjCardEffect.class);
		fromObjectCardToObjectCardEffect.put(LightsObjectCard.class,
				LightObjectCardEffect.class);
		fromObjectCardToObjectCardEffect.put(SuppressorObjectCard.class,
				SuppressorEffect.class);
		fromObjectCardToObjectCardEffect.put(AdrenalineObjectCard.class,
				AdrenalineObjCardEffect.class);
		fromObjectCardToObjectCardEffect.put(DefenseObjectCard.class,
				DefenseObjCardEffect.class);
	}

	/**
	 * Produces the object card effect mapped to an object card
	 * 
	 * @param objectCard
	 *            the object card for which retrieve the effect
	 * @return the effect associated with the object card
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ObjectCardEffect getEffect(ObjectCard objectCard)
			throws InstantiationException, IllegalAccessException {
		ObjectCardEffect effect = fromObjectCardToObjectCardEffect.get(
				objectCard.getClass()).newInstance();
		effect.setObject(objectCard);
		return effect;
	}

}
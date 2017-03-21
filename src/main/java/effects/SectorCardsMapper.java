package effects;

import java.util.HashMap;
import java.util.Map;

import common.GlobalNoiseSectorCard;
import common.LocalNoiseSectorCard;
import common.SectorCard;
import common.SilenceSectorCard;

/**
 * Represents the entity that maps a sector card with its effect. This entity
 * has been defined due to the fact that the client exchanges with the server
 * plain sector cards that don't embed any logic, so an association has to be
 * made between these objects and their logic/effects.
 * 
 * @see SectorCard
 * @see SectorCardEffect
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class SectorCardsMapper {
	private Map<Class<? extends SectorCard>, Class<? extends SectorCardEffect>> fromSectorToSectorEffect;

	/**
	 * Constructs a sector card - effect mapper. This mapper is implemented as
	 * an hash map that maps sector card class types to sector card effect class
	 * types, then, using reflection, from a sector card is possible to get an
	 * actual sector card effect object
	 */
	public SectorCardsMapper() {
		fromSectorToSectorEffect = new HashMap<Class<? extends SectorCard>, Class<? extends SectorCardEffect>>();
		fromSectorToSectorEffect.put(LocalNoiseSectorCard.class,
				LocalNoiseSectorCardEffect.class);
		fromSectorToSectorEffect.put(GlobalNoiseSectorCard.class,
				GlobalNoiseSectorCardEffect.class);
		fromSectorToSectorEffect.put(SilenceSectorCard.class,
				SilenceSectorCardEffect.class);
	}

	/**
	 * Produces the sector card effect mapped to a sector card
	 * 
	 * @param sectorCard
	 *            the sector card for which retrieve the effect
	 * @return the effect associated with the sector card
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Class<? extends SectorCardEffect> getEffect(SectorCard sectorCard)
			throws InstantiationException, IllegalAccessException {
		return fromSectorToSectorEffect.get(sectorCard.getClass());
	}
}

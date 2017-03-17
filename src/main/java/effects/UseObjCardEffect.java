package effects;

import java.util.ArrayList;
import java.util.List;

import common.AdrenalineObjectCard;
import common.AttackObjectCard;
import common.Card;
import common.DefenseObjectCard;
import common.LightsObjectCard;
import common.PSClientNotification;
import common.RRClientNotification;
import common.SuppressorObjectCard;
import common.TeleportObjectCard;
import common.UseObjAction;
import it.polimi.ingsw.cg_19.Game;

/**
 * This class represents the effect associated to a use object action
 * 
 * @see ActionEffect
 * @see UseObjAction
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class UseObjCardEffect extends ActionEffect {
	/**
	 * Initializes the effect wrapping inside an UseObjAction
	 * 
	 * @param objectCard
	 *            The action that needs to be enriched with its effect
	 */
	private List<Class<? extends Card>> beforeMoveCards = new ArrayList<Class<? extends Card>>();
	private List<Class<? extends Card>> afterMoveCards = new ArrayList<Class<? extends Card>>();

	public UseObjCardEffect(UseObjAction useObjAction) {
		super(useObjAction);
		beforeMoveCards.add(AdrenalineObjectCard.class);
		beforeMoveCards.add(SuppressorObjectCard.class);
		beforeMoveCards.add(LightsObjectCard.class);
		beforeMoveCards.add(TeleportObjectCard.class);
		beforeMoveCards.add(DefenseObjectCard.class);
		beforeMoveCards.add(AttackObjectCard.class);

		afterMoveCards.add(LightsObjectCard.class);
		afterMoveCards.add(TeleportObjectCard.class);
	}

	/**
	 * Construct a "default" UseObjActionEffect, objectAction is assumed null
	 */
	public UseObjCardEffect() {
		this(null);
	}

	/**
	 */
	@Override
	public boolean executeEffect(server_store.Game game,
								 RRClientNotification clientNotification,
								 PSClientNotification psNotification) {
		UseObjAction useAction = (UseObjAction) action;
		ObjectCardsMapper mapper = new ObjectCardsMapper();
		// Checks if the card can be played before move or after move
		if (!game.currentPlayer.hasMoved) {
			if (!beforeMoveCards.contains(useAction.getCard().getClass()))
				return false;
		} else {
			if (!afterMoveCards.contains(useAction.getCard().getClass()))
				return false;
		}

		try {
			clientNotification.setMessage("You have used a"
					+ useAction.getCard().toString());
			psNotification.setMessage("[GLOBAL MESSAGE]: "
					+ game.currentPlayer.name+ " has used a "
					+ useAction.getCard().toString());
			/*
			 * ObjectCard card = useAction.getCard(); if(useAction.getCard()
			 * instanceof LightObjectCard){ card = new LightObjectCard(null); }
			 * else if(useAction.getCard() instanceof AttackObjectCard){ card =
			 * new AttackObjectCard(null); }
			 */
			game.objectDeck.addToDiscard(useAction.getCard());
			game.currentPlayer.privateDeck
					.removeCard(useAction.getCard());
			game.lastAction = action;
			return mapper.getEffect(useAction.getCard()).executeEffect(game,
					clientNotification, psNotification);
		} catch (InstantiationException | IllegalAccessException e) {
			return false;
		}
	}
}

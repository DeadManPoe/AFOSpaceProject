package effects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import common.*;
import it.polimi.ingsw.cg_19.Game;

/**
 * This class represents the effect associated to a use object action
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 * @see ActionEffect
 * @see UseObjAction
 */
public class UseObjCardEffect extends ActionEffect {
    /**
     * Initializes the effect wrapping inside an UseObjAction
     * <p>
     * The action that needs to be enriched with its effect
     */
    private static List<Class<? extends Card>> beforeMoveCards = new ArrayList<>();
    private static List<Class<? extends Card>> afterMoveCards = new ArrayList<>();

    private static void produceUtilsDataStructure() {
        beforeMoveCards.add(AdrenalineObjectCard.class);
        beforeMoveCards.add(SuppressorObjectCard.class);
        beforeMoveCards.add(LightsObjectCard.class);
        beforeMoveCards.add(TeleportObjectCard.class);
        beforeMoveCards.add(DefenseObjectCard.class);
        beforeMoveCards.add(AttackObjectCard.class);
        afterMoveCards.add(LightsObjectCard.class);
        afterMoveCards.add(TeleportObjectCard.class);
    }


    public static boolean executeEffect(Game game,
                                        RRClientNotification rrNotification,
                                        PSClientNotification psNotification, Action action) {
        UseObjAction castedAction = (UseObjAction) action;
        ObjectCard associatedCard = castedAction.getCard();
        if (beforeMoveCards.size() == 0 && afterMoveCards.size() == 0) {
            produceUtilsDataStructure();
        }
        ObjectCardsMapper mapper = new ObjectCardsMapper();
        // Checks if the card can be played before moveToSector or after moveToSector
        if (!game.getCurrentPlayer().isHasMoved()) {
            if (!beforeMoveCards.contains(associatedCard.getClass()))
                return false;
        } else {
            if (!afterMoveCards.contains(associatedCard.getClass()))
                return false;
        }

        try {
            rrNotification.setMessage("You have used a "
                    + associatedCard.toString());
            psNotification.setMessage("[GLOBAL MESSAGE]: "
                    + game.getCurrentPlayer().getName() + " has used a "
                    + associatedCard.toString());
            game.getObjectDeck().addToDiscard(associatedCard);
            game.getCurrentPlayer().getPrivateDeck()
                    .removeCard(associatedCard);
            game.setLastAction(action);
            Method executeMethod = mapper.getEffect(associatedCard.getClass()).getMethod("executeEffect",
                    Game.class, RRClientNotification.class, PSClientNotification.class, ObjectCard.class);
            return (boolean) executeMethod.invoke(null, game, rrNotification, psNotification, associatedCard);

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}

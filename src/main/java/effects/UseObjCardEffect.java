package effects;

import common.*;
import server.Game;
import server_store.StoreAction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
     *
     * @param objectCard
     * The action that needs to be enriched with its effect
     */
    private static List<Class<? extends Card>> beforeMoveCards = new ArrayList<Class<? extends Card>>();
    private static List<Class<? extends Card>> afterMoveCards = new ArrayList<Class<? extends Card>>();

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


    public static boolean executeEffect(Game game, StoreAction action) {
        UseObjAction castedAction = (UseObjAction) action;
        if (beforeMoveCards.size() == 0 && afterMoveCards.size() == 0) {
            produceUtilsDataStructure();
        }
        RRClientNotification clientNotification = new RRClientNotification();
        PSClientNotification psNotification = new PSClientNotification();
        // Checks if the card can be played before moveToSector or after moveToSector
        if (!game.getCurrentPlayer().isHasMoved()) {
            if (!beforeMoveCards.contains(castedAction.getObjectCard().getClass()))
                return false;
        } else {
            if (!afterMoveCards.contains(castedAction.getObjectCard().getClass()))
                return false;
        }

        try {
            clientNotification.setMessage("You have used a "
                    + castedAction.getObjectCard().toString());
            psNotification.setMessage("[GLOBAL MESSAGE]: "
                    + game.getCurrentPlayer().getName() + " has used a "
                    + castedAction.getObjectCard().toString());
            game.getObjectDeck().addToDiscard(castedAction.getObjectCard());
            game.getCurrentPlayer().getPrivateDeck()
                    .removeCard(castedAction.getObjectCard());
            game.setLastAction(action);
            game.setLastRRclientNotification(clientNotification);
            game.setLastPSclientNotification(psNotification);
            Method executeMethod = ObjectCardsMapper.getInstance().getEffect(castedAction.getObjectCard()).getMethod("executeEffect", Game.class, ObjectCard.class);
            return (boolean) executeMethod.invoke(null,game, castedAction.getObjectCard());

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

}

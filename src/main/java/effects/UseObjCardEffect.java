package effects;

import common.*;
import server_store.Game;

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


    public static boolean executeEffect(Game game, UseObjAction action) {
        if (beforeMoveCards.size() == 0 && afterMoveCards.size() == 0) {
            produceUtilsDataStructure();
        }
        ObjectCardsMapper mapper = new ObjectCardsMapper();
        RRClientNotification clientNotification = new RRClientNotification();
        PSClientNotification psNotification = new PSClientNotification();
        // Checks if the card can be played before move or after move
        if (!game.currentPlayer.hasMoved) {
            if (!beforeMoveCards.contains(action.payload.getClass()))
                return false;
        } else {
            if (!afterMoveCards.contains(action.payload.getClass()))
                return false;
        }

        try {
            clientNotification.setMessage("You have used a"
                    + action.payload.toString());
            psNotification.setMessage("[GLOBAL MESSAGE]: "
                    + game.currentPlayer.name + " has used a "
                    + action.payload.toString());
            game.objectDeck.addToDiscard(action.payload);
            game.currentPlayer.privateDeck
                    .removeCard(action.payload);
            game.lastAction = action;
            game.lastRRclientNotification = clientNotification;
            game.lastPSclientNotification = psNotification;
            Method executeMethod = mapper.getEffect(action.payload).getMethod("execute");
            return (boolean) executeMethod.invoke(game, action.payload);

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

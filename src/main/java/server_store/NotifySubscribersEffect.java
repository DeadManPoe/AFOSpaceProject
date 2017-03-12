package server_store;

import server.SubscriberHandler;
import sts.Action;
import sts.ActionFactory;
import sts.Effect;

/**
 * Created by giorgiopea on 12/03/17.
 */
public class NotifySubscribersEffect extends Effect {
    public NotifySubscribersEffect(ActionFactory actionFactory) {
        super(actionFactory);
    }
    @Override
    public void apply(Action action) {
        SubscriberNotification subscriberNotification = (SubscriberNotification) action.payload;
        for(PubSubHandler handler : this.store.getState().GAME_TO_SUBSCRIBERS.get(subscriberNotification.gameId)){
            handler.queueNotification(subscriberNotification.remoteMethodCall);
        }
    }
}

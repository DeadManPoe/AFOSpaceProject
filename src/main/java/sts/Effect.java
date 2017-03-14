package sts;

import store_actions.StoreAction;

/**
 * Created by giorgiopea on 08/03/17.
 *
 */
public abstract class Effect {
    protected final Store store;
    protected final ActionFactory actionFactory;

    public Effect(ActionFactory actionFactory) {
        this.store = Store.getInstance();
        this.actionFactory = actionFactory;
    }
    public abstract void apply(StoreAction action);
}

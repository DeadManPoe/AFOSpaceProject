package contracts;

import sts.Action;

import java.util.List;

/**
 * Created by giorgiopea on 11/03/17.
 */
public class StateChangedNotification {
    public List<String> changedStateKeys;
    public Action lastAction;

    public StateChangedNotification(List<String> changedStateKeys, Action lastAction) {
        this.changedStateKeys = changedStateKeys;
        this.lastAction = lastAction;
    }
}

package server_store;

import common.Action;
import common.MoveAction;
import common.UseObjAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class HumanTurn extends Turn {

    public HumanTurn() {
        this.initialActions = new ArrayList<Class<? extends Action>>();
        initialActions.add(MoveAction.class);
        initialActions.add(UseObjAction.class);
    }
}

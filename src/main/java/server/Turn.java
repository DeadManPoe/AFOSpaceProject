package server;

import common.Action;

import java.util.List;

/**
 * Created by giorgiopea on 13/03/17.
 */
public abstract class Turn {
    protected List<Class<? extends Action>> initialActions;
}

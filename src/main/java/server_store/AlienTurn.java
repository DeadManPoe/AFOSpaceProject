package server_store;

import common.Action;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 13/03/17.
 */
public class AlienTurn extends Turn {

    public AlienTurn() {
        this.initialActions = new ArrayList<Class<? extends Action>>();
    }
}

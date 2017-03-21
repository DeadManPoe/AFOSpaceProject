package server_store;

import java.io.Serializable;

/**
 * Created by giorgiopea on 14/03/17.
 */
public abstract class StoreAction implements Serializable {
    protected String type;

    public String getType() {
        return type;
    }
}

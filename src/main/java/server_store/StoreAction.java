package server_store;

import java.io.Serializable;

/**
 * Created by giorgiopea on 14/03/17.
 */
public abstract class StoreAction implements Serializable {
    public String type;

    public StoreAction(String type) {
        this.type = type;
    }
}

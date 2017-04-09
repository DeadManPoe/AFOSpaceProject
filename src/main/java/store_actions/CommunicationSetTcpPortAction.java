package store_actions;

import server_store.StoreAction;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class CommunicationSetTcpPortAction extends StoreAction {
    private Integer payload;

    public CommunicationSetTcpPortAction(Integer tcpPort) {
        super("@COMMUNICATION_SET_TCPORT");
        this.payload = tcpPort;

    }

    public Integer getPayload() {
        return payload;
    }
}

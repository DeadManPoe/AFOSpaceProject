package store_actions;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class CommunicationSetTcpPortAction extends StoreAction {
    private Integer payload;

    public CommunicationSetTcpPortAction(Integer tcpPort) {
        this.type = "@COMMUNICATION_SET_TCPORT";
        this.payload = tcpPort;

    }

    public Integer getPayload() {
        return payload;
    }
}

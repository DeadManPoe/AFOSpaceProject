package store_reducers;

import com.sun.corba.se.spi.activation.Server;
import org.apache.commons.lang.SerializationUtils;
import server_store.PubSubHandler;
import server_store.ReqRespHandler;
import server_store.ServerState;
import server_store.ServerStore;
import store_actions.CommunicationAddPubSubHandlerAction;
import store_actions.CommunicationAddReqRespHandlerAction;
import store_actions.CommunicationSetTcpPortAction;
import store_actions.StoreAction;
import sts.Reducer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 14/03/17.
 *
 */
public class CommunicationReducer extends Reducer {

    @Override
    public ServerState reduce(StoreAction action, ServerState state) {
        String actionType = action.getType();
        if(actionType.equals("@COMMUNICATION_SET_TCPORT")){
            this.setTcpPort(action,state);
        }
        else if(actionType.equals("@COMMUNICATION_ADD_REQRESP_HANDLER")){
            this.addReqRespHandlerAction(action,state);
        }
        else if(actionType.equals("@COMMUNICATION_ADD_PUBSUB_HANDLER")){
            this.addPubSubHandler(action,state);
        }
        return state;
    }

    private ServerState setTcpPort(StoreAction action, ServerState state){
        CommunicationSetTcpPortAction castedAction = (CommunicationSetTcpPortAction) action;
        state.setTcp_port(castedAction.getPayload());
        return state;
    }
    private ServerState addReqRespHandlerAction(StoreAction action, ServerState state){
        CommunicationAddReqRespHandlerAction castedAction = (CommunicationAddReqRespHandlerAction) action;
        state.getReqRespHandlers().add(castedAction.getPayload());
        return state;
    }
    private ServerState addPubSubHandler(StoreAction action, ServerState state){
        CommunicationAddPubSubHandlerAction castedAction = (CommunicationAddPubSubHandlerAction) action;
        state.getPubSubHandlers().add(castedAction.getPayload());
        return state;
    }
}

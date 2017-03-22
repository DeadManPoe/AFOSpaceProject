package store_reducers;

import server_store.*;
import store_actions.*;
import server_store.Reducer;

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
        else if(actionType.equals("@COMMUNICATION_REMOVE_PUBSUB_HANDLER")){
            this.removePubSubHandler(action,state);
        }
        else if(actionType.equals("@COMMUNICATION_REMOVE_REQRESP_HANDLER")){
            this.removeReqRespHandler(action,state);
        }
        return state;
    }

    private ServerState removeReqRespHandler(StoreAction action, ServerState state) {
        CommunicationRemoveReqRespHandlerAction castedAction = (CommunicationRemoveReqRespHandlerAction) action;
        for (int i=0; i<state.getReqRespHandlers().size(); i++){
            if(state.getReqRespHandlers().get(i).getUuid().equals(castedAction.getPayload())){
                state.getReqRespHandlers().remove(i);
                return state;
            }
        }
        return state;
    }

    private ServerState removePubSubHandler(StoreAction action, ServerState state) {
        CommunicationRemovePubSubHandlerAction castedAction = (CommunicationRemovePubSubHandlerAction) action;
        for (int i=0; i<state.getPubSubHandlers().size(); i++){
            if(state.getPubSubHandlers().get(i).getPlayerToken().getGameId().equals(castedAction.getPayload())){
                state.getPubSubHandlers().remove(i);
                return state;
            }
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

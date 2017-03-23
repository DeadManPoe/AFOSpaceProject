package store_reducers;

import server_store.*;
import store_actions.*;
import server_store.Reducer;

/**
 * Created by giorgiopea on 14/03/17.
 *
 * Handles the logic related to the slice of the app's state represented by the list of
 * ReqResp and PubSub handlers
 */
public class CommunicationReducer extends Reducer {

    @Override
    public ServerState reduce(StoreAction action, ServerState state) {
        String actionType = action.getType();
        switch (actionType) {
            case "@COMMUNICATION_SET_TCPORT":
                this.setTcpPort(action, state);
                break;
            case "@COMMUNICATION_ADD_REQRESP_HANDLER":
                this.addReqRespHandlerAction(action, state);
                break;
            case "@COMMUNICATION_ADD_PUBSUB_HANDLER":
                this.addPubSubHandler(action, state);
                break;
            case "@COMMUNICATION_REMOVE_PUBSUB_HANDLER":
                this.removePubSubHandler(action, state);
                break;
            case "@COMMUNICATION_REMOVE_REQRESP_HANDLER":
                this.removeReqRespHandler(action, state);
                break;
        }
        return state;
    }

    /**
     * Removes a ReqResp handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationRemoveReqRespHandlerAction}
     * @param state The current app's state
     * @see server_store.ReqRespHandler
     * @return The app's new state
     */
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
    /**
     * Removes a PubSub handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationRemovePubSubHandlerAction}
     * @param state The current app's state
     * @see server_store.PubSubHandler
     * @return The app's new state
     */
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

    /**
     * Sets the tcp port to be used for client-server connections
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationSetTcpPortAction}
     * @param state The current app's state
     * @return The app's new state
     */
    private ServerState setTcpPort(StoreAction action, ServerState state){
        CommunicationSetTcpPortAction castedAction = (CommunicationSetTcpPortAction) action;
        state.setTcp_port(castedAction.getPayload());
        return state;
    }
    /**
     * Adds a ReqResp handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationAddReqRespHandlerAction}
     * @param state The current app's state
     * @see server_store.ReqRespHandler
     * @return The app's new state
     */
    private ServerState addReqRespHandlerAction(StoreAction action, ServerState state){
        CommunicationAddReqRespHandlerAction castedAction = (CommunicationAddReqRespHandlerAction) action;
        state.getReqRespHandlers().add(castedAction.getPayload());
        return state;
    }
    /**
     * Adds a PubSun handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationAddPubSubHandlerAction}
     * @param state The current app's state
     * @see server_store.PubSubHandler
     * @return The app's new state
     */
    private ServerState addPubSubHandler(StoreAction action, ServerState state){
        CommunicationAddPubSubHandlerAction castedAction = (CommunicationAddPubSubHandlerAction) action;
        state.getPubSubHandlers().add(castedAction.getPayload());
        return state;
    }
}

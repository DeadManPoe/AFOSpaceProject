package store_reducers;

import server.PubSubHandler;
import server.ReqRespHandler;
import server_store.*;
import store_actions.*;
import server_store.Reducer;

/**
 * Created by giorgiopea on 14/03/17.
 *
 * Handles the logic related to the slice of the app's state represented by the list of
 * ReqResp and PubSub handlers
 */
public class CommunicationReducer implements Reducer {

    @Override
    public ServerState reduce(StoreAction action, State state) {
        ServerState castedState = (ServerState) state;
        switch (action.type) {
            case "@COMMUNICATION_SET_TCPORT":
                this.setTcpPort(action, castedState);
                break;
            case "@COMMUNICATION_ADD_REQRESP_HANDLER":
                this.addReqRespHandlerAction(action, castedState);
                break;
            case "@COMMUNICATION_ADD_PUBSUB_HANDLER":
                this.addPubSubHandler(action, castedState);
                break;
            case "@COMMUNICATION_REMOVE_PUBSUB_HANDLER":
                this.removePubSubHandler(action, castedState);
                break;
            case "@COMMUNICATION_REMOVE_REQRESP_HANDLER":
                this.removeReqRespHandler(action, castedState);
                break;
        }
        return castedState;
    }

    /**
     * Removes a ReqResp handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationRemoveReqRespHandlerAction}
     * @param state The current app's state
     * @see ReqRespHandler
     * @return The app's new state
     */
    private ServerState removeReqRespHandler(StoreAction action, ServerState state) {
        CommunicationRemoveReqRespHandlerAction castedAction = (CommunicationRemoveReqRespHandlerAction) action;
        state.getReqRespHandlers().remove(castedAction.getHandler());
        return state;
    }
    /**
     * Removes a PubSub handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationRemovePubSubHandlerAction}
     * @param state The current app's state
     * @see PubSubHandler
     * @return The app's new state
     */
    private ServerState removePubSubHandler(StoreAction action, ServerState state) {
        CommunicationRemovePubSubHandlerAction castedAction = (CommunicationRemovePubSubHandlerAction) action;
        state.getPubSubHandlers().remove(castedAction.getHandler());
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
        state.setTcpPort(castedAction.getPayload());
        return state;
    }
    /**
     * Adds a ReqResp handler from the list of ReqResp handlers in the app's state
     * @param action The action that has triggered this task, see {@link store_actions.CommunicationAddReqRespHandlerAction}
     * @param state The current app's state
     * @see ReqRespHandler
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
     * @see PubSubHandler
     * @return The app's new state
     */
    private ServerState addPubSubHandler(StoreAction action, ServerState state){
        CommunicationAddPubSubHandlerAction castedAction = (CommunicationAddPubSubHandlerAction) action;
        state.getPubSubHandlers().add(castedAction.getPubSubHandler());
        return state;
    }
}

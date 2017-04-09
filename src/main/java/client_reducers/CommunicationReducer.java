package client_reducers;

import client_store.ClientState;
import client_store.PubSubHandler;
import client_store_actions.ClientAddPubSubHandlerAction;
import server_store.Reducer;
import server_store.State;
import server_store.StoreAction;

import java.io.IOException;

/**
 * Created by giorgiopea on 25/03/17.
 *
 */
public class CommunicationReducer implements Reducer {
    @Override
    public State reduce(StoreAction action, State state) {
        ClientState castedState = (ClientState) state;
        switch (action.type){
            case "@COMMUNICATION_ADD_PUB_SUB_HANDLER":
                return this.addPubSubHandler(action,castedState);
            case "@COMMUNICATION_REMOVE_PUBSUB_HANDLER":
                return this.removePubSubHandler(castedState);
        }
        return castedState;
    }

    private State removePubSubHandler(ClientState state) {
        try {
            state.currentPubSubHandler.inputStream.close();
            state.currentPubSubHandler.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        state.currentPubSubHandler = null;
        return state;

    }

    private State addPubSubHandler(StoreAction action, ClientState state) {
        ClientAddPubSubHandlerAction castedAction = (ClientAddPubSubHandlerAction) action;
        state.currentPubSubHandler = new PubSubHandler(castedAction.socket, castedAction.inputStream);
        return state;
    }
}

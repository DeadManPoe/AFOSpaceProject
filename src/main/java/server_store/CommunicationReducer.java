package server_store;

import org.apache.commons.lang.SerializationUtils;
import server.SocketThread;
import sts.Action;
import sts.Reducer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giorgiopea on 10/03/17.
 *
 */
public class CommunicationReducer extends Reducer {

    public CommunicationReducer(List<String> writableStateKeys) {
        super(writableStateKeys);
    }

    @Override
    public ServerState reduce(Action action, ServerState state) {
        if(action.type.equals("@COMMUNICATION_SET_TCPORT")){
            ServerState newState = new ServerState(state);
            newState.TCP_PORT = (Integer )action.payload;
            return newState;
        }
        return state;
    }


}

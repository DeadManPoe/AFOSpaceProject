package server_store;

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
    public Map<String, Object> reduce(Action action, Map<String, Object> state) {
        Map<String,Object> newState = new HashMap<String,Object>(state);
        if(action.type.equals("@COMMUNICATION_SET_CONNECTION")){
            newState.put("@COMMUNICATION_CONNECTION_INFO", action.payload);
        }
        else if(action.type.equals("@COMMUNICATION_ADD_SOCKET")){
            newState.put("@COMMUNICATION_ACTIVE_SOCKETS",action.payload);
        }
        return newState;
    }
}

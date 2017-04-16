package client;

import client_store.ClientStore;
import common.EndTurnAction;
import common.RRClientNotification;
import common.RemoteMethodCall;
import org.junit.Test;
import server_store.StoreAction;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by giorgiopea on 16/04/17.
 */
public class InteractionManagerTests {

    @Test
    public void endTurnTest(){
        CommunicationHandler communicationHandler = mock(CommunicationHandler.class);
        ArrayList<Object> parameters = new ArrayList<>();
        RRClientNotification rrClientNotification = new RRClientNotification();
        rrClientNotification.setActionResult(true);
        parameters.add(rrClientNotification);
        try {
            when(communicationHandler.newComSession((RemoteMethodCall) anyObject())).thenReturn(
                    new RemoteMethodCall("syncNotification",parameters));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        ClientStore.getInstance().getState().isMyTurn = true;
        InteractionManager interactionManager = new InteractionManager(communicationHandler);
        interactionManager.endTurn();
        assertFalse(ClientStore.getInstance().getState().isMyTurn);
    }

}

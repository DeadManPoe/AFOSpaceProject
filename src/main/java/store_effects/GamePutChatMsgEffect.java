package store_effects;

import common.Player;
import common.RemoteMethodCall;
import server.ClientMethodsNamesProvider;
import server.Game;
import server.PubSubHandler;
import server_store.Effect;
import server_store.ServerState;
import server_store.State;
import server_store.StoreAction;
import store_actions.GamePutChatMsg;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 04/05/17.
 *
 */
public class GamePutChatMsgEffect implements Effect {
    @Override
    public void apply(StoreAction action, State state) {
        String playerName = "";
        ServerState castedState = (ServerState) state;
        GamePutChatMsg castedAction = (GamePutChatMsg) action;
        for (Game game : castedState.getGames()){
            //if (game.getGamePublicData().getId() == castedAction.getPlayerToken().getGameId()){
                for (Player player : game.getPlayers()) {
                    if (player.getPlayerToken().equals(castedAction.getPlayerToken())) {
                        playerName = player.getName();
                        break;
                    }
                }
            //}
        }
        for (PubSubHandler pubSubHandler : castedState.getPubSubHandlers()){
            if (pubSubHandler.getPlayerToken().getGameId() == castedAction.getPlayerToken().getGameId()){
                ArrayList<Object> parameters = new ArrayList<>();
                parameters.add(playerName + " says: "+castedAction.getMessage());
                pubSubHandler.queueNotification(new RemoteMethodCall(ClientMethodsNamesProvider.getInstance().chatMessage(),parameters));
            }
        }
    }
}

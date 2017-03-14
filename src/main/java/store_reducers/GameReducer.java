package store_reducers;

import common.PlayerToken;
import common.RemoteMethodCall;
import it.polimi.ingsw.cg_19.PlayerType;
import server_store.Game;
import server_store.ReqRespHandler;
import server_store.ServerState;
import store_actions.GameAddPlayerAction;
import store_actions.GameMakeActionAction;
import store_actions.StoreAction;
import sts.Reducer;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameReducer extends Reducer {
    @Override
    public ServerState reduce(StoreAction action, ServerState state) {

        String actionType = action.getType();
        if(actionType.equals("@GAME_ADD_PLAYER")){
            this.addPlayer(action,state);
        }
        else if(actionType.equals("@GAME_MAKE_ACTION")){
            this.makeAction(action,state);
        }
        return state;

    }

    private ServerState makeAction(StoreAction action, ServerState state) {
        GameMakeActionAction castedAction = (GameMakeActionAction) action;
        for(Game game : state.getGames()){
            if(game.gamePublicData.getId() == castedAction.getPayload().getGameId()){

            }
        }
    }

    private ServerState addPlayer(StoreAction action, ServerState state) {
        GameAddPlayerAction castedAction = (GameAddPlayerAction) action;
        PlayerToken playerToken = null;
        for(Game game : state.getGames()){
            if(game.gamePublicData.getId() == castedAction.getPayload().getGameId()){
                PlayerType playerType = assignTypeToPlayer(game.players.size() + 1);
                playerToken = new PlayerToken(playerType);
                server_store.Player player = new server_store.Player(playerType,castedAction.getPayload().getPlayerName(),playerToken);
                game.players.add(player);
                break;
            }
        }
        for(ReqRespHandler handler : state.getReqRespHandlers()){
            if(handler.getUuid().equals(castedAction.getPayload().getUuid())){
                ArrayList<Object> parameters = new ArrayList<>();
                parameters.add(playerToken);
                handler.addRemoteMethodCallToQueue(new RemoteMethodCall("sendToken",parameters));
                break;
            }
        }
        return state;
    }


    /**
     * Produces a player type based on the number of players already in game .
     * If the number of players already in game is even, the returned player
     * type is "HUMAN", otherwise is "ALIEN". This procedure is adopted in order
     * to guarantee a balanced number of aliens and humans
     *
     * @param numberOfPlayers
     *            the number of players already in game
     * @return a player type, either "HUMAN" or "ALIEN"
     */
    public PlayerType assignTypeToPlayer(int numberOfPlayers) {
        if (numberOfPlayers % 2 == 0) {
            return PlayerType.HUMAN;
        } else {
            return PlayerType.ALIEN;
        }

    }
}

package store_effects;

import common.RemoteMethodCall;
import server_store.*;
import store_actions.GameMakeActionAction;
import store_actions.GamesEndGameAction;

import java.util.ArrayList;

/**
 * Created by giorgiopea on 20/03/17.
 * <p>
 * Handles the side-effects related to an in-game action
 */
public class GameMakeActionEffect extends Effect {

    @Override
    public void apply(StoreAction action, ServerState state) {
        /*
            This method will check if some faction has won the game, will notify the clients with the
            messages produces by the in-game action, will schedule a new turn's timeout and will end the turn
            if a faction has won
         */
        GameMakeActionAction castedAction = (GameMakeActionAction) action;
        Game game = null;
        for (Game c_game : state.getGames()) {
            if (c_game.gamePublicData.getId() == castedAction.payload.playerToken.getGameId()) {
                game = c_game;
                break;
            }
        }
        if (game != null) {
            if (game.didHumansWin) {
                game.lastPSclientNotification
                        .setMessage(game.lastPSclientNotification.getMessage()
                                + "\n[GLOBAL MESSAGE]: The game has ended, HUMANS WIN!");
                game.lastPSclientNotification.setHumanWins(true);
            }
            if (game.didAlienWin) {
                game.lastPSclientNotification
                        .setMessage(game.lastPSclientNotification.getMessage()
                                + "\n[GLOBAL MESSAGE]: The game has ended, ALIENS WIN!");
                game.lastPSclientNotification.setAlienWins(true);

            }
            for (ReqRespHandler handler : state.getReqRespHandlers()) {
                if (handler.getUuid().equals(castedAction.payload.reqRespHandlerUUID)) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(game.lastRRclientNotification);
                    handler.addRemoteMethodCallToQueue(new RemoteMethodCall("sendNotification", parameters));
                    break;
                }
            }
            for (PubSubHandler handler : state.getPubSubHandlers()) {
                if (handler.getPlayerToken().getGameId() == game.gamePublicData.getId()) {
                    ArrayList<Object> parameters = new ArrayList<>();
                    parameters.add(game.lastPSclientNotification);
                    handler.queueNotification(new RemoteMethodCall("sendPubNotification", parameters));

                    if (castedAction.payload.action.getType().equals("@GAMEACTION_END_TURN") && game.currentPlayer.playerToken.equals(handler.getPlayerToken())) {
                        handler.queueNotification(new RemoteMethodCall("allowTurn", new ArrayList<Object>()));
                    }

                }
            }
            if (!game.didAlienWin && !game.didHumansWin) {
                game.currentTimer.schedule(new TurnTimeout(castedAction.payload.playerToken.getGameId()), state.getTurnTimeout());
            } else {
                ServerStore.getInstance().dispatchAction(new GamesEndGameAction(game.gamePublicData.getId()));
            }

        }


    }
}

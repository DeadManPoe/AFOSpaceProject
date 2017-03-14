package store_actions;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GamesRemoveGameAction extends StoreAction {

    private Integer payload;

    public GamesRemoveGameAction(Integer gameId) {
        this.type = "@GAMES_REMOVE_GAME";
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}

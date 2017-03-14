package store_actions;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameEndGame extends StoreAction {

    private Integer payload;

    public GameEndGame(Integer gameId) {
        this.type = "@GAMES_REMOVE_GAME";
        this.payload = gameId;
    }

    public Integer getPayload() {
        return payload;
    }
}

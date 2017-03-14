package store_actions;

import java.util.UUID;

/**
 * Created by giorgiopea on 14/03/17.
 */
public class GameAddPlayerActionPayload {

    private UUID uuid;
    private Integer gameId;
    private String playerName;

    public GameAddPlayerActionPayload(UUID uuid, Integer gameId, String playerName) {
        this.uuid = uuid;
        this.gameId = gameId;
        this.playerName = playerName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
}

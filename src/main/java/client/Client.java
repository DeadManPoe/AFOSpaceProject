package client;

import common.Coordinate;
import common.Sector;
import it.polimi.ingsw.cg_19.GameMap;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerType;

/**
 * Represents a client in the client server communication layer of the
 * application
 *
 * @author Andrea Sessa
 * @author Giorgio Pea
 */
public class Client {
    private Player player;
    private GameMap gameMap;
    private boolean isGameStarted;
    private boolean isMyTurn;

    private static Client instance = new Client();


    public static Client getInstance() {
        return instance;
    }

    private Client() {
        this.isGameStarted = false;
        this.isMyTurn = false;
    }

    public void setGameStarted(boolean gameStarted) {
        this.isGameStarted = gameStarted;
    }

    public GameMap getGameMap() {
        return this.gameMap;
    }

    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }

    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    /**
     * Sets the client's current sector to the given coordinates and
     * stores the fact that the client has moved.
     * @param coordinate The coordinate of the sector the client has to move to.
     */
    public void move(Coordinate coordinate) {
        Sector targetSector = this.getGameMap().getSectorByCoords(coordinate);
        this.player.setHasMoved(true);
        this.player.setCurrentSector(targetSector);
    }

    /**
     * Sets client's properties to reflect the ending of a game turn
     */
    public void endTurn() {
        this.player.setSedated(false);
        this.player.setAdrenalined(false);
        this.player.setHasMoved(false);
        this.isMyTurn = false;
    }


    /**
     * Sets client's properties to reflect a teleport object usage
     */
    public void teleport() {
        if (this.player.getPlayerToken().getPlayerType().equals(PlayerType.HUMAN)) {
            this.player.setCurrentSector(this.gameMap.getHumanSector());
        } else {
            this.player.setCurrentSector(this.gameMap.getAlienSector());
        }

    }

    public Player getPlayer() {
        return this.player;
    }
    public void setPlayer(String playerName) {
        this.player = new Player(playerName);
    }

}

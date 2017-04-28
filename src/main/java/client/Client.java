package client;

import it.polimi.ingsw.cg_19.GameMap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;

import common.Action;
import common.AttackObjectCard;
import common.Coordinate;
import common.DefenseObjectCard;
import common.DiscardAction;
import common.GamePublicData;
import common.GlobalNoiseSectorCard;
import common.LightsObjectCard;
import common.MoveAttackAction;
import common.ObjectCard;
import common.PSClientNotification;
import common.PlayerToken;
import common.PrivateDeck;
import common.RRClientNotification;
import common.Sector;
import common.SectorCard;
import common.TeleportObjectCard;
import common.UseObjAction;
import common.UseSectorCardAction;
import it.polimi.ingsw.cg_19.Player;
import it.polimi.ingsw.cg_19.PlayerState;
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
    private boolean isGameEnded;

    private static Client instance = new Client();


    public static Client getInstance() {
        return instance;
    }

    private Client() {
        this.isGameStarted = false;
        this.isMyTurn = false;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.isGameStarted = gameStarted;
    }

    public boolean isGameEnded() {
        return this.isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.isGameEnded = gameEnded;
    }

    public GameMap getGameMap() {
        return this.gameMap;
    }


    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }


    public boolean getIsMyTurn() {
        return isMyTurn;
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

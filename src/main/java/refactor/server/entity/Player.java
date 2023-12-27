package refactor.server.entity;

import java.io.Serializable;

public class Player implements Serializable {
    public String name;
    Player(Player player) {
        name = player.name;
    }
    public Player(String playerName) {
        name = playerName;
    }
}

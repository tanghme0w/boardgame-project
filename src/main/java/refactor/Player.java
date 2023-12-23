package refactor;

import java.io.Serializable;

public class Player implements Serializable {
    public String name;
    Player(Player player) {
        name = player.name;
    }
    Player(String playerName) {
        name = playerName;
    }
}

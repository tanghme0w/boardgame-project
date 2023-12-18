package refactor;

public class Player {
    String name;
    Player(Player player) {
        name = player.name;
    }
    Player(String playerName) {
        name = playerName;
    }
}

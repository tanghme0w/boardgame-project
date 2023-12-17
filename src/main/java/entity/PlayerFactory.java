package entity;

public class PlayerFactory {
    //create player, but do not assign identity
    public static Player createPlayer(String name) {
        return new Player(name, null);
    }
    //create player and assign identity
    public static Player createPlayer(String name, Identity identity) {
        return new Player(name, identity);
    }
}

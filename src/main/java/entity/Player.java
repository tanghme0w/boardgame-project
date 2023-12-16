package entity;

public class Player {
    String name;
    Identity id = Identity.RANDOM;
    Integer withdraw_count;
    Player(String name, Identity id) {
        this.name = name;
        this.id = id;
    }
    Player(String name) {
        this.name = name;
    }
}

package entity;

public class Player {
    String name;
    Identity id = null;
    Integer withdraw_count;
    Player(String name, Identity id) {
        this.name = name;
        this.id = id;
    }
}

package entity;

public class Player {
    public String name;
    public Identity id = null;
    public Integer withdraw_count;
    Player(String name, Identity id) {
        this.name = name;
        this.id = id;
    }
}

package refactor.server.entity;

import globals.StoneColor;

import java.io.Serializable;

public class Identity implements Serializable {
    public Player player;
    public StoneColor stoneColor;
    public Integer withdrawCount;
    public boolean hasAbstained;
    //deep copy construct
    Identity(Identity id) {
        this.player = new Player(id.player);
        this.stoneColor = id.stoneColor;
        this.withdrawCount = id.withdrawCount;
        this.hasAbstained = id.hasAbstained;
    }
    Identity(StoneColor ct) {
        player = null;
        stoneColor = ct;
        withdrawCount = 0;
        hasAbstained = false;
    }
}

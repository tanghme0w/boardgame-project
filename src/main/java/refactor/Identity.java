package refactor;

import globals.ChessType;

public class Identity {
    Player player;
    ChessType chessType;
    Integer withdrawCount;
    boolean hasAbstained;
    //deep copy construct
    Identity(Identity id) {
        this.player = new Player(id.player);
        this.chessType = id.chessType;
        this.withdrawCount = id.withdrawCount;
        this.hasAbstained = id.hasAbstained;
    }
    Identity(ChessType ct) {
        player = null;
        chessType = ct;
        withdrawCount = 0;
        hasAbstained = false;
    }
}

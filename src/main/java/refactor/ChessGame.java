package refactor;

import globals.ChessType;

import java.io.Serializable;
import java.util.*;

public class ChessGame implements Serializable {
    Board board;
    List<Identity> identities;
    Map<Player, Identity> playerIdentityMap;
    Identity currentActingIdentity;
    Stack<Board> boardHistory;
    Ruleset ruleset;

    //start brand-new game
    ChessGame(Integer boardSizeX, Integer boardSizeY, Ruleset ruleset) {
        //initialize game board
        board = new Board(boardSizeX, boardSizeY);
        //initialize identities and identity map
        identities = new ArrayList<>();
        identities.add(new Identity(ChessType.BLACK));
        identities.add(new Identity(ChessType.WHITE));
        this.playerIdentityMap = new HashMap<>();
        this.boardHistory = new Stack<>();
        this.ruleset = ruleset;
    }

    //modify game status
    public void switchTurn() {
        int currentIdentityIndex = identities.indexOf(currentActingIdentity);
        int nextIdentityIndex = (currentIdentityIndex + 1) % identities.size();
        currentActingIdentity = identities.get(nextIdentityIndex);
        board.nextChessType = currentActingIdentity.chessType;
    }
}
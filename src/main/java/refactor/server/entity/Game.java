package refactor.server.entity;

import globals.StoneColor;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    public Board board;
    public List<Identity> identities;
    public Map<Player, Identity> playerIdentityMap;
    public Identity currentActingIdentity;
    public Stack<Board> boardHistory;
    public Ruleset ruleset;

    //start brand-new game
    public Game(Integer boardSizeX, Integer boardSizeY, Ruleset ruleset) {
        //initialize game board
        board = new Board(boardSizeX, boardSizeY);
        ruleset.init(board);
        //initialize identities and identity map
        identities = new ArrayList<>();
        for (StoneColor stoneColor: StoneColor.values()) {
            identities.add(new Identity(stoneColor));
        }
        this.playerIdentityMap = new HashMap<>();
        this.boardHistory = new Stack<>();
        this.ruleset = ruleset;
    }

    //modify game status
    public void switchTurn() {
        int nextIdentityIndex;
        if (currentActingIdentity == null) {
            nextIdentityIndex = 0;
        } else nextIdentityIndex = (identities.indexOf(currentActingIdentity) + 1) % identities.size();
        currentActingIdentity = identities.get(nextIdentityIndex);
        board.actingStoneColor = currentActingIdentity.stoneColor;
    }
}
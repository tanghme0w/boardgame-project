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
        //initialize identities and identity map
        identities = new ArrayList<>();
        identities.add(new Identity(StoneColor.BLACK));
        identities.add(new Identity(StoneColor.WHITE));
        this.playerIdentityMap = new HashMap<>();
        this.boardHistory = new Stack<>();
        this.ruleset = ruleset;
    }

    //modify game status
    public void switchTurn() {
        int currentIdentityIndex = identities.indexOf(currentActingIdentity);
        int nextIdentityIndex = (currentIdentityIndex + 1) % identities.size();
        currentActingIdentity = identities.get(nextIdentityIndex);
        board.actingStoneColor = currentActingIdentity.stoneColor;
    }
}
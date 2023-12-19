package refactor;

import java.util.ArrayList;
import java.util.List;

public class GameMemento {
    Board board;
    List<Identity> identities;
    Identity currentActingIdentity;
    List<Board> boardHistory;
    List<Step> stepHistory;
    Ruleset ruleset;
    GameMemento(Board board,
                List<Identity> identities,
                Identity currentActingIdentity,
                List<Board> boardHistory,
                List<Step> stepHistory,
                Ruleset ruleset) {
        //copy board
        this.board = board == null ? null : new Board(board);
        //copy identity list
        if (identities == null) this.identities = null;
        else {
            this.identities = new ArrayList<>(identities.size());
            for (Identity id: identities) {
                this.identities.add(new Identity(id));
            }
        }
        //copy current acting identity
        this.currentActingIdentity = currentActingIdentity == null ? null : new Identity(currentActingIdentity);
        //copy board history
        if (boardHistory == null) this.boardHistory = null;
        else {
            this.boardHistory = new ArrayList<>(boardHistory.size());
            for (Board bd: boardHistory) {
                this.boardHistory.add(new Board(bd));
            }
        }
        //copy move history
        if (stepHistory == null) this.stepHistory = null;
        else {
            this.stepHistory = new ArrayList<>(stepHistory.size());
            for (Step step: stepHistory) {
                this.stepHistory.add(new Step(step));
            }
        }
        //copy deprecated.ruleset
        this.ruleset = ruleset;
    }
}

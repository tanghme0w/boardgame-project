package refactor;

import java.util.List;

public class RenderVO {
    List<Identity> identities;
    Board board;
    List<String> log;
    RenderVO(List<Identity> identities, Board board, List<String> log) {
        this.identities = identities;
        this.board = board;
        this.log = log;
    }
}

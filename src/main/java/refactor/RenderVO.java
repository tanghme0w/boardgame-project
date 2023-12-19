package refactor;

import java.util.List;

public class RenderVO {
    public List<Identity> identities;
    public Identity currentActingIdentity;
    public Board board;
    public List<String> log;
    RenderVO(List<Identity> identities, Identity currentActingIdentity, Board board, List<String> log) {
        this.identities = identities;
        this.currentActingIdentity = currentActingIdentity;
        this.board = board;
        this.log = log;
    }
}

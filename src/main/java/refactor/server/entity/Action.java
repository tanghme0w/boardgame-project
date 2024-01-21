package refactor.server.entity;

//Possible actions at a certain period of a game
public class Action {
    public Position position;
    public Integer score;
    Action(Position position, Integer score) {
        this.position = position;
        this.score = score;
    }
}

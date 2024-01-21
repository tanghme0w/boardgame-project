package refactor.server.entity;

//Possible actions at a certain period of a game
public class Action {
    public Position position;
    public Double score;
    Action(Position position, Double score) {
        this.position = position;
        this.score = score;
    }
}

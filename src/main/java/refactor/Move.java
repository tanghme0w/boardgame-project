package refactor;

public class Move {
    Identity identity;
    Position position;
    //deep copy construct
    Move(Move move) {
        identity = new Identity(move.identity);
        position = new Position(move.position.x, move.position.y);
    }
    Move(Identity id, Position pose) {
        identity = id;
        position = pose;
    }
}

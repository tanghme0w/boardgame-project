package entity;

import java.util.List;

public class Move {
    Piece piece;
    List<Integer> position;

    public List<Integer> getPosition() {
        return position;
    }

    public Piece getPlayerId() {
        return piece;
    }
}

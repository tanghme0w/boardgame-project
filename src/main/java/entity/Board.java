package entity;

import java.util.List;

public class Board {
    Integer size;
    List<List<Piece>> array;
    Player currentActingPlayer;
    Board(Integer size) {
        this.size = size;
    }
}

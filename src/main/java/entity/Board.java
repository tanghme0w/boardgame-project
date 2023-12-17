package entity;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public Integer size;
    List<List<Piece>> array = new ArrayList<>();
    Player currentActingPlayer;
    Board(Integer size) {
        this.size = size;
    }
}

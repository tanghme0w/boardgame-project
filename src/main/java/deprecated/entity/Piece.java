package deprecated.entity;

public class Piece {
    Integer player_id;
    Integer move_id;
    Piece(Integer player_id, Integer move_id) {
        this.player_id = player_id;
        this.move_id = move_id;
    }
    public Integer getPlayer_id() {
        return player_id;
    }
}

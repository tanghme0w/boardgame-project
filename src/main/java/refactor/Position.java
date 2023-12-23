package refactor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Position implements Serializable {
    Integer x;
    Integer y;
    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    List<Position> connectedPositions() {
        List<Position> dcp = new ArrayList<>();
        dcp.add(up());
        dcp.add(down());
        dcp.add(left());
        dcp.add(right());
        return dcp;
    }
    public Position up() {
        return new Position(this.x - 1, this.y);
    }
    public Position down() {
        return new Position(this.x + 1, this.y);
    }
    public Position left() {
        return new Position(this.x, this.y - 1);
    }
    public Position right() {
        return new Position(this.x, this.y + 1);
    }
    public Position upperLeft() {
        return new Position(this.x - 1, this.y - 1);
    }
    public Position upperRight() {
        return new Position(this.x - 1, this.y + 1);
    }
    public Position lowerLeft() {
        return new Position(this.x + 1, this.y - 1);
    }
    public Position lowerRight() {
        return new Position(this.x + 1, this.y + 1);
    }
    public Position offset(Integer x, Integer y) {
        return new Position(this.x + x, this.y +y);
    }
}

package globals;

public enum ChessType {
    BLACK, WHITE;
    public String string() {
        return switch (this) {
            case BLACK -> "black";
            case WHITE -> "white";
        };
    }
}

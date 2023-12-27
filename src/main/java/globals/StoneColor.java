package globals;

public enum StoneColor {
    BLACK, WHITE;
    public String string() {
        return switch (this) {
            case BLACK -> "black";
            case WHITE -> "white";
        };
    }
}

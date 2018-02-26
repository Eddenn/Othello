package app.model;

public enum Tile {
    EMPTY,
    WHITE,
    BLACK;

    public static Tile opposite(Tile t) {
        if(t == Tile.WHITE) return Tile.BLACK;
        if(t == Tile.BLACK) return Tile.WHITE;
        return t;
    }

    public String toString() {
        if(this == Tile.EMPTY) return " ";
        if(this == Tile.WHITE) return "W";
        if(this == Tile.BLACK) return "B";
        return this.name();
    }
}

package app.model;

public enum Direction {
    UP(0,-1),
    DOWN(0,1),
    LEFT(-1,0),
    RIGHT(1,0),
    UP_RIGHT(1,-1),
    UP_LEFT(-1,-1),
    DOWN_RIGHT(1,1),
    DOWN_LEFT(-1,1);

    private int dx, dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public static Direction getDirection(int dx, int dy) {
        for (Direction d : values()) {
            if( d.dx == dx && d.dy == dy) {
                return d;
            }
        }
        return null;
    }
}

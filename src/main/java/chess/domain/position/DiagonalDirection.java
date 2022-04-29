package chess.domain.position;

import java.util.Arrays;

public enum DiagonalDirection {

    NORTHWEST(-1, 1),
    SOUTHWEST(-1, -1),
    NORTHEAST(1, 1),
    SOUTHEAST(1, -1),
    ;

    private final int dx;
    private final int dy;

    DiagonalDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    private static int toDegree(int number) {
        return Integer.compare(number, 0);
    }

    public static DiagonalDirection of(int fileSubtract, int rankSubtract) {
        int dx = toDegree(fileSubtract);
        int dy = toDegree(rankSubtract);
        return Arrays.stream(values())
                .filter(value -> value.dx == dx)
                .filter(value -> value.dy == dy)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 해당하는 방향이 없습니다."));
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}

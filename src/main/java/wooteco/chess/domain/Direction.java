package wooteco.chess.domain;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.exception.IllegalMoveException;

public enum Direction {
    NORTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0),
    SOUTH(0, -1),

    NN(0, 2),
    SS(0, -2),

    NE(1, 1),
    NW(-1, 1),
    SE(1, -1),
    SW(-1, -1),

    NNE(1, 2),
    NEE(2, 1),
    NNW(-1, 2),
    NWW(-2, 1),
    SSE(1, -2),
    SEE(2, -1),
    SSW(-1, -2),
    SWW(-2, -1);

    private static final String INVALID_DIRECTION = "올바르지 않은 방향입니다.";
    private int xDegree;
    private int yDegree;

    Direction(int xDegree, int yDegree) {
        this.xDegree = xDegree;
        this.yDegree = yDegree;
    }

    public static Direction of(int x, int y) {
        return Arrays.stream(Direction.values()).filter(d -> d.xDegree == x && d.yDegree == y)
            .findFirst()
            .orElseThrow(() -> new IllegalMoveException(INVALID_DIRECTION));
    }

    public boolean isDiagonal() {
        List<Direction> diagonalDirection = Arrays.asList(NW, NE, SW, SE);
        return diagonalDirection.contains(this);
    }

    public boolean isForwardDouble() {
        return (this == NN || this == SS);
    }

    public boolean isForwardForPawn() {
        List<Direction> forwardDirection = Arrays.asList(NORTH, NN, SOUTH, SS);
        return forwardDirection.contains(this);
    }

    public int getYDegree() {
        return yDegree;
    }

    public int getXDegree() {
        return xDegree;
    }
}

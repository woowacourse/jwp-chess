package chess.database.entity;

import chess.domain.board.Point;

public class PointEntity {

    private final Integer horizontalIndex;
    private final Integer verticalIndex;

    public PointEntity(Integer horizontalIndex, Integer verticalIndex) {
        this.horizontalIndex = horizontalIndex;
        this.verticalIndex = verticalIndex;
    }

    public static PointEntity from(Point point) {
        return new PointEntity(point.getHorizontal(), point.getVertical());
    }

    public Integer getHorizontalIndex() {
        return horizontalIndex;
    }

    public Integer getVerticalIndex() {
        return verticalIndex;
    }
}

package wooteco.chess.domains.position;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Position implements Comparable<Position> {
    private static final Map<String, Position> cachedPositions;

    static {
        Map<String, Position> positions = new HashMap<>();
        for (Column x : Column.values()) {
            positions.putAll(createPositionsByColumn(x));
        }
        cachedPositions = positions;
    }

    private Column x;
    private Row y;

    public Position(Column x, Row y) {
        this.x = x;
        this.y = y;
    }

    private static Map<String, Position> createPositionsByColumn(Column x) {
        Map<String, Position> positions = new HashMap<>();
        for (Row y : Row.values()) {
            String positionName = String.valueOf(x.getColumn()) + y.getRow();
            Position position = new Position(x, y);
            positions.put(positionName, position);
        }
        return positions;
    }

    public static List<Position> fromRow(Row row) {
        String rowName = String.valueOf(row.getRow());
        return Arrays.stream(Column.values())
                .map(column -> column.getColumn() + rowName)
                .map(cachedPositions::get)
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<Position> fromColumn(Column column) {
        String columnName = String.valueOf(column.getColumn());
        return Arrays.stream(Row.values())
                .map(row -> columnName + row.getRow())
                .map(cachedPositions::get)
                .sorted()
                .collect(Collectors.toList());
    }

    public static Position ofPositionName(String positionName) {
        return cachedPositions.get(positionName);
    }

    public static Stream<Position> stream() {
        return cachedPositions.values().stream();
    }

    public int xGapBetween(Position target) {
        return this.x.columnGap(target.x);
    }

    public int yGapBetween(Position target) {
        return this.y.rowGap(target.y);
    }

    public boolean isSameX(Position target) {
        return this.x == target.x;
    }

    public boolean isSameY(Position target) {
        return this.y == target.y;
    }

    public boolean isRow(Row row) {
        return this.y == row;
    }

    public boolean isColumn(Column column) {
        return this.x == column;
    }

    public List<Position> findRoute(Position target) {
        ArrayList<Position> route = new ArrayList<>();
        Direction direction = findDirection(target);

        Column x = this.x.moveBy(direction.getxGap());
        Row y = this.y.moveBy(direction.getyGap());

        while (x != target.x || y != target.y) {
            route.add(new Position(x, y));
            x = x.moveBy(direction.getxGap());
            y = y.moveBy(direction.getyGap());
        }

        return route;
    }

    public Direction findDirection(Position target) {
        int yGap = this.yGapBetween(target);
        int xGap = this.xGapBetween(target);

        return Direction.findDirection(xGap, yGap);
    }

    public String name() {
        return String.valueOf(this.x.getColumn()) + this.y.getRow();
    }

    @Override
    public int compareTo(Position o) {
        if (y.isBiggerThan(o.y)) {
            return -1;
        }
        if (y == o.y && x.isSmallerThan(o.x)) {
            return -1;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

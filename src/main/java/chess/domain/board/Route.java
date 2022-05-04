package chess.domain.board;

import java.util.List;

import chess.domain.piece.move.Direction;

public class Route {

    private static final int ARGUMENT_SIZE = 2;
    private static final int ARGUMENT_SOURCE_INDEX = 0;
    private static final int ARGUMENT_DESTINATION_INDEX = 1;

    private final Point source;
    private final Point destination;

    public Route(Point source, Point destination) {
        this.source = source;
        this.destination = destination;
    }

    public static Route of(String source, String destination) {
        return new Route(Point.of(source), Point.of(destination));
    }

    public static Route of(List<String> arguments) {
        validateSize(arguments);
        return of(arguments.get(ARGUMENT_SOURCE_INDEX), arguments.get(ARGUMENT_DESTINATION_INDEX));
    }

    private static void validateSize(List<String> arguments) {
        if (arguments.size() != ARGUMENT_SIZE) {
            throw new IllegalArgumentException("[ERROR] 출발지와 도착지가 올바르지 않습니다.(move a1 a2)");
        }
    }

    public int subtractVertical() {
        return destination.subtractVertical(source);
    }

    public int subtractHorizontal() {
        return destination.subtractHorizontal(source);
    }

    public boolean isArrived() {
        return source.equals(destination);
    }

    public Point getSource() {
        return source;
    }

    public Point getDestination() {
        return destination;
    }

    public Route moveSource(Direction direction) {
        Point sourceNext = this.source.next(direction);
        return new Route(sourceNext, destination);
    }
}

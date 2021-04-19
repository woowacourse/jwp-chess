package chess.domain.board;

import java.util.List;

public class Path {
    private Path() {
    }

    static Direction findDirection(ChessBoard chessBoard, Position sourcePosition, Position targetPosition) {
        List<Direction> directions = chessBoard.getCandidateDirections(sourcePosition);
        return directions.stream()
                .filter(direction -> isMovableDirection(sourcePosition, targetPosition, direction))
                .findFirst()
                .orElseThrow(NotMovableDirectionException::new);
    }

    private static boolean isMovableDirection(Position sourcePosition, Position targetPosition, Direction direction) {
        Position currentPosition = sourcePosition;
        while (currentPosition.hasNextPosition(direction)) {
            currentPosition = currentPosition.nextPosition(direction);
            if (currentPosition.equals(targetPosition)) {
                return true;
            }
        }
        return false;
    }
}

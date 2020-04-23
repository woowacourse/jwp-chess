package wooteco.chess.domain.strategy;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class MultipleMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> possiblePositions(Board board, Piece piece, Position position) {
        List<Position> possiblePositions = new ArrayList<>();

        for (Direction direction : piece.getDirections()) {
            Position currentPosition = position;
            while (currentPosition.isNextPositionValidForward(direction)) {
                Position nextPosition = currentPosition.moveBy(direction);
                Piece nextPiece = board.findBy(nextPosition);

                if (!nextPiece.isBlank()) {
                    if (piece.isOtherTeam(nextPiece)) {
                        possiblePositions.add(nextPosition);
                    }
                    break;
                }
                possiblePositions.add(nextPosition);
                currentPosition = nextPosition;
            }
        }
        return possiblePositions;
    }
}

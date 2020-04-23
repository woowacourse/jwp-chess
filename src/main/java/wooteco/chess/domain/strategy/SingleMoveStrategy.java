package wooteco.chess.domain.strategy;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.util.Direction;

import java.util.ArrayList;
import java.util.List;

public class SingleMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> possiblePositions(final Board board, final Piece piece, final Position position) {
        List<Position> possiblePositions = new ArrayList<>();

        for (Direction direction : piece.getDirections()) {
            if (position.isNextPositionValidForward(direction)) {
                Position nextPosition = position.moveBy(direction);
                Piece nextPiece = board.findBy(nextPosition);

                if (nextPiece.isBlank()) {
                    possiblePositions.add(nextPosition);
                } else if (piece.isOtherTeam(nextPiece)) {
                    possiblePositions.add(nextPosition);
                }
            }
        }
        return possiblePositions;
    }
}

package chess.domain.piece.strategy;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import chess.domain.position.Target;
import chess.exception.ChessException;
import chess.exception.ErrorInformation;

import java.util.List;
import java.util.Optional;

public interface MoveStrategy {
    default void checkTarget(final Target target, final List<Position> positions) {
        if (!positions.contains(target.getPosition())) {
            throw new ChessException(ErrorInformation.INVALID_MOVE);
        }
    }

    default Position findTargetPosition(final int rank, final int file) {
        return Position.valueOf(rank, file);
    }

    default Optional<Piece> findPiece(final Pieces pieces, final Position position) {
        return pieces.findPiece(position);
    }
}

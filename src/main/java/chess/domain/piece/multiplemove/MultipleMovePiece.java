package chess.domain.piece.multiplemove;

import chess.domain.piece.NonBlankPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.util.List;

public abstract class MultipleMovePiece extends NonBlankPiece {

    protected MultipleMovePiece(final Team team, final Square square) {
        super(team, square);
    }

    @Override
    protected boolean isMovable(final Piece piece) {
        final Square to = piece.getSquare();
        final List<Direction> directions = getAvailableDirections();
        final Direction direction = Direction.findDirection(this.square, to);
        return directions.contains(direction);
    }
}

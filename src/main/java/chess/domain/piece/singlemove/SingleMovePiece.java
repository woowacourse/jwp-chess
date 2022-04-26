package chess.domain.piece.singlemove;

import chess.domain.piece.NonBlankPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.util.List;

public abstract class SingleMovePiece extends NonBlankPiece {

    protected SingleMovePiece(final Team team, final Square square) {
        super(team, square);
    }

    @Override
    protected boolean isMovable(final Piece piece) {
        final Square to = piece.getSquare();
        final List<Direction> directions = getAvailableDirections();

        return directions.stream()
                .filter(direction -> this.square.isExist(direction))
                .map(direction -> this.square.next(direction))
                .anyMatch(to::equals);
    }
}

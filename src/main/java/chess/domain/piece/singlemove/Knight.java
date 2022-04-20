package chess.domain.piece.singlemove;

import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.util.List;

public class Knight extends SingleMovePiece {

    public Knight(final Team team, final Square square) {
        super(team, square);
    }

    @Override
    public Piece moveTo(final Square to) {
        return new Knight(getTeam(), to);
    }

    @Override
    public List<Direction> getAvailableDirections() {
        return Direction.getKnightDirections();
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KNIGHT;
    }
}

package chess.domain.piece.singlemove;

import java.util.List;

import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

public class King extends SingleMovePiece {

    public King(final Team team, final Square square) {
        super(team, square);
    }

    @Override
    public Piece moveTo(final Square to) {
        return new King(getTeam(), to);
    }

    @Override
    public List<Direction> getAvailableDirections() {
        return Direction.getEveryDirections();
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
    }

    @Override
    public boolean isKing() {
        return true;
    }
}

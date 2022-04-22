package chess.domain.piece.multiplemove;

import java.util.List;

import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

public class Rook extends MultipleMovePiece {

    public Rook(final Team team, final Square square) {
        super(team, square);
    }

    @Override
    public Piece moveTo(final Square to) {
        return new Rook(getTeam(), to);
    }

    @Override
    public List<Direction> getAvailableDirections() {
        return Direction.getVerticalAndHorizontalDirections();
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.ROOK;
    }
}

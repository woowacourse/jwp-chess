package chess.domain.piece.pawn;

import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.util.List;

public class WhitePawn extends Pawn {

    public WhitePawn(final Team team, final Square square) {
        super(team, square);
    }

    @Override
    public List<Direction> getAvailableDirections() {
        return Direction.getWhitePawnDirections();
    }

    @Override
    protected boolean isPawnInitial() {
        return square.isWhitePawnInitial();
    }

    @Override
    public List<Direction> getPawnAttackDirections() {
        return Direction.getWhitePawnAttackDirections();
    }
}

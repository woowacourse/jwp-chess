package chess.domain.piece;

import static chess.domain.direction.Direction.*;

import java.util.List;

import chess.domain.direction.Direction;
import chess.domain.piece.movestrategy.MoveStrategy;
import chess.domain.piece.movestrategy.PawnMoveStrategy;

public final class Pawn extends Piece {

    Pawn(Symbol symbol, Team team) {
        super(symbol, team);
    }

    @Override
    public List<Direction> direction() {
        if (team.isBlack()) {
            return List.of(DOWN, DOWN_LEFT, DOWN_RIGHT);
        }
        return List.of(UP, UP_LEFT, UP_RIGHT);
    }

    @Override
    public MoveStrategy moveStrategy() {
        return new PawnMoveStrategy();
    }
}

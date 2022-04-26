package chess.domain.piece;

import static chess.domain.direction.Direction.*;

import java.util.List;

import chess.domain.direction.Direction;
import chess.domain.piece.movestrategy.MoveStrategy;
import chess.domain.piece.movestrategy.RepeatableMoveStrategy;

public final class Rook extends Piece {

    Rook(Symbol symbol, Team team) {
        super(symbol, team);
    }

    @Override
    public List<Direction> direction() {
        return List.of(UP, DOWN, LEFT, RIGHT);
    }

    @Override
    public MoveStrategy moveStrategy() {
        return new RepeatableMoveStrategy();
    }
}

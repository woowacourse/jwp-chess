package chess.domain.piece;

import static chess.domain.direction.Direction.*;

import java.util.List;

import chess.domain.direction.Direction;
import chess.domain.piece.movestrategy.MoveStrategy;
import chess.domain.piece.movestrategy.NonRepeatableMoveStrategy;

public final class Knight extends Piece {

    Knight(Symbol symbol, Team team) {
        super(symbol, team);
    }

    @Override
    public List<Direction> direction() {
        return List.of(UP_UP_LEFT, UP_UP_RIGHT, DOWN_DOWN_LEFT, DOWN_DOWN_RIGHT,
                LEFT_LEFT_UP, LEFT_LEFT_DOWN, RIGHT_RIGHT_UP, RIGHT_RIGHT_DOWN);
    }

    @Override
    public MoveStrategy moveStrategy() {
        return new NonRepeatableMoveStrategy();
    }
}

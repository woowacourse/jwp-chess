package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;

import java.util.List;

public class King extends FixedDistancePiece {
    public King(Color color, Position position) {
        super(color, position);
        if (color.equals(Color.NO_COLOR)) {
            throw new MovablePieceColorException();
        }
        this.type = Type.KING;
    }

    @Override
    public List<Direction> directions() {
        return Direction.everyDirection();
    }
}

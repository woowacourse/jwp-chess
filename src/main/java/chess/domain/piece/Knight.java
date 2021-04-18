package chess.domain.piece;

import chess.domain.board.Direction;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;

import java.util.List;

public class Knight extends FixedDistancePiece {
    public Knight(Color color, Position position) {
        super(color, position);
        if (color.equals(Color.NO_COLOR)) {
            throw new MovablePieceColorException();
        }
        this.type = Type.KNIGHT;
    }

    @Override
    public List<Direction> directions() {
        return Direction.knightDirection();
    }
}

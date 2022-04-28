package chess.piece;

import chess.domain.MovingPosition;
import chess.domain.Position;

import java.util.List;

public class Rook extends Piece {

    public Rook(Color color) {
        super(Type.ROOK, color);
    }

    @Override
    public boolean isMovable(MovingPosition movingPosition) {
        return movingPosition.isLinear();
    }

    @Override
    public List<Position> computeMiddlePosition(MovingPosition movingPosition) {
        return movingPosition.computeLinearMiddle();
    }

}



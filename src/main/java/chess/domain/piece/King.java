package chess.domain.piece;

import java.util.Map;

import chess.domain.board.Score;
import chess.domain.position.Direction;
import chess.domain.position.Position;

public class King extends Piece {

    private static final String NAME = "k";
    private static final Score SCORE = new Score(0);

    public King(Color color) {
        super(NAME, color, SCORE);
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public boolean canMove(Map<Position, Piece> board, Position source, Position target) {
        return Direction.everyDirection()
            .stream()
            .anyMatch(direction -> source.sum(direction).equals(target));
    }

}

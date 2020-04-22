package wooteco.chess.domain.piece;

import java.util.Arrays;

import wooteco.chess.domain.Position;
import wooteco.chess.exception.IllegalMoveException;

public class Bishop extends Piece {
    public Bishop(Position position, Team team) {
        super(position, team);
        this.representation = Arrays.asList('♗', '♝');
        this.score = PieceRule.BISHOP.getScore();
    }

    @Override
    public void validateMove(Position destination) {
        if (this.position.isNonDiagonalDirection(destination)) {
            throw new IllegalMoveException(ILLEGAL_MOVE);
        }
    }
}

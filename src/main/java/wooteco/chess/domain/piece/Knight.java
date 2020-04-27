package wooteco.chess.domain.piece;

import static wooteco.chess.domain.Direction.*;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.Direction;
import wooteco.chess.domain.Position;
import wooteco.chess.exception.IllegalMoveException;

public class Knight extends Piece {
    private static List<Direction> possibleDirections = Arrays.asList(NNE, NEE, NNW, NWW, SSE, SEE, SSW, SWW);

    public Knight(Position position, Team team) {
        super(position, team);
        this.representation = Arrays.asList('♘', '♞');
        this.score = PieceRule.KNIGHT.getScore();
    }

    @Override
    public void validateMove(Position destination) {
        Direction direction = position.calculateDirection(destination);
        if (!possibleDirections.contains(direction)) {
            throw new IllegalMoveException(ILLEGAL_MOVE);
        }
    }
}

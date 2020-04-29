package wooteco.chess.domain.piece;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.Direction;
import wooteco.chess.domain.Position;
import wooteco.chess.exception.IllegalMoveException;

import static wooteco.chess.domain.Direction.*;

public class King extends Piece {
    private static List<Direction> possibleDirections = Arrays.asList(NORTH, EAST, WEST,
        SOUTH, NE, NW, SE, SW);

    public King(Position position, Team team) {
        super(position, team);
        this.representation = Arrays.asList('♔', '♚');
        this.score = PieceRule.KING.getScore();
    }

    public void validateMove(Position destination) {
        Direction direction = position.calculateDirection(destination);
        if (!possibleDirections.contains(direction)) {
            throw new IllegalMoveException(ILLEGAL_MOVE);
        }
    }
}

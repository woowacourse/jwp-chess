package wooteco.chess.domain.piece;

import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.Direction;
import wooteco.chess.domain.Position;
import wooteco.chess.exception.IllegalMoveException;

public class Pawn extends Piece {
    public static final List<Direction> LOWER_TEAM_POSSIBLE_DIRECTIONS = Arrays.asList(Direction.NORTH,
        Direction.NN, Direction.NE, Direction.NW);
    public static final List<Direction> UPPER_TEAM_POSSIBLE_DIRECTIONS = Arrays.asList(Direction.SOUTH,
        Direction.SS, Direction.SE, Direction.SW);

    public Pawn(Position position, Team team) {
        super(position, team);
        this.representation = Arrays.asList('♙', '♟');
        this.score = PieceRule.PAWN.getScore();
    }

    @Override
    public void validateMove(Position destination) {
        if (this.team == Team.WHITE) {
            validateMoveByDirection(destination, LOWER_TEAM_POSSIBLE_DIRECTIONS);
        }
        if (this.team == Team.BLACK) {
            validateMoveByDirection(destination, UPPER_TEAM_POSSIBLE_DIRECTIONS);
        }
    }

    private void validateMoveByDirection(Position destination, List<Direction> possibleDirections) {
        Direction direction = this.position.calculateDirection(destination);
        if (!this.position.isPawnInOriginalPosition() && direction.isForwardDouble()) {
            throw new IllegalMoveException(ILLEGAL_MOVE);
        }
        if (!possibleDirections.contains(direction)) {
            throw new IllegalMoveException(ILLEGAL_MOVE);
        }
    }

    public boolean isInSameColumn(Pawn pawn) {
        return this.position.isInSameColumn(pawn.position);
    }
}

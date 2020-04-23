package chess.domain.pieces;

import chess.domain.pieces.exceptions.CanNotMoveException;
import chess.domain.coordinate.Direction;
import chess.domain.coordinate.Coordinate;
import chess.domain.team.Team;

public class Rook extends Piece {

    public Rook(Team team, Coordinate coordinate) {
        super(PieceType.ROOK, team, coordinate);
    }

    @Override
    public Piece move(Coordinate afterCoordinate) {
        return new Rook(getTeam(), afterCoordinate);
    }

    @Override
    public void validateMoveDirection(Direction direction) {
        if (direction.isNotStraight()) {
            throw new CanNotMoveException("Rook은 일직선으로만 이동할 수 있습니다.");
        }
    }
}

package chess.model.piece;

import chess.model.square.Direction;

import java.util.List;

public class Bishop extends LinearMovingPiece {

    private static final double POINT = 3;

    public Bishop(Team team) {
        super(team);
    }

    public Bishop(int id, Team team, int squareId) {
        super(id, team, squareId);
    }

    @Override
    public String name() {
        return PieceType.b.name();
    }

    @Override
    public boolean isNotEmpty() {
        return true;
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public List<Direction> getDirection() {
        return List.of(Direction.NORTH_WEST, Direction.SOUTH_WEST, Direction.NORTH_EAST, Direction.SOUTH_EAST);
    }

    @Override
    public double getPoint() {
        return POINT;
    }
}

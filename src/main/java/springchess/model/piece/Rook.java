package springchess.model.piece;

import springchess.model.square.Direction;

import java.util.List;

public class Rook extends LinearMovingPiece {

    private static final double POINT = 5;

    public Rook(Team team) {
        super(team);
    }

    public Rook(int id, Team team, int squareId) {
        super(id, team, squareId);
    }

    @Override
    public String name() {
        return PieceType.r.name();
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
        return List.of(
                Direction.EAST,
                Direction.WEST,
                Direction.SOUTH,
                Direction.NORTH
        );
    }

    @Override
    public double getPoint() {
        return POINT;
    }
}

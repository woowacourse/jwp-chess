package springchess.model.piece;

import springchess.model.square.Direction;

import java.util.List;

public class Pawn extends PawnMovingPiece {

    private static final double POINT = 1;

    public Pawn(Team team) {
        super(team);
    }

    public Pawn(int id, Team team, int squareId) {
        super(id, team, squareId);
    }

    @Override
    public String name() {
        return PieceType.p.name();
    }

    @Override
    public boolean isNotEmpty() {
        return true;
    }

    @Override
    public boolean isPawn() {
        return true;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public List<Direction> getDirection() {
        if (team.isBlack()) {
            return List.of(Direction.SOUTH);
        }
        return List.of(Direction.NORTH);
    }

    @Override
    public double getPoint() {
        return POINT;
    }
}

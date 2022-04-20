package springchess.model.piece;

import springchess.model.board.ConsoleBoard;
import springchess.model.square.Direction;
import springchess.model.square.Square;

import java.util.List;

public abstract class Piece {

    private final int id;
    protected final Team team;
    private final int squareId;

    protected Piece(int id, Team team, int squareId) {
        this.id = id;
        this.team = team;
        this.squareId = squareId;
    }

    protected Piece(Team team) {
        this(0, team, 0);
    }

    public boolean isBlack() {
        return team.equals(Team.BLACK);
    }

    public boolean movable(ConsoleBoard consoleBoard, Square source, Square target) {
        return movable(source, target);
    }

    abstract boolean movable(Square source, Square target);

    public boolean movable(Piece targetPiece, Square source, Square target) {
        return movable(source, target);
    }

    public boolean isNotAlly(Piece target) {
        return this.team != target.team();
    }

    public boolean isSameTeam(Team team) {
        return this.team.equals(team);
    }

    public Team team() {
        return team;
    }

    public abstract boolean isNotEmpty();

    abstract List<Direction> getDirection();

    public abstract double getPoint();

    public abstract boolean isKing();

    public abstract boolean isPawn();

    public abstract String name();

    public abstract boolean canMoveWithoutObstacle(ConsoleBoard board, Square source, Square target);

    public abstract List<Square> getRoute(Square source, Square target);

    public int getId() {
        return id;
    }

    public int getSquareId() {
        return squareId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Piece piece = (Piece) o;

        if (id != piece.id) {
            return false;
        }
        if (squareId != piece.squareId) {
            return false;
        }
        return team == piece.team;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + squareId;
        return result;
    }
}

package chess.domain.game.board.piece;

import chess.domain.game.board.piece.location.Location;
import chess.domain.game.team.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Piece implements Movable {

    private static final int WHITE_INITIAL_Y = 1;
    private static final int BLACK_INITIAL_Y = 8;

    protected final long id;
    protected final Team team;
    protected Location location;

    protected Piece(final long id, final Location location, final Team team) {
        this.id = id;
        this.location = location;
        this.team = team;
    }

    protected static int getInitialY(final Team team) {
        if (team.isBlack()) {
            return BLACK_INITIAL_Y;
        }
        return WHITE_INITIAL_Y;
    }

    public final void move(final Location target) {
        if (isMovable(target)) {
            location = target;
        }
    }

    public final boolean isHere(final Location location) {
        return this.location.equals(location);
    }

    public List<Location> findPath(final Location target) {
        final List<Location> path = new ArrayList<>();
        int subX = location.subtractX(target);
        int subY = location.subtractY(target);
        int dx = subX == 0 ? 0 : subX / Math.abs(subX);
        int dy = subY == 0 ? 0 : subY / Math.abs(subY);

        Location next = location.moveByStep(dx, dy);
        while (!next.equals(target)) {
            path.add(next);
            next = next.moveByStep(dx, dy);
        }
        return path;
    }

    public boolean isSameTeam(final Piece other) {
        return (isBlackTeam() && other.isBlackTeam())
            || (isWhiteTeam() && other.isWhiteTeam());
    }

    public boolean isSameTeam(final Team team) {
        return this.team.equals(team);
    }

    private boolean isBlackTeam() {
        return team.isBlack();
    }

    private boolean isWhiteTeam() {
        return team.isWhite();
    }

    public boolean isPawn() {
        return false;
    }

    public boolean isKing() {
        return false;
    }

    public long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    public Team getTeam() {
        return team;
    }

    public String getTeamValue() {
        return team.getValue();
    }

    public char getPieceTypeValue() {
        return getPieceType().getValue();
    }

    public abstract PieceType getPieceType();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Piece piece = (Piece) o;
        return id == piece.id && getTeam() == piece.getTeam() && Objects
            .equals(location, piece.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getTeam(), location);
    }

}

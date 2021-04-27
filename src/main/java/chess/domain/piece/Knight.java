package chess.domain.piece;

import chess.domain.location.Location;
import chess.domain.team.Team;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Knight extends Piece {

    private static final int LEFT_INITIAL_X = 2;
    private static final int RIGHT_INITIAL_X = 7;

    private Knight(final long id, final Location location, final Team team) {
        super(id, location, team);
    }

    private Knight(final long id, final int x, final Team team) {
        super(id, Location.of(x, getInitialY(team)), team);
    }

    public static Knight of(final long id, final Location location, final Team team) {
        return new Knight(id, location, team);
    }

    public static List<Piece> createInitialPieces() {
        return Arrays.asList(
            new Knight(0L, LEFT_INITIAL_X, Team.WHITE),
            new Knight(0L, RIGHT_INITIAL_X, Team.WHITE),
            new Knight(0L, LEFT_INITIAL_X, Team.BLACK),
            new Knight(0L, RIGHT_INITIAL_X, Team.BLACK)
        );
    }

    @Override
    public boolean isMovable(final Location target) {
        int subX = Math.abs(location.subtractX(target));
        int subY = Math.abs(location.subtractY(target));
        return ((subX == 1 && subY == 2) || (subX == 2 && subY == 1));
    }

    @Override
    public List<Location> findPath(final Location target) {
        return Collections.emptyList();
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KNIGHT;
    }

}

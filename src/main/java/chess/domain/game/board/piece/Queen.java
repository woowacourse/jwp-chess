package chess.domain.game.board.piece;

import chess.domain.game.board.piece.location.Location;
import chess.domain.game.team.Team;
import java.util.Arrays;
import java.util.List;

public class Queen extends Piece {

    private static final int INITIAL_X = 4;

    private Queen(final long id, final Location location, final Team team) {
        super(id, location, team);
    }

    private Queen(final long id, final int x, final Team team) {
        super(id, Location.of(x, getInitialY(team)), team);
    }

    public static Queen of(final long id, final Location location, final Team team) {
        return new Queen(id, location, team);
    }

    public static List<Piece> createInitialPieces() {
        return Arrays.asList(
            new Queen(0L, INITIAL_X, Team.WHITE),
            new Queen(0L, INITIAL_X, Team.BLACK)
        );
    }

    @Override
    public boolean isMovable(final Location target) {
        return location.isHorizontalOrVertical(target) || location.isDiagonal(target);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.QUEEN;
    }

}

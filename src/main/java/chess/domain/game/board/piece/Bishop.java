package chess.domain.game.board.piece;

import chess.domain.game.board.piece.location.Location;
import chess.domain.game.team.Team;
import java.util.Arrays;
import java.util.List;

public class Bishop extends Piece {

    private static final int LEFT_INITIAL_X = 3;
    private static final int RIGHT_INITIAL_X = 6;

    private Bishop(final long id, final Location location, final Team team) {
        super(id, location, team);
    }

    private Bishop(final long id, final int x, final Team team) {
        super(id, Location.of(x, getInitialY(team)), team);
    }

    public static Bishop of(final long id, final Location location, final Team team) {
        return new Bishop(id, location, team);
    }

    public static List<Piece> createInitialPieces() {
        return Arrays.asList(
            new Bishop(0L, LEFT_INITIAL_X, Team.WHITE),
            new Bishop(0L, RIGHT_INITIAL_X, Team.WHITE),
            new Bishop(0L, LEFT_INITIAL_X, Team.BLACK),
            new Bishop(0L, RIGHT_INITIAL_X, Team.BLACK)
        );
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.BISHOP;
    }

    @Override
    public boolean isMovable(final Location target) {
        return location.isDiagonal(target);
    }

}

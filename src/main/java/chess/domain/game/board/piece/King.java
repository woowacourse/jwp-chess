package chess.domain.game.board.piece;

import chess.domain.game.board.piece.location.Location;
import chess.domain.game.team.Team;
import java.util.Arrays;
import java.util.List;

public class King extends Piece {

    private static final int INITIAL_X = 5;

    private King(final long id, final Location location, final Team team) {
        super(id, location, team);
    }

    private King(final long id, final int x, final Team team) {
        super(id, Location.of(x, getInitialY(team)), team);
    }

    public static King of(final long id, final Location location, final Team team) {
        return new King(id, location, team);
    }

    public static List<Piece> createInitialPieces() {
        return Arrays.asList(
            new King(0L, INITIAL_X, Team.WHITE),
            new King(0L, INITIAL_X, Team.BLACK)
        );
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.KING;
    }

    @Override
    public boolean isMovable(final Location target) {
        return location.isAdjacent(target);
    }

}

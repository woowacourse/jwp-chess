package chess.web.domain.piece;

import static chess.web.domain.piece.type.PieceType.PAWN;

import chess.web.domain.piece.type.Direction;
import chess.web.domain.player.type.TeamColor;

public class Pawn extends Piece {
    private static final double DEFAULT_SCORE = 1;
    private static final double HALF_SCORE = DEFAULT_SCORE / 2;

    public Pawn(Long id, TeamColor teamColor) {
        super(id, PAWN, teamColor, DEFAULT_SCORE, Direction.pawnDirections(teamColor));
    }

    public Pawn(TeamColor teamColor) {
        super(PAWN, teamColor, DEFAULT_SCORE, Direction.pawnDirections(teamColor));
    }

    public static double defaultScore() {
        return DEFAULT_SCORE;
    }

    public static double halfScore() {
        return HALF_SCORE;
    }
}

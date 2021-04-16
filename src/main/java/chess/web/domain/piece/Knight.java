package chess.web.domain.piece;

import static chess.web.domain.piece.type.PieceType.KNIGHT;

import chess.web.domain.piece.type.Direction;
import chess.web.domain.player.type.TeamColor;

public class Knight extends Piece {
    private static final double SCORE = 2.5;

    public Knight(Long id, TeamColor teamColor) {
        super(id, KNIGHT, teamColor, SCORE, Direction.knightDirections());
    }

    public Knight(TeamColor teamColor) {
        super(KNIGHT, teamColor, SCORE, Direction.knightDirections());
    }
}

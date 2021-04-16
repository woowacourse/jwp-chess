package chess.web.domain.piece;

import static chess.web.domain.piece.type.PieceType.BISHOP;

import chess.web.domain.piece.type.Direction;
import chess.web.domain.player.type.TeamColor;

public class Bishop extends Piece {
    private static final double SCORE = 3;

    public Bishop(Long id, TeamColor teamColor) {
        super(id, BISHOP, teamColor, SCORE, Direction.bishopDirections());
    }

    public Bishop(TeamColor teamColor) {
        super(BISHOP, teamColor, SCORE, Direction.bishopDirections());
    }
}

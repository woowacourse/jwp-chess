package chess.web.domain.piece;

import static chess.web.domain.piece.type.PieceType.KING;

import chess.web.domain.piece.type.Direction;
import chess.web.domain.player.type.TeamColor;

public class King extends Piece {
    private static final double SCORE = 0;

    public King(Long id, TeamColor teamColor) {
        super(id, KING, teamColor, SCORE, Direction.kingDirections());
    }

    public King(TeamColor teamColor) {
        super(KING, teamColor, SCORE, Direction.kingDirections());
    }
}

package chess.domain.piece;

import static chess.domain.piece.type.PieceType.KING;

import chess.domain.color.type.TeamColor;
import chess.domain.piece.type.Direction;

public class King extends Piece {

    private static final double SCORE = 0;

    public King(TeamColor teamColor) {
        super(KING, teamColor, SCORE, Direction.kingDirections());
    }
}

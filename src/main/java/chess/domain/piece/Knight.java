package chess.domain.piece;

import static chess.domain.piece.type.PieceType.KNIGHT;

import chess.domain.piece.type.Direction;
import chess.domain.color.type.TeamColor;

public class Knight extends Piece {

    private static final double SCORE = 2.5;

    public Knight(TeamColor teamColor) {
        super(KNIGHT, teamColor, SCORE, Direction.knightDirections());
    }
}

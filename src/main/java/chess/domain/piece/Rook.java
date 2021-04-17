package chess.domain.piece;

import static chess.domain.piece.type.PieceType.ROOK;

import chess.domain.color.type.TeamColor;
import chess.domain.piece.type.Direction;

public class Rook extends Piece {

    private static final double SCORE = 5;

    public Rook(TeamColor teamColor) {
        super(ROOK, teamColor, SCORE, Direction.rookDirections());
    }
}

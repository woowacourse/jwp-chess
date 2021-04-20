package chess.chessgame.domain.piece;

import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.piece.attribute.Notation;
import chess.chessgame.domain.piece.attribute.Score;
import chess.chessgame.domain.piece.strategy.RookMoveStrategy;

public class Rook extends Piece {
    private static final Score ROOK_SCORE = new Score(5);
    private static final Notation ROOK_NOTATION = new Notation("R");

    public Rook(Color color) {
        super(color, ROOK_NOTATION, new RookMoveStrategy(), ROOK_SCORE);
    }
}

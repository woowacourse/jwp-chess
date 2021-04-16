package chess.chessgame.domain.piece;

import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.piece.attribute.Notation;
import chess.chessgame.domain.piece.attribute.Score;
import chess.chessgame.domain.piece.strategy.KnightMoveStrategy;

public class Knight extends Piece {
    private static final Score KNIGHT_SCORE = new Score(2.5);
    private static final Notation KNIGHT_NOTATION = new Notation("N");

    public Knight(Color color) {
        super(color, KNIGHT_NOTATION, new KnightMoveStrategy(), KNIGHT_SCORE);
    }
}

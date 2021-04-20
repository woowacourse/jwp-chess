package chess.chessgame.domain.piece;

import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.piece.attribute.Notation;
import chess.chessgame.domain.piece.attribute.Score;
import chess.chessgame.domain.piece.strategy.KingMoveStrategy;

public class King extends Piece {
    private static final Score ZERO_SCORE = new Score(0);
    private static final Notation KING_WORD = new Notation("K");

    public King(Color color) {
        super(color, KING_WORD, new KingMoveStrategy(), ZERO_SCORE);
    }
}

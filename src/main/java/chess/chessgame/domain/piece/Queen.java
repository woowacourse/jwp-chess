package chess.chessgame.domain.piece;

import chess.chessgame.domain.piece.attribute.Color;
import chess.chessgame.domain.piece.attribute.Notation;
import chess.chessgame.domain.piece.attribute.Score;
import chess.chessgame.domain.piece.strategy.QueenMoveStrategy;

public class Queen extends Piece {
    private static final Score QUEEN_SCORE = new Score(9);
    private static final Notation QUEEN_NOTATION = new Notation("Q");

    public Queen(Color color) {
        super(color, QUEEN_NOTATION, new QueenMoveStrategy(), QUEEN_SCORE);
    }
}

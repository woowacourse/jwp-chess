package chess.chessgame.domain.room.game.board.piece;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.piece.attribute.Notation;
import chess.chessgame.domain.room.game.board.piece.attribute.Score;
import chess.chessgame.domain.room.game.board.piece.strategy.KingMoveStrategy;

public class King extends Piece {
    private static final Score ZERO_SCORE = new Score(0);
    private static final Notation KING_WORD = new Notation("K");

    public King(Color color) {
        super(color, KING_WORD, new KingMoveStrategy(), ZERO_SCORE);
    }
}

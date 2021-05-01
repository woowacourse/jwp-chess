package chess.chessgame.domain.room.game.board.piece;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.piece.attribute.Notation;
import chess.chessgame.domain.room.game.board.piece.attribute.Score;
import chess.chessgame.domain.room.game.board.piece.strategy.RookMoveStrategy;

public class Rook extends Piece {
    private static final Score ROOK_SCORE = new Score(5);
    private static final Notation ROOK_NOTATION = new Notation("R");

    public Rook(Color color) {
        super(color, ROOK_NOTATION, new RookMoveStrategy(), ROOK_SCORE);
    }
}

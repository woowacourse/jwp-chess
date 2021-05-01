package chess.chessgame.domain.room.game.board.piece;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.piece.attribute.Notation;
import chess.chessgame.domain.room.game.board.piece.attribute.Score;
import chess.chessgame.domain.room.game.board.piece.strategy.PawnMoveStrategy;

public class Pawn extends Piece {
    public static final double SCORE_DISADVANTAGE_RATIO = 0.5;

    private static final Score PAWN_SCORE = new Score(1);
    private static final Notation PAWN_NOTATION = new Notation("P");

    public Pawn(Color color) {
        super(color, PAWN_NOTATION, new PawnMoveStrategy(color), PAWN_SCORE);
    }
}

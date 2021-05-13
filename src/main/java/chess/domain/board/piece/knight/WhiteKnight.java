package chess.domain.board.piece.knight;

import chess.domain.board.piece.Owner;

public class WhiteKnight extends Knight {

    private static final String SYMBOL = "n";
    private static final WhiteKnight WHITE_KNIGHT = new WhiteKnight();

    private WhiteKnight() {
        super(Owner.WHITE, SYMBOL);
    }

    public static WhiteKnight getInstance() {
        return WHITE_KNIGHT;
    }
}

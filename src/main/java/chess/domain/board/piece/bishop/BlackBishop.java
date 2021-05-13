package chess.domain.board.piece.bishop;

import chess.domain.board.piece.Owner;

public class BlackBishop extends Bishop {

    private static final String SYMBOL = "B";
    private static final BlackBishop BLACK_BISHOP = new BlackBishop();

    private BlackBishop() {
        super(Owner.BLACK, SYMBOL);
    }

    public static BlackBishop getInstance() {
        return BLACK_BISHOP;
    }
}

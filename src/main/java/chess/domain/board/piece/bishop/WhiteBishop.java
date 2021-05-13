package chess.domain.board.piece.bishop;

import chess.domain.board.piece.Owner;

public class WhiteBishop extends Bishop {

    private static final String SYMBOL = "b";
    private static final WhiteBishop WHITE_BISHOP = new WhiteBishop();

    private WhiteBishop() {
        super(Owner.WHITE, SYMBOL);
    }

    public static WhiteBishop getInstance() {
        return WHITE_BISHOP;
    }
}

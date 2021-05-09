package chess.domain.board.piece.rook;

import chess.domain.board.piece.Owner;

public class BlackRook extends Rook {

    private static final String SYMBOL = "R";
    private static final BlackRook BLACK_ROOK = new BlackRook();

    private BlackRook() {
        super(Owner.BLACK, SYMBOL);
    }

    public static BlackRook getInstance() {
        return BLACK_ROOK;
    }
}

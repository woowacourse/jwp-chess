package chess.domain.board.piece.rook;

import chess.domain.board.piece.Owner;

public class WhiteRook extends Rook {

    private static final String SYMBOL = "r";
    private static final WhiteRook WHITE_ROOK = new WhiteRook();

    private WhiteRook() {
        super(Owner.WHITE, SYMBOL);
    }

    public static WhiteRook getInstance() {
        return WHITE_ROOK;
    }
}

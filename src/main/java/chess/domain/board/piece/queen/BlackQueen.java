package chess.domain.board.piece.queen;

import chess.domain.board.piece.Owner;

public class BlackQueen extends Queen {

    private static final String SYMBOL = "Q";
    private static final BlackQueen BLACK_QUEEN = new BlackQueen();

    private BlackQueen() {
        super(Owner.BLACK, SYMBOL);
    }

    public static BlackQueen getInstance() {
        return BLACK_QUEEN;
    }
}

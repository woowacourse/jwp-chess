package chess.domain.board.piece.king;

import chess.domain.board.piece.Owner;

public class BlackKing extends King {

    private static final String SYMBOL = "K";
    private static final BlackKing BLACK_KING = new BlackKing();

    public BlackKing() {
        super(Owner.BLACK, SYMBOL);
    }

    public static BlackKing getInstance() {
        return BLACK_KING;
    }
}

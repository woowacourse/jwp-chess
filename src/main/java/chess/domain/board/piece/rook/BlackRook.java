package chess.domain.board.piece.rook;

import chess.domain.board.piece.Owner;

public class BlackRook extends Rook {

    private static final BlackRook BLACK_ROOK = new BlackRook();

    private BlackRook() {
        super(Owner.BLACK);
    }

    public static BlackRook getInstance() {
        return BLACK_ROOK;
    }

    @Override
    public String getSymbol() {
        return "R";
    }
}

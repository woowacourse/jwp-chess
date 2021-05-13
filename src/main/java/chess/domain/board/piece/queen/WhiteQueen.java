package chess.domain.board.piece.queen;

import chess.domain.board.piece.Owner;

public class WhiteQueen extends Queen {

    private static final String SYMBOL = "q";
    private static final WhiteQueen WHITE_QUEEN = new WhiteQueen();

    private WhiteQueen() {
        super(Owner.WHITE, SYMBOL);
    }

    public static WhiteQueen getInstance() {
        return WHITE_QUEEN;
    }
}

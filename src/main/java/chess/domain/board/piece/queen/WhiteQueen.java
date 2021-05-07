package chess.domain.board.piece.queen;

import chess.domain.board.piece.Owner;

public class WhiteQueen extends Queen {

    private static final WhiteQueen WHITE_QUEEN = new WhiteQueen();

    private WhiteQueen() {
        super(Owner.WHITE);
    }

    public static WhiteQueen getInstance() {
        return WHITE_QUEEN;
    }

    @Override
    public String getSymbol() {
        return "q";
    }
}

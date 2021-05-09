package chess.domain.board.piece.king;

import chess.domain.board.piece.Owner;
import chess.domain.board.piece.bishop.WhiteBishop;

public class WhiteKing extends King {

    private static final String SYMBOL = "k";
    private static final WhiteKing WHITE_KING = new WhiteKing();

    public WhiteKing() {
        super(Owner.WHITE, SYMBOL);
    }

    public static WhiteKing getInstance() {
        return WHITE_KING;
    }
}

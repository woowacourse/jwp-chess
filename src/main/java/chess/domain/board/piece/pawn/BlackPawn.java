package chess.domain.board.piece.pawn;

import chess.domain.board.position.Vertical;
import chess.domain.direction.Direction;
import chess.domain.board.piece.Owner;

public class BlackPawn extends Pawn {

    private static final String SYMBOL = "P";
    private static final BlackPawn BLACK_PAWN = new BlackPawn();

    public BlackPawn() {
        super(Owner.BLACK, SYMBOL, Direction.blackPawnDirections());
    }

    public static BlackPawn getInstance() {
        return BLACK_PAWN;
    }

    @Override
    public boolean isFirstLine(final Vertical vertical) {
        return Vertical.SEVEN.equals(vertical);
    }
}

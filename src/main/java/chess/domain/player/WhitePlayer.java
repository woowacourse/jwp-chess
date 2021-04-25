package chess.domain.player;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.State;

import java.util.Map;

public class WhitePlayer extends Player {
    private static final String NAME = "white";

    public WhitePlayer(final Map<Position, Piece> board, final String currentTurn) {
        super(board, currentTurn, NAME);
    }

    public WhitePlayer(final State state) {
        super(state);
    }
}

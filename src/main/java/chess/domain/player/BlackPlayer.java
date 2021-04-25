package chess.domain.player;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.State;

import java.util.Map;

public class BlackPlayer extends Player {
    private static final String NAME = "black";

    public BlackPlayer(final Map<Position, Piece> board, final String currentTurn) {
        super(board, currentTurn, NAME);
    }

    public BlackPlayer(final State state) {
        super(state);
    }
}

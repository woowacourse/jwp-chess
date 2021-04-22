package chess.domain.player;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.State;

import java.util.Map;

public class BlackPlayer extends Player {
    private static final String NAME = "black";

    public BlackPlayer(final State state) {
        super(state);
    }

    public BlackPlayer(Map<Position, Piece> board, String currentTurn) {
        super(board, currentTurn, NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }
}

package chess.domain.player;

import chess.domain.piece.Piece;
import chess.domain.piece.PiecesFactory;
import chess.domain.position.Position;
import chess.domain.state.RunningTurn;
import chess.domain.state.State;

import java.util.Map;

public class WhitePlayer extends Player {
    private static final String NAME = "white";

    public WhitePlayer(final State state) {
        super(state);
    }

    public WhitePlayer(Map<Position, Piece> board, String currentTurn) {
        super(board, currentTurn, NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }
}

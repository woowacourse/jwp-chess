package chess.domain.player;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.piece.PiecesFactory;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.FinishedTurn;
import chess.domain.state.RunningTurn;
import chess.domain.state.State;

import java.util.Map;

public abstract class Player {
    private State state;

    public Player(final Map<Position, Piece> board, final String currentTurn, final String name) {
        if (name.equals(currentTurn)) {
            this.state = new RunningTurn(PiecesFactory.pieces(board, name));
        }
        if (!name.equals(currentTurn)) {
            this.state = new FinishedTurn(PiecesFactory.pieces(board, name));
        }
    }

    protected Player(final State state) {
        this.state = state;
    }

    public final boolean isFinish() {
        return state.isFinishedTurn();
    }

    public final void move(final Source source, final Target target, final State targetState) {
        this.state = this.state.move(source, target, targetState);
    }

    public final double calculateScore() {
        return state.calculateScore();
    }

    public final void toRunningState(final State anotherState) {
        this.state = this.state.toRunningTurn(anotherState);
    }

    public final State getState() {
        return state;
    }

    public final Pieces getPieces() {
        return state.pieces();
    }
}

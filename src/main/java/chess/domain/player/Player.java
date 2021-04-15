package chess.domain.player;

import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.State;

import java.util.Optional;

public abstract class Player {
    private State state;

    protected Player(final State state) {
        this.state = state;
    }

    public boolean isBlack() {
        return state.isBlack();
    }

    public final State state() {
        return state;
    }

    public final boolean isFinish() {
        return state.isFinish();
    }

    public final void move(final Source source, final Target target) {
        this.state = this.state.move(source, target);
    }

    public final Optional<Piece> findPiece(final Position position) {
        return state.findPiece(position);
    }

    public final Pieces pieces() {
        return state().pieces();
    }

/*    public final double calculateScore() {
        return state.pieces().calculateScore();
    }*/

    public final void toRunningState(final State anotherState) {
        this.state = this.state.toRunningState(anotherState);
    }

    public abstract String getName();
}

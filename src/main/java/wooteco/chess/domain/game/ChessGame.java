package wooteco.chess.domain.game;

import wooteco.chess.domain.game.state.State;
import wooteco.chess.domain.piece.Position;

public class ChessGame {
    private State state;

    private ChessGame(State state) {
        this.state = state;
    }

    public static ChessGame of(State state) {
        return new ChessGame(state);
    }

    public void start() {
        state = state.start();
    }

    public void move(Position source, Position target) {
        state = state.move(source, target);
    }

    public void end() {
        state = state.end();
    }

    public Board board() {
        return state.board();
    }

    public Turn turn() {
        return state.turn();
    }

    public boolean isFinished() {
        return state.isFinished();
    }

    public Status status() {
        return state.status();
    }

    public State getState() {
        return state;
    }
}

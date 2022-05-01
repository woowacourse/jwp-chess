package chess.domain;

import chess.piece.Color;
import chess.state.Finish;
import chess.state.Ready;
import chess.state.State;
import chess.state.StateFactory;

public class CurrentStatus {
    State state;
    Color turn;

    public CurrentStatus() {
        this.state = new Ready();
        this.turn = Color.WHITE;
    }

    public CurrentStatus(String state, String turn) {
        this.state = StateFactory.valueOf(state).create();
        this.turn = Color.valueOf(turn);
    }

    private void nextTurn() {
        if (turn == Color.BLACK) {
            turn = Color.WHITE;
            return;
        }
        if (turn == Color.WHITE) {
            turn = Color.BLACK;
        }
    }

    public String getTurnToString() {
        return turn.getColor();
    }

    public String getStateToString() {
        return state.getStateToString();
    }

    public void start() {
        state = state.start();
    }

    public void move(Chessboard chessboard, MovingPosition movingPosition) {
        state = state.move(chessboard, movingPosition, turn);
        nextTurn();
    }

    public void end() {
        state = new Finish();
    }


    public boolean isFinished() {
        return state.isFinished();
    }

    public boolean canBeDeleted() {
        return state.canBeDeleted();
    }
}

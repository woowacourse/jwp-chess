package wooteco.chess.domain.game.state;

import wooteco.chess.domain.game.Board;
import wooteco.chess.domain.game.Status;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.domain.piece.Position;

public class Ready implements State {
    @Override
    public State start() {
        return new Playing(Board.create(), Turn.from(Color.WHITE));
    }

    @Override
    public State end() {
        return new Finished(Board.EMPTY, Turn.from(Color.WHITE));
    }

    @Override
    public State move(Position source, Position target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Board board() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Turn turn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Status status() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "READY";
    }
}

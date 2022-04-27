package chess.state;

import chess.chessgame.Chessboard;

import java.util.function.Function;

public enum StateFactory {
    READY(chessboard -> new Ready()),
    PLAY(Play::new),
    FINISH(Finish::new);

    private final Function<Chessboard, State> creator;

    StateFactory(Function<Chessboard, State> creator) {
        this.creator = creator;
    }

    public State create(Chessboard chessboard) {
        return creator.apply(chessboard);
    }
}

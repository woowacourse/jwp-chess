package wooteco.chess.domain.game.state;

import wooteco.chess.domain.game.Board;
import wooteco.chess.domain.game.Status;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.piece.Position;

public interface State {
    State start();

    State end();

    State move(Position source, Position target);

    Board board();

    Turn turn();

    Status status();

    boolean isFinished();
}

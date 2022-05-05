package chess.domain.state;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.position.Position;

public interface State {

    State start();

    boolean isEnd();

    State move(final Position from, final Position to);

    double score(final Color color);

    Result getWinner();

    State end();

    Board getBoard();
}

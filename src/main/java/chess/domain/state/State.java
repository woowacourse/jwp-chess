package chess.domain.state;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.Map;

public interface State {

    State start();

    boolean isEnd();

    State move(final Position from, final Position to);

    double score(final Color color);

    Result getWinner();

    State end();

    Board getBoard();
}

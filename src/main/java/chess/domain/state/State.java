package chess.domain.state;

import chess.domain.chessboard.position.Position;
import chess.domain.piece.Piece;
import chess.domain.game.Player;

import java.util.Map;

public interface State {
    boolean isRunning();

    State proceed(final String command);

    Map<Position, Piece> getBoard();

    boolean isStatus();

    Player getNextTurnPlayer();
}

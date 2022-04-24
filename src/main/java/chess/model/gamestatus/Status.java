package chess.model.gamestatus;

import chess.model.Color;
import chess.model.game.GameResult;
import chess.model.board.MoveResult;
import chess.model.board.Score;
import chess.model.board.Square;
import chess.model.piece.Piece;
import java.util.Map;

public interface Status {
    Status start();

    MoveResult move(Square from, Square to, Color turn);

    Score getScore();

    GameResult getResult();

    Status end();

    boolean isEnd();

    Map<Square, Piece> getBoard();
}

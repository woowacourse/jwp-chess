package chess.chessgame.domain.room.game;

import chess.chessgame.domain.room.game.board.Board;
import chess.chessgame.domain.room.game.board.Square;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;

import java.util.List;

public interface ChessGameManager {
    ChessGameManager start();

    ChessGameManager end();

    void move(Position from, Position to);

    Board getBoard();

    Color nextColor();

    boolean isNotEnd();

    boolean isEnd();

    boolean isStart();

    boolean isKingDead();

    long getId();

    List<Square> getAliveSquares();

    ChessGameStatistics getStatistics();
}

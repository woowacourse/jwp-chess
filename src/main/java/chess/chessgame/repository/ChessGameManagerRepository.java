package chess.chessgame.repository;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;

public interface ChessGameManagerRepository {
    ChessGameManager findById(long id);

    ChessGameManagerBundle findRunningGames();

    long add(ChessGameManager chessGameManager);

    void update(ChessGameManager chessGameManager);

    void delete(long id);

    boolean isEnd(long gameId);
}

package chess.repository;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;

import java.util.List;

public interface GameRepository {
    long save(ChessGameManager chessGameManager);

    Color findCurrentTurnByGameId(long gameId);

    void updateTurnByGameId(ChessGameManager chessGameManager, long gameId);

    List<Long> findAllGamesId();

    void delete(long gameId);
}

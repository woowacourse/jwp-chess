package chess.repository;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;

import java.util.List;

public interface GameRepository {
    int saveNewGame(ChessGameManager chessGameManager);

    Color loadCurrentTurnByGameId(int gameId);

    void updateTurnByGameId(ChessGameManager chessGameManager, int gameId);

    List<Integer> loadGames();
}

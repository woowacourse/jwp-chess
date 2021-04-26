package chess.repository;

import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.dto.GameEntryDto;

import java.util.List;

public interface GameRepository {
    long save(ChessGameManager chessGameManager, String title);

    Color findCurrentTurnByGameId(long gameId);

    void updateTurnByGameId(ChessGameManager chessGameManager, long gameId);

    List<GameEntryDto> findAllGames();

    void delete(long gameId);
}

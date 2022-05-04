package chess.repository;

import chess.domain.Color;
import chess.domain.GameState;

public interface BoardDao {
    int save(int roomId, GameState gameState);

    Color getTurn(int boardId);

    int getBoardIdByRoom(int roomId);

    void updateTurn(int boardId, GameState gameState);

    void deleteByRoom(int roomId);
}

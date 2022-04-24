package chess.repository;

import chess.domain.Color;
import chess.web.dto.GameStateDto;

public interface BoardRepository {
    int save(int roomId, GameStateDto gameStateDto);

    Color getTurn(int boardId);

    int getBoardIdByRoom(int roomId);

    void updateTurn(int boardId, GameStateDto gameStateDto);

    void deleteByRoom(int roomId);
}

package chess.web.dao;

import chess.domain.Color;
import chess.web.dto.GameStateDto;

public interface BoardRepository {
    int save(int userId, GameStateDto gameStateDto);

    Color getTurn(int boardId);

    int getBoardIdByroom(int roomId);

    void update(int boardId, GameStateDto gameStateDto);

    void deleteByroom(int roomId);
}

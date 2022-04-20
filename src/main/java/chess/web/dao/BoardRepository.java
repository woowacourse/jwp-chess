package chess.web.dao;

import chess.domain.Color;
import chess.web.dto.GameStateDto;

public interface BoardRepository {
    int save(int userId, GameStateDto gameStateDto);

    Color getTurn(int boardId);

    int getBoardIdByPlayer(int playerId);

    void update(int boardId, GameStateDto gameStateDto);

    void deleteByPlayer(int playerId);
}

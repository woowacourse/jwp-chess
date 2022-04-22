package chess.dao;

import chess.domain.Color;
import chess.web.dto.GameStateDto;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepositoryImpl implements BoardRepository {

    @Override
    public int save(int roomId, GameStateDto gameStateDto) {
        return 0;
    }

    @Override
    public Color getTurn(int boardId) {
        return null;
    }

    @Override
    public int getBoardIdByRoom(int roomId) {
        return 0;
    }

    @Override
    public void update(int boardId, GameStateDto gameStateDto) {

    }

    @Override
    public void deleteByRoom(int roomId) {

    }
}

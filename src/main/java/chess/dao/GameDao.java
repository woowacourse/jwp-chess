package chess.dao;

import chess.service.dto.GameEntity;
import chess.service.dto.GamesDto;
import chess.service.dto.StatusDto;

public interface GameDao {
    void update(GameEntity dto);

    GameEntity findById(int id);

    GamesDto findAll();

    int createGame(String name);
}

package chess.dao;

import chess.entity.GameEntity;
import java.util.List;

public interface GameDao {
    int update(GameEntity dto);

    GameEntity findById(int id);

    List<GameEntity> findAll();

    int createGame(String name, String password);

    int deleteGame(int id);

    String findPasswordById(int gameId);
}

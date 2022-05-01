package chess.repository.dao;

import chess.repository.dao.entity.GameEntity;
import java.util.List;

public interface GameDao {
    int update(GameEntity dto);

    GameEntity findById(Integer id);

    List<GameEntity> findAll();

    int createGame(String name, String password);

    int deleteGame(Integer id);

    String findPasswordById(Integer gameId);
}

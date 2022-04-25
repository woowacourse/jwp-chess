package chess.dao;

import java.util.List;

public interface GameDao {
    void update(GameEntity dto);

    GameEntity findById(int id);

    List<GameEntity> findAll();

    int createGame(String name);
}

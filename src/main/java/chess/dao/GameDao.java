package chess.dao;

import chess.dao.entity.GameEntity;

import java.util.List;

public interface GameDao {

    Long save(GameEntity game);

    void removeById(Long id);

    GameEntity findGameById(Long id);

    String findPasswordById(Long id);

    String findStatusById(Long id);

    List<GameEntity> findAll();

    void updateGame(Long id, String turn, String status);

    void updateStatus(Long id, String status);
}
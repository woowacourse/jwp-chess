package chess.database.dao;

import java.util.Optional;

import chess.database.entity.GameEntity;

public interface GameDao {
    Long saveGame(GameEntity entity);

    void updateGame(GameEntity entity);

    Optional<GameEntity> findGameById(Long gameId);

    Optional<GameEntity> findGameByRoomId(Long roomId);
}

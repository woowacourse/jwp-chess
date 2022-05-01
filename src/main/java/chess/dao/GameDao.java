package chess.dao;

import chess.entity.GameEntity;

public interface GameDao {

    void updateById(GameEntity gameEntity);
    GameEntity findById(GameEntity gameEntity);
    void insert(GameEntity gameEntity);
    void deleteById(GameEntity gameEntity);
    int insertWithKeyHolder(GameEntity gameEntity);

}

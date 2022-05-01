package chess.dao;

import chess.entity.GameEntity;
import java.util.HashMap;
import java.util.Map;

public class FakeGameDao implements GameDao {
    private final Map<Integer, GameEntity> storage;
    private int autoIncrease = 1;

    public FakeGameDao() {
        this.storage = new HashMap<>();
    }

    @Override
    public void updateById(GameEntity gameEntity) {
        storage.replace(gameEntity.getGameId(), gameEntity);
    }

    @Override
    public GameEntity findById(GameEntity gameEntity) {
        return storage.get(gameEntity.getGameId());
    }

    @Override
    public void insert(GameEntity gameEntity) {
        storage.put(autoIncrease++, gameEntity);
    }

    @Override
    public void deleteById(GameEntity gameEntity) {
        storage.remove(gameEntity.getGameId());
    }

    @Override
    public int insertWithKeyHolder(GameEntity gameEntity) {
        storage.put(autoIncrease, gameEntity);
        return autoIncrease++;
    }
}

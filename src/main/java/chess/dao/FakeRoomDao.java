package chess.dao;

import chess.entity.RoomEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRoomDao implements RoomDao {
    private final Map<Integer, RoomEntity> storage;

    public FakeRoomDao() {
        this.storage = new HashMap<>();
    }

    @Override
    public void insert(RoomEntity roomEntity) {
        storage.put(roomEntity.getGameId(), roomEntity);
    }

    @Override
    public List<RoomEntity> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(RoomEntity roomEntity) {
        storage.remove(roomEntity.getGameId());
    }

    @Override
    public RoomEntity findById(RoomEntity roomEntity) {
        return storage.get(roomEntity.getGameId());
    }

    @Override
    public void updateById(RoomEntity roomEntity) {
        storage.replace(roomEntity.getGameId(), roomEntity);
    }
}

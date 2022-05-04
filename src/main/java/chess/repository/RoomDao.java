package chess.repository;

import chess.repository.entity.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomDao {
    int save(RoomEntity room);

    Optional<RoomEntity> findByName(String name);

    Optional<RoomEntity> findById(int roomId);

    void deleteById(int id);

    List<RoomEntity> findAll();
}

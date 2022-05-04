package chess.database.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import chess.database.entity.RoomEntity;

@Repository
public interface RoomDao {
    Long saveRoom(RoomEntity entity);

    Optional<RoomEntity> findRoomById(Long id);

    List<RoomEntity> findAll();

    void deleteRoom(Long roomId);
}

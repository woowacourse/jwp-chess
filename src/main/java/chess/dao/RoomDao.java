package chess.dao;

import chess.entity.RoomEntity;
import java.util.List;
import java.util.Optional;

public interface RoomDao {
    long save(RoomEntity room);

    Optional<RoomEntity> findById(long id);

    List<RoomEntity> findAll();

    void updateTurn(long id, String turn);

    void deleteRoom(long id);
}

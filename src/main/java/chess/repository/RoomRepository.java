package chess.repository;

import chess.entity.RoomEntity;
import java.util.List;

public interface RoomRepository {
    List<RoomEntity> findRooms();

    RoomEntity insert(RoomEntity room);

    void updateTeam(Long id, String team);

    RoomEntity findById(Long id);

    void updateGameOver(Long id);
}

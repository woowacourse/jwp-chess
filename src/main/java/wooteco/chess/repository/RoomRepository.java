package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wooteco.chess.domain.Color;
import wooteco.chess.repository.entity.RoomEntity;

public interface RoomRepository extends CrudRepository<RoomEntity, Integer> {

    @Modifying
    @Query("UPDATE room SET room_color = :currentColor WHERE room_id = :roomId")
    void updateRoomColorById(int roomId, String currentColor);

}

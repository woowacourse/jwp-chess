package wooteco.chess.boot.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import wooteco.chess.boot.entity.RoomInfoEntity;

public interface RoomInfoRepository extends CrudRepository<RoomInfoEntity, Long> {

    @Query("SELECT id from room_info where room_id = :roomId limit 1")
    Long findIdByRoomId(Long roomId);

    @Query("SELECT turn from room_info where room_id = :roomId limit 1")
    String findTurnByRoomId(Long roomId);
}

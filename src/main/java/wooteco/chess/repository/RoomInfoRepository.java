package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomInfoRepository extends CrudRepository<RoomInfo, Long> {
    List<RoomInfo> findAll();

    @Query("SELECT * FROM room_info WHERE room_name = :room_name")
    List<RoomInfo> findByRoomName(@Param("room_name") String roomName);

    @Query("SELECT * FROM room_info WHERE room_name = :room_name LIMIT 1")
    RoomInfo findByRoomNameOnlyOne(@Param("room_name") String roomName);
}

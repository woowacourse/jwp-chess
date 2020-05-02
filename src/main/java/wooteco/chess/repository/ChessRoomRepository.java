package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.chess.dto.ChessRoom;

public interface ChessRoomRepository extends CrudRepository<ChessRoom, Long> {
    @Modifying
    @Query("INSERT INTO chess_room VALUES (:id, :room_name)")
    void saveRoom(@Param("id") Long id, @Param("room_name") String roomName);
}

package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PieceInfoRepository extends CrudRepository<PieceInfo, Long> {

    List<PieceInfo> findAll();

    @Query("SELECT * FROM piece_info where room_name_hash = :room_name_hash")
    List<PieceInfo> findByRoomNameHash(@Param("room_name_hash") String roomNameHash);

    @Query("SELECT * FROM piece_info WHERE room_name_hash = :room_name_hash AND position = :position LIMIT 1")
    PieceInfo findByRoomNameHashAndPositionOnlyOne(@Param("room_name_hash") String roomName, @Param("position") String position);
}

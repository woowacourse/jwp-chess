package chess.repository;

import chess.entity.RoomEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends CrudRepository<RoomEntity, Integer> {
	@Modifying
	@Query("INSERT INTO room (room_name) VALUES (:room_name)")
	void saveByRoomName(@Param("room_name") final String roomName);

	@Query("SELECT * FROM room WHERE room_name = :room_name")
	Optional<RoomEntity> findByRoomName(@Param("room_name") final String roomName);

	@Modifying
	@Query("DELETE FROM room WHERE room_name = :room_name")
	void deleteByRoomName(@Param("room_name") final String roomName);
}

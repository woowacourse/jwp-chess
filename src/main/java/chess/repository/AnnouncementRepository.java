package chess.repository;

import chess.entity.AnnouncementEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnnouncementRepository extends CrudRepository<AnnouncementEntity, Integer> {
	@Modifying
	@Query("INSERT INTO announcement(message, room_id) VALUES(:message, :room_id)")
	void save(@Param("message") final String message, @Param("room_id") final int roomId);

	@Query("SELECT * FROM announcement WHERE room_id=:room_id")
	Optional<AnnouncementEntity> findByRoomId(@Param("room_id") final int roomId);

	@Modifying
	@Query("UPDATE announcement SET message = :message WHERE room_id=:room_id")
	void setByRoomId(@Param("room_id") final int roomId, @Param("message") final String message);
}

package chess.repository;

import chess.entity.StateEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StateRepository extends CrudRepository<StateEntity, Integer> {

	@Modifying
	@Query("INSERT INTO state (state, room_id) VALUES (:state, :room_id)")
	void save(@Param("state") final String state, @Param("room_id") final int roomId);

	@Query("SELECT * FROM state WHERE room_id=:room_id")
	Optional<StateEntity> findByRoomId(@Param("room_id") final int roomId);

	@Modifying
	@Query("UPDATE state SET state=:state WHERE room_id=:room_id")
	void setByRoomId(@Param("state") final String state, @Param("room_id") final int roomId);
}

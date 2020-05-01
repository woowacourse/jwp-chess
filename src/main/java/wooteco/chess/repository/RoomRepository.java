package wooteco.chess.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<Room, Long> {
	@Override
	List<Room> findAll();

	@Query("SELECT * FROM room WHERE room_name = :name")
	Optional<Room> findByRoomName(@Param("name") String name);

	@Override
	<S extends Room> S save(S room);
}

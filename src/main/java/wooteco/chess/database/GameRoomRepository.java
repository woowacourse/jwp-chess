package wooteco.chess.database;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.GameRoom;

public interface GameRoomRepository extends CrudRepository<GameRoom, Long> {

	@Override
	List<GameRoom> findAll();

	@Query("SELECT * FROM game_room WHERE name = :name")
	Optional<GameRoom> findByName(@Param("name") final String name);

}
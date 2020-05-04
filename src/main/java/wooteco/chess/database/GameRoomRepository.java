package wooteco.chess.database;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.chess.entity.GameRoom;

import java.util.List;

public interface GameRoomRepository extends CrudRepository<GameRoom, Long> {

	@Override
	List<GameRoom> findAll();

	@Query("SELECT * FROM game_room WHERE name = :name")
	GameRoom findByName(@Param("name") final String name);

}
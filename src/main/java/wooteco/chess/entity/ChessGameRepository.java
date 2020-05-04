package wooteco.chess.entity;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessGameRepository extends CrudRepository<ChessGameEntity, String> {
	@Query("SELECT id FROM chess_game_entity")
	List<String> findRoomIds();
}

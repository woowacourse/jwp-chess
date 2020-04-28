package wooteco.chess.entity;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChessGameRepository extends CrudRepository<ChessGameEntity, Long> {
	@Query("SELECT id FROM chess_game_entity")
	public List<Long> findRoomIds();
}

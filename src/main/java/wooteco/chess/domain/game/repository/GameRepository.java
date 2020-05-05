package wooteco.chess.domain.game.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<GameEntity, String> {
	@Query("SELECT id FROM game_entity")
	List<String> findAllIds();

}

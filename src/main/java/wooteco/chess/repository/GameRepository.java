package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import wooteco.chess.domain.entity.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
	@Override
	List<Game> findAll();

	@Query("SELECT ID FROM GAME")
	List<Long> finAllGameId();
}

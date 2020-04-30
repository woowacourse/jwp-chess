package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.entity.TurnEntity;

@Repository
public interface TurnRepository extends CrudRepository<TurnEntity, Long> {
	@Query("SELECT * FROM turn LIMIT 1")
	TurnEntity findFirst();
}

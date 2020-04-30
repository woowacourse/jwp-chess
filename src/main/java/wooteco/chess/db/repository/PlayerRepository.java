package wooteco.chess.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.db.entity.PlayerEntity;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {

}

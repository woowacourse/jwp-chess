package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.repository.entity.GameEntity;

public interface GameRepository extends CrudRepository<GameEntity, Long> {
}
